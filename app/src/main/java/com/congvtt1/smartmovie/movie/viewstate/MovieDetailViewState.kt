package com.congvtt1.smartmovie.movie.viewstate

import com.congvtt1.domain.model.MovieDetail

data class MovieDetailViewState(
    val error: Throwable?,
    val loading: Boolean,
    val movie: MovieDetail,
)

