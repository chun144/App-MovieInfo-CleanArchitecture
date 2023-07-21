package com.congvtt1.smartmovie.movie.fragment.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.congvtt1.domain.model.Movie
import com.congvtt1.smartmovie.base.viewmodel.ViewModelFactory
import com.congvtt1.smartmovie.databinding.FragmentSearchBinding
import com.congvtt1.smartmovie.movie.adapter.MovieListAdapter
import com.congvtt1.smartmovie.movie.viewmodel.MovieSearchViewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: MovieSearchViewModel by viewModels { ViewModelFactory() }
    private lateinit var movieAdapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setOnClick()
        observeData(viewLifecycleOwner)
    }

    private fun initRecyclerView() {
        binding.rcMovieSearch.layoutManager = LinearLayoutManager(requireContext())
        movieAdapter = MovieListAdapter { navigateToDetailFragment(it) }
        binding.rcMovieSearch.adapter = movieAdapter
    }

    private fun setOnClick() {
        binding.ivSearch.setOnClickListener {
            if (binding.edtSearch.text.isNullOrBlank()) {
                if (movieAdapter.itemCount != 0) {
                    viewModel.resetMovieSearch()
                }
            } else {
                viewModel.searchMovies(binding.edtSearch.text.toString())
            }
        }
        binding.tvCancel.setOnClickListener {
            if (!binding.edtSearch.text.isNullOrBlank()) {
                binding.edtSearch.setText("")
            }
            if (movieAdapter.itemCount != 0) {
                viewModel.resetMovieSearch()
            }
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
        val direction = SearchFragmentDirections.actionSearchFragmentToDetailFragment(movie.id)
        findNavController().navigate(direction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetError()
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}