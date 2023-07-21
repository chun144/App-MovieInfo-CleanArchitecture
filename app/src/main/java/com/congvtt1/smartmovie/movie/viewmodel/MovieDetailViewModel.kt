package com.congvtt1.smartmovie.movie.viewmodel

import androidx.lifecycle.viewModelScope
import com.congvtt1.domain.interactor.ShowMovieDetailUseCase
import com.congvtt1.domain.model.MovieDetail
import com.congvtt1.smartmovie.base.viewmodel.BaseViewModel
import com.congvtt1.smartmovie.movie.viewstate.MovieDetailViewState
import com.congvtt1.domain.Result

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val showMovieDetailUseCase: ShowMovieDetailUseCase
) : BaseViewModel<MovieDetailViewState>() {
    private var job: Job? = null

    fun loadMovie(movieID: Long) {
        job?.cancel()
        dispatchState(state = store.state.copy(loading = true))
        job = viewModelScope.launch(handlerException) {
            val result = showMovieDetailUseCase.execute(movieID)
            dispatchState(state = store.state.copy(loading = false))
            handleResultUseCase(result)
        }
    }

    private fun handleResultUseCase(
        result: Result<MovieDetail>
    ) {
        if (result is Result.Success) {
            val movieDetail = result.data
            dispatchState(state = currentState.copy(movie = movieDetail))
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

    override fun initState(): MovieDetailViewState {
        return MovieDetailViewState(
            error = null,
            loading = false,
            movie = MovieDetail(),
        )
    }
}