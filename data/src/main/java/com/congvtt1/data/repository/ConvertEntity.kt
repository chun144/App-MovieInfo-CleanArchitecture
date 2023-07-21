package com.congvtt1.data.repository

import com.congvtt1.data.service.movie.entity.*
import com.congvtt1.domain.model.Actor
import com.congvtt1.domain.model.Genre
import com.congvtt1.domain.model.MovieDetail

fun Movie.toDomainEntity() =
    com.congvtt1.domain.model.Movie(
        adult = adult,
        backdropPath = "https://image.tmdb.org/t/p/w300${backdropPath ?: posterPath}",
        genreIDS = genreIDS ?: emptyList(),
        id = id,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = "https://image.tmdb.org/t/p/w300${posterPath ?: backdropPath}",
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount
    )

fun MovieDetailResult.toDomainEntity(): MovieDetail {
    return MovieDetail(
        title = title ?: "",
        content = overview ?: "",
        time = "${releaseDate ?: ""}   |   ${runtime / 60}h  ${runtime % 60}m",
        language = spokenLanguages.getOrNull(0)?.englishName ?: "",
        rating = voteAverage,
        category = genres.map { it.name }.joinToString ( " | " ),
        cover = "https://image.tmdb.org/t/p/w300${posterPath}",
    )
}

fun ActorsResult.toDomainEntity(): List<Actor> {
    val listActors = mutableListOf<Actor>()
    cast.forEach {
        if (!it.profilePath.isNullOrEmpty() && it.profilePath.endsWith(".jpg")) {
            listActors.add(Actor(it.name, "https://image.tmdb.org/t/p/w185${it.profilePath}"))
        }
    }
    listActors.distinctBy { it.name }
    return listActors
}

fun GenresResult.toDomainEntity(): List<Genre> {
    val listGenres = mutableListOf<Genre>()
    genres.forEach {
        if (!it.name.isNullOrEmpty()) {
            listGenres.add(Genre(it.id, it.name))
        }
    }
    return listGenres
}
