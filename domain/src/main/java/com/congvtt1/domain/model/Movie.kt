package com.congvtt1.domain.model

data class Movie (
    val adult: Boolean,
    val backdropPath: String?,
    val genreIDS: List<Long>,
    val id: Long,
    val originalLanguage: String?,
    val originalTitle: String?,
    val overview: String?,
    val popularity: Double,
    val posterPath: String?,
    val releaseDate: String?,
    val title: String?,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Long,
    var category: String = ""
)
