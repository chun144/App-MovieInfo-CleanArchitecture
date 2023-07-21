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
import com.congvtt1.smartmovie.databinding.FragmentTopRatedBinding
import com.congvtt1.smartmovie.movie.adapter.MovieRecyclerAdapter
import com.congvtt1.smartmovie.movie.common.MovieConstant
import com.congvtt1.smartmovie.movie.viewmodel.MoviesViewModel

class TopRatedFragment : Fragment() {
    private lateinit var binding: FragmentTopRatedBinding
    private val viewModel: MoviesViewModel by lazy {
        ViewModelProvider(requireParentFragment(), ViewModelFactory())[MoviesViewModel::class.java]
    }
    private lateinit var layoutManagerGridTopRated: GridLayoutManager
    private lateinit var layoutManagerMenuTopRated: LinearLayoutManager
    private lateinit var movieTopRatedAdapter: MovieRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTopRatedBinding.inflate(inflater, container, false)
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
            viewModel.resetApiPageTopRated()
            viewModel.loadTopRatedMovies(1)
        }
    }

    private fun initRecyclerView() {
        layoutManagerGridTopRated = GridLayoutManager(requireContext(), 2)
        layoutManagerMenuTopRated = LinearLayoutManager(requireContext())
        movieTopRatedAdapter = MovieRecyclerAdapter { navigateToDetailFragment(it) }
        binding.rcMovieTopRated.layoutManager = layoutManagerGridTopRated
        binding.rcMovieTopRated.adapter = movieTopRatedAdapter

        binding.rcMovieTopRated.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(1)) {
                    if (!viewModel.currentState.loading && !viewModel.currentState.loadMore.loadMoreTopRated) {
                        viewModel.loadTopRatedMovies(viewModel.currentState.apiPage.apiPageTopRated + 1)
                    }
                }
            }
        })
    }

    private fun switchLayoutItem(viewType: Int) {
        val firstVisibleItemPosition =
            (binding.rcMovieTopRated.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (viewType == MovieConstant.VIEW_TYPE_GRID) {
            binding.rcMovieTopRated.layoutManager = layoutManagerGridTopRated
        } else {
            binding.rcMovieTopRated.layoutManager = layoutManagerMenuTopRated
        }
        binding.rcMovieTopRated.scrollToPosition(firstVisibleItemPosition)
    }

    private fun observeData(lifecycleOwner: LifecycleOwner) {
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.viewType },
            observer = { viewType ->
                movieTopRatedAdapter.setViewType(viewType)
                switchLayoutItem(viewType)
            }
        )
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.moviesTopRated },
            observer = { data ->
                movieTopRatedAdapter.submitList(data)
            }
        )
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.loadMore.loadMoreTopRated },
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
        fun newInstance() = TopRatedFragment()
    }
}