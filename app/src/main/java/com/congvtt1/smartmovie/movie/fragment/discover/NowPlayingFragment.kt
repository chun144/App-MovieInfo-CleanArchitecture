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
import com.congvtt1.smartmovie.databinding.FragmentNowPlayingBinding
import com.congvtt1.smartmovie.movie.adapter.MovieRecyclerAdapter
import com.congvtt1.smartmovie.movie.common.MovieConstant
import com.congvtt1.smartmovie.movie.viewmodel.MoviesViewModel

class NowPlayingFragment : Fragment() {
    private lateinit var binding: FragmentNowPlayingBinding
    private val viewModel: MoviesViewModel by lazy {
        ViewModelProvider(requireParentFragment(), ViewModelFactory())[MoviesViewModel::class.java]
    }
    private lateinit var layoutManagerGridNowPlaying: GridLayoutManager
    private lateinit var layoutManagerMenuNowPlaying: LinearLayoutManager
    private lateinit var movieNowPlayingAdapter: MovieRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNowPlayingBinding.inflate(inflater, container, false)
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
            viewModel.resetApiPageNowPlaying()
            viewModel.loadNowPlayingMovies(1)
        }
    }

    private fun initRecyclerView() {
        layoutManagerGridNowPlaying = GridLayoutManager(requireContext(), 2)
        layoutManagerMenuNowPlaying = LinearLayoutManager(requireContext())
        movieNowPlayingAdapter = MovieRecyclerAdapter { navigateToDetailFragment(it) }
        binding.rcMovieNowPlaying.layoutManager = layoutManagerGridNowPlaying
        binding.rcMovieNowPlaying.adapter = movieNowPlayingAdapter

        binding.rcMovieNowPlaying.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(1)) {
                    if (!viewModel.currentState.loading && !viewModel.currentState.loadMore.loadMoreNowPlaying) {
                        viewModel.loadNowPlayingMovies(viewModel.currentState.apiPage.apiPageNowPlaying + 1)
                    }
                }
            }
        })
    }

    private fun switchLayoutItem(viewType: Int) {
        val firstVisibleItemPosition =
            (binding.rcMovieNowPlaying.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (viewType == MovieConstant.VIEW_TYPE_GRID) {
            binding.rcMovieNowPlaying.layoutManager = layoutManagerGridNowPlaying
        } else {
            binding.rcMovieNowPlaying.layoutManager = layoutManagerMenuNowPlaying
        }
        binding.rcMovieNowPlaying.scrollToPosition(firstVisibleItemPosition)
    }

    private fun observeData(lifecycleOwner: LifecycleOwner) {
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.viewType },
            observer = { viewType ->
                movieNowPlayingAdapter.setViewType(viewType)
                switchLayoutItem(viewType)
            }
        )
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.moviesNowPlaying },
            observer = { data ->
                movieNowPlayingAdapter.submitList(data)
            }
        )
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.loadMore.loadMoreNowPlaying },
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
        fun newInstance() = NowPlayingFragment()
    }
}