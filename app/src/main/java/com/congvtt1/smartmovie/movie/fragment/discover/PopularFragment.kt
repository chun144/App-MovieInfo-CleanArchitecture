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
import com.congvtt1.smartmovie.databinding.FragmentPopularBinding
import com.congvtt1.smartmovie.movie.adapter.MovieRecyclerAdapter
import com.congvtt1.smartmovie.movie.common.MovieConstant
import com.congvtt1.smartmovie.movie.viewmodel.MoviesViewModel

class PopularFragment : Fragment() {
    private lateinit var binding: FragmentPopularBinding
    private val viewModel: MoviesViewModel by lazy {
        ViewModelProvider(requireParentFragment(), ViewModelFactory())[MoviesViewModel::class.java]
    }
    private lateinit var layoutManagerGridPopular: GridLayoutManager
    private lateinit var layoutManagerMenuPopular: LinearLayoutManager
    private lateinit var moviePopularAdapter: MovieRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPopularBinding.inflate(inflater, container, false)
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
            viewModel.resetApiPagePopular()
            viewModel.loadPopularMovies(1)
        }
    }

    private fun initRecyclerView() {
        layoutManagerGridPopular = GridLayoutManager(requireContext(), 2)
        layoutManagerMenuPopular = LinearLayoutManager(requireContext())
        moviePopularAdapter = MovieRecyclerAdapter { navigateToDetailFragment(it) }
        binding.rcMoviePopular.layoutManager = layoutManagerGridPopular
        binding.rcMoviePopular.adapter = moviePopularAdapter

        binding.rcMoviePopular.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(1)) {
                    if (!viewModel.currentState.loading && !viewModel.currentState.loadMore.loadMorePopular) {
                        viewModel.loadPopularMovies(viewModel.currentState.apiPage.apiPagePopular + 1)
                    }
                }
            }
        })
    }

    private fun switchLayoutItem(viewType: Int) {
        val firstVisibleItemPosition =
            (binding.rcMoviePopular.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (viewType == MovieConstant.VIEW_TYPE_GRID) {
            binding.rcMoviePopular.layoutManager = layoutManagerGridPopular
        } else {
            binding.rcMoviePopular.layoutManager = layoutManagerMenuPopular
        }
        binding.rcMoviePopular.scrollToPosition(firstVisibleItemPosition)
    }

    private fun observeData(lifecycleOwner: LifecycleOwner) {
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.viewType },
            observer = { viewType ->
                moviePopularAdapter.setViewType(viewType)
                switchLayoutItem(viewType)
            }
        )
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.moviesPopular },
            observer = { data ->
                moviePopularAdapter.submitList(data)
            }
        )
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.loadMore.loadMorePopular },
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
        fun newInstance() = PopularFragment()
    }
}