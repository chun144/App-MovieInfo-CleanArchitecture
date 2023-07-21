package com.congvtt1.data.service.movie

import com.congvtt1.data.service.movie.entity.MovieDetailResult
import com.congvtt1.data.service.movie.entity.MovieResult
import com.congvtt1.data.BuildConfig
import com.congvtt1.data.service.base.Service
import com.congvtt1.data.service.movie.entity.ActorsResult
import com.congvtt1.data.service.movie.entity.GenresResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieInfoService {

    @GET("/3/movie/now_playing")
    suspend fun getNowPlaying(
        @Query("page") page: Int = 1,
        @Query("api_key") clientId: String = BuildConfig.MOVIE_DB_ACCESS_KEY
    ): MovieResult

    @GET("/3/movie/popular")
    suspend fun getPopular(
        @Query("page") page: Int = 1,
        @Query("api_key") clientId: String = BuildConfig.MOVIE_DB_ACCESS_KEY
    ): MovieResult

    @GET("/3/movie/top_rated")
    suspend fun getTopRated(
        @Query("page") page: Int = 1,
        @Query("api_key") clientId: String = BuildConfig.MOVIE_DB_ACCESS_KEY
    ): MovieResult

    @GET("/3/movie/upcoming")
    suspend fun getUpComing(
        @Query("page") page: Int = 1,
        @Query("api_key") clientId: String = BuildConfig.MOVIE_DB_ACCESS_KEY
    ): MovieResult

    @GET("/3/movie/{movieID}")
    suspend fun getMovieDetail(
        @Path("movieID") movieID: Long,
        @Query("api_key") clientId: String = BuildConfig.MOVIE_DB_ACCESS_KEY
    ): MovieDetailResult

    @GET("/3/movie/{movieID}/credits")
    suspend fun getActors(
        @Path("movieID") movieID: Long,
        @Query("api_key") clientId: String = BuildConfig.MOVIE_DB_ACCESS_KEY
    ): ActorsResult

    @GET("/3/movie/{movieID}/similar")
    suspend fun getSimilarMovies(
        @Path("movieID") movieID: Long,
        @Query("api_key") clientId: String = BuildConfig.MOVIE_DB_ACCESS_KEY
    ): MovieResult

    @GET("/3/genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") clientId: String = BuildConfig.MOVIE_DB_ACCESS_KEY
    ): GenresResult

    @GET("/3/search/movie")
    suspend fun getMoviesSearch(
        @Query("query") query: String = "",
        @Query("api_key") clientId: String = BuildConfig.MOVIE_DB_ACCESS_KEY
    ): MovieResult

    @GET("/3/discover/movie")
    suspend fun getMoviesGenre(
        @Query("with_genres") genreId: Long = 0,
        @Query("api_key") clientId: String = BuildConfig.MOVIE_DB_ACCESS_KEY
    ): MovieResult

    companion object {
        private const val BASE_URL = MovieDBServer.URL_MOVIE_DB
        fun create(): MovieInfoService {
            return Service<MovieInfoService>(BASE_URL).create(
                MovieInfoService::class.java
            )
        }
    }

}