package com.congvtt1.smartmovie.movie.viewstate

import com.congvtt1.domain.model.Genre
import com.congvtt1.domain.model.Movie

data class GenreViewState(
    val error: Throwable?,
    val loading: Boolean,
    val genres: List<Genre>,
    val movies: List<Movie>
)