package com.congvtt1.domain.interactor

import com.congvtt1.domain.Result
import com.congvtt1.domain.SingleUseCase
import com.congvtt1.domain.model.Genre
import com.congvtt1.domain.repository.MovieRepository

class GetGenresUseCase(
    private val movieRepository: MovieRepository
) : SingleUseCase<Result<List<Genre>>> {
    override suspend fun execute(): Result<List<Genre>> {
        return movieRepository.getGenres()
    }
}