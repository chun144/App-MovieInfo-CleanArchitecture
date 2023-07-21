package com.congvtt1.domain.model

data class MovieList(
    val popularList: List<Movie>,
    val nowPlayingList: List<Movie>,
    val topRatedList: List<Movie>,
    val upComingList: List<Movie>
)