package com.congvtt1.smartmovie.movie.fragment.discover

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.congvtt1.smartmovie.R
import com.congvtt1.smartmovie.base.viewmodel.ViewModelFactory
import com.congvtt1.smartmovie.databinding.FragmentHomeBinding
import com.congvtt1.smartmovie.movie.adapter.MoviePageAdapter
import com.congvtt1.smartmovie.movie.common.MovieConstant
import com.congvtt1.smartmovie.movie.viewmodel.MoviesViewModel
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MoviesViewModel by viewModels { ViewModelFactory() }
    private lateinit var moviePageAdapter: MoviePageAdapter
    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        initDialog()
        setOnClick()
        observeData(requireActivity())
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadMovies()
    }

    private fun setupView() {
        moviePageAdapter = MoviePageAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = moviePageAdapter
    }

    private fun initDialog() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_load_data)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        val reload: TextView = dialog.findViewById(R.id.tvReload)
        reload.setOnClickListener {
            dialog.cancel()
            viewModel.loadMovies()
        }
    }

    private fun setOnClick() {
        binding.ivMenu.setOnClickListener {
            if (viewModel.currentState.viewType == MovieConstant.VIEW_TYPE_GRID) {
                Glide.with(binding.ivMenu)
                    .load(R.drawable.round_grid_view_teal_24)
                    .into(binding.ivMenu)

                viewModel.setViewType(MovieConstant.VIEW_TYPE_MENU)
            } else {
                Glide.with(binding.ivMenu)
                    .load(R.drawable.round_menu_24)
                    .into(binding.ivMenu)

                viewModel.setViewType(MovieConstant.VIEW_TYPE_GRID)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData(lifecycleOwner: LifecycleOwner) {
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.error },
            observer = { error ->
                if (error != null) {
                    dialog.show()
                    Toast.makeText(context, "${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.tabMovie },
            observer = { tabMovie ->
                if (tabMovie.tabPopular || tabMovie.tabNowPlaying || tabMovie.tabUpComing || tabMovie.tabTopRated) {
                    moviePageAdapter.clearData()
                    moviePageAdapter.addFragment(MoviesFragment.newInstance(), "Movies")
                    if (tabMovie.tabPopular) {
                        moviePageAdapter.addFragment(PopularFragment.newInstance(), "Popular")
                    }
                    if (tabMovie.tabTopRated) {
                        moviePageAdapter.addFragment(TopRatedFragment.newInstance(), "Top Rated")
                    }
                    if (tabMovie.tabUpComing) {
                        moviePageAdapter.addFragment(UpComingFragment.newInstance(), "Up Coming")
                    }
                    if (tabMovie.tabNowPlaying) {
                        moviePageAdapter.addFragment(NowPlayingFragment.newInstance(), "Now Playing")
                    }

                    binding.viewPager.offscreenPageLimit = moviePageAdapter.itemCount - 1
                    binding.viewPager.adapter?.notifyDataSetChanged()

                    TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                        tab.text = moviePageAdapter.getTitle(position)
                    }.attach()
                }
            }
        )
        viewModel.store.observeAnyway(
            owner = lifecycleOwner,
            selector = { state -> state.loading },
            observer = { loading ->
                if (loading) {
                    binding.progressLoading.visibility = View.VISIBLE
                } else {
                    binding.progressLoading.visibility = View.GONE
                }
            }
        )
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.tabTitle },
            observer = { tabTitle ->
                binding.viewPager.currentItem = moviePageAdapter.getIndexTitle(tabTitle)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetError()
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}