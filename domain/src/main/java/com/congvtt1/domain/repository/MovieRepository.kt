package com.congvtt1.domain.repository

import com.congvtt1.domain.Result
import com.congvtt1.domain.model.Actor
import com.congvtt1.domain.model.Genre
import com.congvtt1.domain.model.Movie
import com.congvtt1.domain.model.MovieDetail

interface MovieRepository {
    suspend fun getMoviePopular(page: Int): Result<List<Movie>>
    suspend fun getMovieTopRated(page: Int): Result<List<Movie>>
    suspend fun getMovieUpComing(page: Int): Result<List<Movie>>
    suspend fun getMovieNowPlaying(page: Int): Result<List<Movie>>
    suspend fun getMovieDetailByMovieID(movieId: Long): Result<MovieDetail>
    suspend fun getActors(movieId: Long): Result<List<Actor>>
    suspend fun getSimilarMovies(movieId: Long): Result<List<Movie>>
    suspend fun getGenres(): Result<List<Genre>>
    suspend fun getMoviesSearch(title: String): Result<List<Movie>>
    suspend fun getMoviesGenre(genreId: Long): Result<List<Movie>>
}