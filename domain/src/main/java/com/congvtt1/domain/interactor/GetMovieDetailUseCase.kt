package com.congvtt1.domain.interactor

import com.congvtt1.domain.Result
import com.congvtt1.domain.SingleUseCaseWithParameter
import com.congvtt1.domain.model.MovieDetail
import com.congvtt1.domain.repository.MovieRepository

class GetMovieDetailUseCase(private val movieRepository: MovieRepository) :
    SingleUseCaseWithParameter<Long, Result<MovieDetail>> {
    override suspend fun execute(movieId: Long): Result<MovieDetail> {
        return movieRepository.getMovieDetailByMovieID(movieId)
    }
}