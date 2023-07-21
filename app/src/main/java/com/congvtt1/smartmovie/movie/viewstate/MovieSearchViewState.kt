package com.congvtt1.smartmovie.movie.viewstate

import com.congvtt1.domain.model.Movie

data class MovieSearchViewState(
    val error: Throwable?,
    val loading: Boolean,
    val movies: List<Movie>
)