package com.congvtt1.smartmovie.movie.viewmodel

import androidx.lifecycle.viewModelScope
import com.congvtt1.domain.Result
import com.congvtt1.domain.interactor.GetGenresUseCase
import com.congvtt1.domain.interactor.GetMovieGenreUseCase
import com.congvtt1.smartmovie.base.viewmodel.BaseViewModel
import com.congvtt1.smartmovie.movie.viewstate.GenreViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GenreViewModel(
    private val getGenresUseCase: GetGenresUseCase,
    private val getMovieGenreUseCase: GetMovieGenreUseCase,
) : BaseViewModel<GenreViewState>() {
    private var job: Job? = null

    fun loadGenres() {
        job?.cancel()
        dispatchState(state = store.state.copy(loading = true))
        job = viewModelScope.launch(handlerException) {
            val result = getGenresUseCase.execute()
            dispatchState(state = store.state.copy(loading = false))
            if (result is Result.Success) {
                val genres = result.data
                dispatchState(state = currentState.copy(genres = genres))
            } else if (result is Result.Error) {
                val error = result.exception
                dispatchState(state = currentState.copy(error = error))
            }
        }
    }

    fun loadMoviesGenre(genreId: Long) {
        job?.cancel()
        dispatchState(state = store.state.copy(loading = true))
        job = viewModelScope.launch(handlerException) {
            val result = getMovieGenreUseCase.execute(genreId)
            dispatchState(state = store.state.copy(loading = false))
            if (result is Result.Success) {
                val movies = result.data
                dispatchState(state = currentState.copy(movies = movies))
            } else if (result is Result.Error) {
                val error = result.exception
                dispatchState(state = currentState.copy(error = error))
            }
        }
    }

    fun resetError() {
        if (currentState.error != null) {
            dispatchState(state = currentState.copy(error = null))
        }
    }

    override fun initState(): GenreViewState {
        return GenreViewState(
            error = null,
            loading = false,
            genres = emptyList(),
            movies = emptyList()
        )
    }
}