package com.congvtt1.smartmovie.movie.viewstate

import com.congvtt1.domain.model.Movie

data class MoviesViewState(
    val error: Throwable?,
    val loading: Boolean,
    val viewType: Int,
    val moviesPopular: List<Movie>,
    val moviesNowPlaying: List<Movie>,
    val moviesTopRated: List<Movie>,
    val moviesUpComing: List<Movie>,
    val tabMovie: TabMovie,
    val tabTitle: String,
    val apiPage: ApiPage,
    val loadMore: LoadMore
)

data class TabMovie(
    val tabPopular: Boolean = false,
    val tabNowPlaying: Boolean = false,
    val tabTopRated: Boolean = false,
    val tabUpComing: Boolean = false,
)

data class ApiPage(
    val apiPagePopular: Int = 0,
    val apiPageTopRated: Int = 0,
    val apiPageUpComing: Int = 0,
    val apiPageNowPlaying: Int = 0
)

data class LoadMore(
    val loadMorePopular: Boolean = false,
    val loadMoreNowPlaying: Boolean = false,
    val loadMoreTopRated: Boolean = false,
    val loadMoreUpComing: Boolean = false,
)