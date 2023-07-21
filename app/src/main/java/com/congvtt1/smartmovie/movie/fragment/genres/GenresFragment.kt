package com.congvtt1.smartmovie.movie.fragment.genres

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
import com.congvtt1.domain.model.Genre
import com.congvtt1.smartmovie.base.viewmodel.ViewModelFactory
import com.congvtt1.smartmovie.databinding.FragmentGenresBinding
import com.congvtt1.smartmovie.movie.adapter.GenreAdapter
import com.congvtt1.smartmovie.movie.viewmodel.GenreViewModel

class GenresFragment : Fragment() {
    private lateinit var binding: FragmentGenresBinding
    private val viewModel: GenreViewModel by viewModels { ViewModelFactory() }
    private lateinit var genreAdapter: GenreAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGenresBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadGenres()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeData(viewLifecycleOwner)
    }

    private fun initRecyclerView() {
        binding.rcGenres.layoutManager = LinearLayoutManager(requireContext())
        genreAdapter = GenreAdapter { navigateToGenreFragment(it) }
        binding.rcGenres.adapter = genreAdapter
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
            selector = { state -> state.genres },
            observer = { data ->
                genreAdapter.submitList(data)
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

    private fun navigateToGenreFragment(genre: Genre) {
        val direction = GenresFragmentDirections.actionGenresFragmentToGenreFragment(genre.id, genre.name)
        findNavController().navigate(direction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetError()
    }

    companion object {
        fun newInstance() = GenresFragment()
    }
}