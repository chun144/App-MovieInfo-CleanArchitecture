package com.congvtt1.smartmovie.movie.viewmodel

import androidx.lifecycle.viewModelScope
import com.congvtt1.domain.Result
import com.congvtt1.domain.interactor.ShowMovieSearchUseCase
import com.congvtt1.domain.model.Movie
import com.congvtt1.smartmovie.base.viewmodel.BaseViewModel
import com.congvtt1.smartmovie.movie.viewstate.MovieSearchViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieSearchViewModel(
    private val showMovieSearchUseCase: ShowMovieSearchUseCase
) : BaseViewModel<MovieSearchViewState>() {
    private var job: Job? = null

    fun searchMovies(title: String) {
        job?.cancel()
        dispatchState(state = store.state.copy(loading = true))
        job = viewModelScope.launch(handlerException) {
            val result = showMovieSearchUseCase.execute(title)
            dispatchState(state = store.state.copy(loading = false))
            handleResultUseCase(result)
        }
    }

    private fun handleResultUseCase(
        result: Result<List<Movie>>
    ) {
        if (result is Result.Success) {
            val movies = result.data
            dispatchState(state = currentState.copy(movies = movies))
        } else if (result is Result.Error) {
            val error = result.exception
            dispatchState(state = currentState.copy(error = error))
        }
    }

    fun resetError() {
        if (currentState.error != null) {
            dispatchState(state = currentState.copy(error = null))
        }
    }

    fun resetMovieSearch() {
        dispatchState(state = currentState.copy(movies = emptyList()))
    }

    override fun initState(): MovieSearchViewState {
        return MovieSearchViewState(
            error = null,
            loading = false,
            movies = emptyList(),
        )
    }
}