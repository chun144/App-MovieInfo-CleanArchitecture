package com.congvtt1.domain.interactor

import com.congvtt1.domain.Result
import com.congvtt1.domain.SingleUseCaseWithParameter
import com.congvtt1.domain.model.Movie
import com.congvtt1.domain.repository.MovieRepository


class GetNowPlayingUseCase(private val movieRepository: MovieRepository) :
    SingleUseCaseWithParameter<Int, Result<List<Movie>>> {
    override suspend fun execute(page: Int): Result<List<Movie>> {
        return movieRepository.getMovieNowPlaying(page)
    }
}