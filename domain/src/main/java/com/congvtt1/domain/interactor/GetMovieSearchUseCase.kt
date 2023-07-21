package com.congvtt1.domain.interactor

import com.congvtt1.domain.Result
import com.congvtt1.domain.SingleUseCaseWithParameter
import com.congvtt1.domain.model.Movie
import com.congvtt1.domain.repository.MovieRepository

class GetMovieSearchUseCase(private val movieRepository: MovieRepository) :
    SingleUseCaseWithParameter<String, Result<List<Movie>>> {
    override suspend fun execute(title: String): Result<List<Movie>> {
        return movieRepository.getMoviesSearch(title)
    }
}