package com.congvtt1.smartmovie.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.congvtt1.data.repository.MovieRepositoryImpl
import com.congvtt1.data.service.movie.MovieInfoService
import com.congvtt1.domain.interactor.*
import com.congvtt1.smartmovie.movie.viewmodel.GenreViewModel
import com.congvtt1.smartmovie.movie.viewmodel.MovieDetailViewModel
import com.congvtt1.smartmovie.movie.viewmodel.MovieSearchViewModel
import com.congvtt1.smartmovie.movie.viewmodel.MoviesViewModel
import kotlinx.coroutines.Dispatchers

class ViewModelFactory :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
     if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            val serviceData = MovieInfoService.create()
            val repository = MovieRepositoryImpl(serviceData, Dispatchers.IO)
            val nowPlayingUseCase = GetNowPlayingUseCase(repository)
            val popularUseCase = GetMoviePopularUseCase(repository)
            val topRatedUseCase = GetTopRatedUseCase(repository)
            val upComingUseCase = GetUpComingUseCase(repository)
            val showMovieListUseCase = ShowMovieListUseCase(
                getMoviePopularUseCase = popularUseCase,
                getNowPlayingUseCase = nowPlayingUseCase,
                getTopRatedUseCase = topRatedUseCase,
                getUpComingUseCase = upComingUseCase
            )
            return MoviesViewModel(
                showMovieListUseCase,
                popularUseCase,
                topRatedUseCase,
                upComingUseCase,
                nowPlayingUseCase
            ) as T
        } else if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
            val serviceData = MovieInfoService.create()
            val repository = MovieRepositoryImpl(serviceData, Dispatchers.IO)
            val getMovieDetailUseCase = GetMovieDetailUseCase(repository)
            val getActorsUseCase = GetActorsUseCase(repository)
            val getSimilarMoviesUseCase = GetSimilarMoviesUseCase(repository)
            val getGenresUseCase = GetGenresUseCase(repository)
            val showMovieDetailUseCase = ShowMovieDetailUseCase(
                getMovieDetailUseCase = getMovieDetailUseCase,
                getActorsUseCase = getActorsUseCase,
                getSimilarMoviesUseCase = getSimilarMoviesUseCase,
                getGenresUseCase = getGenresUseCase
            )
            return MovieDetailViewModel(
                showMovieDetailUseCase
            ) as T
        } else if (modelClass.isAssignableFrom(MovieSearchViewModel::class.java)) {
         val serviceData = MovieInfoService.create()
         val repository = MovieRepositoryImpl(serviceData, Dispatchers.IO)
         val getMovieSearchUseCase = GetMovieSearchUseCase(repository)
         val getGenresUseCase = GetGenresUseCase(repository)
         val showMovieSearchUseCase = ShowMovieSearchUseCase(
             getMovieSearchUseCase = getMovieSearchUseCase,
             getGenresUseCase = getGenresUseCase
         )
         return MovieSearchViewModel(
             showMovieSearchUseCase
         ) as T
     } else if (modelClass.isAssignableFrom(GenreViewModel::class.java)) {
         val serviceData = MovieInfoService.create()
         val repository = MovieRepositoryImpl(serviceData, Dispatchers.IO)
         val getGenresUseCase = GetGenresUseCase(repository)
         val getMovieGenreUseCase = GetMovieGenreUseCase(repository)
         return GenreViewModel(
             getGenresUseCase,
             getMovieGenreUseCase
         ) as T
     }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}