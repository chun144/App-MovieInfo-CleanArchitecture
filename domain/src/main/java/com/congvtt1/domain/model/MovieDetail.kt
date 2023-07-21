package com.congvtt1.domain.model

data class MovieDetail(
    var title: String = "",
    var content: String = "",
    var time: String = "",
    var language: String = "",
    var rating: Double = 0.0,
    var category: String = "",
    var cover: String = "",
    var actors: MutableList<Actor> = mutableListOf(),
    var similarMovies: MutableList<Movie> = mutableListOf()
)

data class Actor(
    val name: String = "",
    val image: String = ""
)
