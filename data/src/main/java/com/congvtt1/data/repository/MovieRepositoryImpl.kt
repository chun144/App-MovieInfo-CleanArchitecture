package com.congvtt1.data.repository

import com.congvtt1.data.service.movie.MovieInfoService
import com.congvtt1.domain.Result
import com.congvtt1.domain.model.Actor
import com.congvtt1.domain.model.Genre
import com.congvtt1.domain.model.Movie
import com.congvtt1.domain.model.MovieDetail
import com.congvtt1.domain.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(
    private val dataSource: MovieInfoService,
    private val dispatcher: CoroutineDispatcher,
) : MovieRepository {

    override suspend fun getMovieNowPlaying(page: Int): Result<List<Movie>> =
        withContext(dispatcher) {
            try {
                val dataResponse = dataSource.getNowPlaying(page = page)
                val movies = mutableListOf<Movie>()
                dataResponse.results.forEach {
                    movies.add(it.toDomainEntity())
                }
                Result.Success(movies)
            } catch (e: Exception) {
                Result.Error(e)
            }

        }

    override suspend fun getMoviePopular(page: Int): Result<List<Movie>> = withContext(dispatcher) {
        try {
            val dataResponse = dataSource.getPopular(page = page)
            val movies = mutableListOf<Movie>()
            dataResponse.results.forEach {
                movies.add(it.toDomainEntity())
            }
            Result.Success(movies)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getMovieTopRated(page: Int): Result<List<Movie>> =
        withContext(dispatcher) {
            try {
                val dataResponse = dataSource.getTopRated(page = page)
                val movies = mutableListOf<Movie>()
                dataResponse.results.forEach {
                    movies.add(it.toDomainEntity())
                }
                Result.Success(movies)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getMovieUpComing(page: Int): Result<List<Movie>> =
        withContext(dispatcher) {
            try {
                val dataResponse = dataSource.getUpComing(page = page)
                val movies = mutableListOf<Movie>()
                dataResponse.results.forEach {
                    movies.add(it.toDomainEntity())
                }
                Result.Success(movies)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getMovieDetailByMovieID(movieId: Long): Result<MovieDetail> =
        withContext(dispatcher) {
            try {
                val dataResponse = dataSource.getMovieDetail(movieId)
                val movieDetail = dataResponse.toDomainEntity()
                Result.Success(movieDetail)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getActors(movieId: Long): Result<List<Actor>> = withContext(dispatcher) {
        try {
            val dataResponse = dataSource.getActors(movieId)
            val actors = dataResponse.toDomainEntity()
            Result.Success(actors.toList())
        } catch (e: Exception) {
            Result.Error(e)
        }

    }

    override suspend fun getSimilarMovies(movieId: Long): Result<List<Movie>> =
        withContext(dispatcher) {
            try {
                val dataResponse = dataSource.getSimilarMovies(movieId)
                val movies = mutableListOf<Movie>()
                dataResponse.results.forEach {
                    movies.add(it.toDomainEntity())
                }
                Result.Success(movies)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getGenres(): Result<List<Genre>> = withContext(dispatcher) {
        try {
            val dataResponse = dataSource.getGenres()
            val genres = dataResponse.toDomainEntity()
            Result.Success(genres)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getMoviesSearch(title: String): Result<List<Movie>> =
        withContext(dispatcher) {
            try {
                val dataResponse = dataSource.getMoviesSearch(query = title)
                val movies = mutableListOf<Movie>()
                dataResponse.results.forEach {
                    movies.add(it.toDomainEntity())
                }
                Result.Success(movies)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getMoviesGenre(genreId: Long): Result<List<Movie>> =
        withContext(dispatcher) {
            try {
                val dataResponse = dataSource.getMoviesGenre(genreId = genreId)
                val movies = mutableListOf<Movie>()
                dataResponse.results.forEach {
                    movies.add(it.toDomainEntity())
                }
                Result.Success(movies)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
}




