package com.congvtt1.smartmovie.movie.fragment.genres

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.congvtt1.domain.model.Movie
import com.congvtt1.smartmovie.base.viewmodel.ViewModelFactory
import com.congvtt1.smartmovie.databinding.FragmentMovieGenreBinding
import com.congvtt1.smartmovie.movie.adapter.MovieRecyclerAdapter
import com.congvtt1.smartmovie.movie.common.MovieConstant
import com.congvtt1.smartmovie.movie.viewmodel.GenreViewModel

class MovieGenreFragment : Fragment() {
    private lateinit var binding: FragmentMovieGenreBinding
    private val args: MovieGenreFragmentArgs by navArgs()
    private val viewModel: GenreViewModel by viewModels { ViewModelFactory() }
    private lateinit var movieAdapter: MovieRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMovieGenreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setOnClick()
        observeData(viewLifecycleOwner)
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadMoviesGenre(args.genreId)
    }

    private fun initView() {
        binding.tvGenre.text = args.name
        binding.rcMoviesGenre.layoutManager = LinearLayoutManager(requireContext())
        movieAdapter = MovieRecyclerAdapter { navigateToDetailFragment(it) }
        movieAdapter.setViewType(MovieConstant.VIEW_TYPE_MENU)
        binding.rcMoviesGenre.adapter = movieAdapter
    }

    private fun setOnClick() {
        binding.ivChevronBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
    }

    private fun observeData(lifecycleOwner: LifecycleOwner) {
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.error },
            observer = { error ->
                if (error != null) {
                    Toast.makeText(context, "${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.movies },
            observer = { data ->
                movieAdapter.submitList(data)
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
    }

    private fun navigateToDetailFragment(movie: Movie) {
        val direction = MovieGenreFragmentDirections.actionGenreFragmentToDetailFragment(movie.id)
        findNavController().navigate(direction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetError()
    }

    companion object {
        fun newInstance() = MovieGenreFragment()
    }
}