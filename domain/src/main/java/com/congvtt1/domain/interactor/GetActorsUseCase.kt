package com.congvtt1.domain.interactor

import com.congvtt1.domain.Result
import com.congvtt1.domain.SingleUseCaseWithParameter
import com.congvtt1.domain.model.Actor
import com.congvtt1.domain.repository.MovieRepository

class GetActorsUseCase(private val movieRepository: MovieRepository) :
    SingleUseCaseWithParameter<Long, Result<List<Actor>>> {
    override suspend fun execute(movieId: Long): Result<List<Actor>> {
        return movieRepository.getActors(movieId)
    }
}