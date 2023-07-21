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
import com.congvtt1.domain.model.Movie
import com.congvtt1.smartmovie.base.viewmodel.ViewModelFactory
import com.congvtt1.smartmovie.databinding.FragmentMoviesBinding
import com.congvtt1.smartmovie.movie.adapter.MovieRecyclerAdapter
import com.congvtt1.smartmovie.movie.common.MovieConstant
import com.congvtt1.smartmovie.movie.viewmodel.MoviesViewModel

class MoviesFragment : Fragment() {
    private lateinit var binding: FragmentMoviesBinding
    private val viewModel: MoviesViewModel by lazy {
        ViewModelProvider(requireParentFragment(), ViewModelFactory())[MoviesViewModel::class.java]
    }
    private lateinit var moviePopularAdapter: MovieRecyclerAdapter
    private lateinit var movieTopRatedAdapter: MovieRecyclerAdapter
    private lateinit var movieUpComingAdapter: MovieRecyclerAdapter
    private lateinit var movieNowPlayingAdapter: MovieRecyclerAdapter
    private lateinit var layoutManagerGridPopular: GridLayoutManager
    private lateinit var layoutManagerMenuPopular: LinearLayoutManager
    private lateinit var layoutManagerGridTopRated: GridLayoutManager
    private lateinit var layoutManagerMenuTopRated: LinearLayoutManager
    private lateinit var layoutManagerGridUpComing: GridLayoutManager
    private lateinit var layoutManagerMenuUpComing: LinearLayoutManager
    private lateinit var layoutManagerGridNowPlaying: GridLayoutManager
    private lateinit var layoutManagerMenuNowPlaying: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnRefresh()
        initRecyclerView()
        setOnCLickSeeAll()
        observeData(viewLifecycleOwner)
    }

    private fun setOnRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.resetApiPage()
            viewModel.loadMovies()
        }
    }

    private fun initRecyclerView() {
        layoutManagerGridPopular = GridLayoutManager(requireContext(), 2)
        layoutManagerMenuPopular = LinearLayoutManager(requireContext())
        layoutManagerGridTopRated = GridLayoutManager(requireContext(), 2)
        layoutManagerMenuTopRated = LinearLayoutManager(requireContext())
        layoutManagerGridUpComing = GridLayoutManager(requireContext(), 2)
        layoutManagerMenuUpComing = LinearLayoutManager(requireContext())
        layoutManagerGridNowPlaying = GridLayoutManager(requireContext(), 2)
        layoutManagerMenuNowPlaying = LinearLayoutManager(requireContext())
        moviePopularAdapter = MovieRecyclerAdapter { navigateToDetailFragment(it) }
        movieTopRatedAdapter = MovieRecyclerAdapter { navigateToDetailFragment(it) }
        movieUpComingAdapter = MovieRecyclerAdapter { navigateToDetailFragment(it) }
        movieNowPlayingAdapter = MovieRecyclerAdapter { navigateToDetailFragment(it) }
        binding.rcMoviePopular.layoutManager = layoutManagerGridPopular
        binding.rcMoviePopular.adapter = moviePopularAdapter
        binding.rcMovieTopRated.layoutManager = layoutManagerGridTopRated
        binding.rcMovieTopRated.adapter = movieTopRatedAdapter
        binding.rcMovieUpComing.layoutManager = layoutManagerGridUpComing
        binding.rcMovieUpComing.adapter = movieUpComingAdapter
        binding.rcMovieNowPlaying.layoutManager = layoutManagerGridNowPlaying
        binding.rcMovieNowPlaying.adapter = movieNowPlayingAdapter
    }

    private fun switchLayoutItem(viewType: Int) {
        if (viewType == MovieConstant.VIEW_TYPE_GRID) {
            binding.rcMoviePopular.layoutManager = layoutManagerGridPopular
            binding.rcMovieTopRated.layoutManager = layoutManagerGridTopRated
            binding.rcMovieUpComing.layoutManager = layoutManagerGridUpComing
            binding.rcMovieNowPlaying.layoutManager = layoutManagerGridNowPlaying
        } else {
            binding.rcMoviePopular.layoutManager = layoutManagerMenuPopular
            binding.rcMovieTopRated.layoutManager = layoutManagerMenuTopRated
            binding.rcMovieUpComing.layoutManager = layoutManagerMenuUpComing
            binding.rcMovieNowPlaying.layoutManager = layoutManagerMenuNowPlaying
        }
    }

    private fun observeData(lifecycleOwner: LifecycleOwner) {
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.viewType },
            observer = { viewType ->
                moviePopularAdapter.setViewType(viewType)
                movieTopRatedAdapter.setViewType(viewType)
                movieUpComingAdapter.setViewType(viewType)
                movieNowPlayingAdapter.setViewType(viewType)
                switchLayoutItem(viewType)
            }
        )
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.moviesPopular },
            observer = { data ->
                if (viewModel.currentState.apiPage.apiPagePopular == 1) {
                    if (data.isNotEmpty()) {
                        binding.tvPopular.visibility = View.VISIBLE
                        binding.layoutSeeAllPopular.visibility = View.VISIBLE
                        binding.rcMoviePopular.visibility = View.VISIBLE
                        moviePopularAdapter.submitList(data.take(4))
                    } else {
                        binding.tvPopular.visibility = View.GONE
                        binding.layoutSeeAllPopular.visibility = View.GONE
                        binding.rcMoviePopular.visibility = View.GONE
                    }
                }
            }
        )
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.moviesTopRated },
            observer = { data ->
                if (viewModel.currentState.apiPage.apiPageTopRated == 1) {
                    if (data.isNotEmpty()) {
                        binding.tvTopRated.visibility = View.VISIBLE
                        binding.layoutSeeAllTopRated.visibility = View.VISIBLE
                        binding.rcMovieTopRated.visibility = View.VISIBLE
                        movieTopRatedAdapter.submitList(data.take(4))
                    } else {
                        binding.tvTopRated.visibility = View.GONE
                        binding.layoutSeeAllTopRated.visibility = View.GONE
                        binding.rcMovieTopRated.visibility = View.GONE
                    }
                }
            }
        )
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.moviesUpComing },
            observer = { data ->
                if (viewModel.currentState.apiPage.apiPageUpComing == 1) {
                    if (data.isNotEmpty()) {
                        binding.tvUpComing.visibility = View.VISIBLE
                        binding.layoutSeeAllUpComing.visibility = View.VISIBLE
                        binding.rcMovieUpComing.visibility = View.VISIBLE
                        movieUpComingAdapter.submitList(data.take(4))
                    } else {
                        binding.tvUpComing.visibility = View.GONE
                        binding.layoutSeeAllUpComing.visibility = View.GONE
                        binding.rcMovieUpComing.visibility = View.GONE
                    }
                }
            }
        )
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.moviesNowPlaying },
            observer = { data ->
                if (viewModel.currentState.apiPage.apiPageNowPlaying == 1) {
                    if (data.isNotEmpty()) {
                        binding.tvNowPlaying.visibility = View.VISIBLE
                        binding.layoutSeeAllNowPlaying.visibility = View.VISIBLE
                        binding.rcMovieNowPlaying.visibility = View.VISIBLE
                        movieNowPlayingAdapter.submitList(data.take(4))
                    } else {
                        binding.tvNowPlaying.visibility = View.GONE
                        binding.layoutSeeAllNowPlaying.visibility = View.GONE
                        binding.rcMovieNowPlaying.visibility = View.GONE
                    }
                }
            }
        )
    }

    private fun setOnCLickSeeAll() {
        binding.layoutSeeAllPopular.setOnClickListener {
            viewModel.setTabTitle("Popular")
        }
        binding.layoutSeeAllTopRated.setOnClickListener {
            viewModel.setTabTitle("Top Rated")
        }
        binding.layoutSeeAllUpComing.setOnClickListener {
            viewModel.setTabTitle("Up Coming")
        }
        binding.layoutSeeAllNowPlaying.setOnClickListener {
            viewModel.setTabTitle("Now Playing")
        }
    }

    private fun navigateToDetailFragment(movie: Movie) {
        val direction = HomeFragmentDirections.actionHomeFragmentToDetailFragment(movie.id)
        findNavController().navigate(direction)
    }

    companion object {
        fun newInstance() = MoviesFragment()
    }
}