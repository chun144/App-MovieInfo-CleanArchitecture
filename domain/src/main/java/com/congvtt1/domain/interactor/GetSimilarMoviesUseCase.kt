package com.congvtt1.domain.interactor

import com.congvtt1.domain.Result
import com.congvtt1.domain.SingleUseCaseWithParameter
import com.congvtt1.domain.model.Movie
import com.congvtt1.domain.repository.MovieRepository

class GetSimilarMoviesUseCase(private val movieRepository: MovieRepository) :
    SingleUseCaseWithParameter<Long, Result<List<Movie>>> {
    override suspend fun execute(movieId: Long): Result<List<Movie>> {
        return movieRepository.getSimilarMovies(movieId)
    }
}