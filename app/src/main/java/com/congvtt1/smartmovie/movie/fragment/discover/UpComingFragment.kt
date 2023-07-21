package com.congvtt1.smartmovie.movie.fragment.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.congvtt1.domain.model.Movie
import com.congvtt1.smartmovie.base.viewmodel.ViewModelFactory
import com.congvtt1.smartmovie.databinding.FragmentUpComingBinding
import com.congvtt1.smartmovie.movie.adapter.MovieRecyclerAdapter
import com.congvtt1.smartmovie.movie.common.MovieConstant
import com.congvtt1.smartmovie.movie.viewmodel.MoviesViewModel

class UpComingFragment : Fragment() {
    private lateinit var binding: FragmentUpComingBinding
    private val viewModel: MoviesViewModel by lazy {
        ViewModelProvider(requireParentFragment(), ViewModelFactory())[MoviesViewModel::class.java]
    }
    private lateinit var layoutManagerGridUpComing: GridLayoutManager
    private lateinit var layoutManagerMenuUpComing: LinearLayoutManager
    private lateinit var movieUpComingAdapter: MovieRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentUpComingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnRefresh()
        initRecyclerView()
        observeData(viewLifecycleOwner)
    }

    private fun setOnRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.resetApiPageUpComing()
            viewModel.loadUpComingMovies(1)
        }
    }

    private fun initRecyclerView() {
        layoutManagerGridUpComing = GridLayoutManager(requireContext(), 2)
        layoutManagerMenuUpComing = LinearLayoutManager(requireContext())
        movieUpComingAdapter = MovieRecyclerAdapter { navigateToDetailFragment(it) }
        binding.rcMovieUpComing.layoutManager = layoutManagerGridUpComing
        binding.rcMovieUpComing.adapter = movieUpComingAdapter

        binding.rcMovieUpComing.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(1)) {
                    if (!viewModel.currentState.loading && !viewModel.currentState.loadMore.loadMoreUpComing) {
                        viewModel.loadUpComingMovies(viewModel.currentState.apiPage.apiPageUpComing + 1)
                    }
                }
            }
        })
    }

    private fun switchLayoutItem(viewType: Int) {
        val firstVisibleItemPosition =
            (binding.rcMovieUpComing.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (viewType == MovieConstant.VIEW_TYPE_GRID) {
            binding.rcMovieUpComing.layoutManager = layoutManagerGridUpComing
        } else {
            binding.rcMovieUpComing.layoutManager = layoutManagerMenuUpComing
        }
        binding.rcMovieUpComing.scrollToPosition(firstVisibleItemPosition)
    }

    private fun observeData(lifecycleOwner: LifecycleOwner) {
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.viewType },
            observer = { viewType ->
                movieUpComingAdapter.setViewType(viewType)
                switchLayoutItem(viewType)
            }
        )
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.moviesUpComing },
            observer = { data ->
                movieUpComingAdapter.submitList(data)
            }
        )
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.loadMore.loadMoreUpComing },
            observer = { loadMore ->
                if (loadMore) {
                    binding.progressLoading.visibility = View.VISIBLE
                } else {
                    binding.progressLoading.visibility = View.GONE
                }
            }
        )
    }

    private fun navigateToDetailFragment(movie: Movie) {
        val direction = HomeFragmentDirections.actionHomeFragmentToDetailFragment(movie.id)
        findNavController().navigate(direction)
    }

    companion object {
        fun newInstance() = UpComingFragment()
    }
}