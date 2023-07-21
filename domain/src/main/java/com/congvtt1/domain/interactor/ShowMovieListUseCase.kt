package com.congvtt1.domain.interactor

import com.congvtt1.domain.Result
import com.congvtt1.domain.SingleUseCase
import com.congvtt1.domain.model.Movie
import com.congvtt1.domain.model.MovieList
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ShowMovieListUseCase(
    private val getMoviePopularUseCase: GetMoviePopularUseCase,
    private val getNowPlayingUseCase: GetNowPlayingUseCase,
    private val getTopRatedUseCase: GetTopRatedUseCase,
    private val getUpComingUseCase: GetUpComingUseCase,
) : SingleUseCase<Result<MovieList>> {
    override suspend fun execute(): Result<MovieList> {
        var result: Result<MovieList> =
            Result.Success(MovieList(emptyList(), emptyList(), emptyList(), emptyList()))
        coroutineScope {
            val nowPlayingUseCase = async {
                getNowPlayingUseCase.execute(1)
            }
            val popularUseCase = async {
                getMoviePopularUseCase.execute(1)
            }
            val topRatedUseCase = async {
                getTopRatedUseCase.execute(1)
            }
            val upComingUseCase = async {
                getUpComingUseCase.execute(1)
            }

            val nowPlayingResult = nowPlayingUseCase.await()
            val popularResult = popularUseCase.await()
            val topRatedResult = topRatedUseCase.await()
            val upComingResult = upComingUseCase.await()
            if (nowPlayingResult is Result.Error && popularResult is Result.Error
                && topRatedResult is Result.Error && upComingResult is Result.Error
            ) {
                result = nowPlayingResult
                return@coroutineScope
            }

            var nowPlayingList = emptyList<Movie>()
            var popularList = emptyList<Movie>()
            var topRatedList = emptyList<Movie>()
            var upComingList = emptyList<Movie>()
            if (nowPlayingResult is Result.Success) {
                nowPlayingList = nowPlayingResult.data
            }
            if (popularResult is Result.Success) {
                popularList = popularResult.data
            }
            if (topRatedResult is Result.Success) {
                topRatedList = topRatedResult.data
            }
            if (upComingResult is Result.Success) {
                upComingList = upComingResult.data
            }
            val movieList = MovieList(
                popularList = popularList,
                nowPlayingList = nowPlayingList,
                topRatedList = topRatedList,
                upComingList = upComingList
            )
            result = Result.Success(movieList)
        }
        return result
    }
}