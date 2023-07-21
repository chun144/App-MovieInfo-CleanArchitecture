package com.congvtt1.domain.interactor

import com.congvtt1.domain.Result
import com.congvtt1.domain.SingleUseCaseWithParameter
import com.congvtt1.domain.model.Genre
import com.congvtt1.domain.model.MovieDetail
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ShowMovieDetailUseCase(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getActorsUseCase: GetActorsUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase,
    private val getGenresUseCase: GetGenresUseCase
) : SingleUseCaseWithParameter<Long, Result<MovieDetail>> {

    override suspend fun execute(movieId: Long): Result<MovieDetail> {
        var result: Result<MovieDetail> = Result.Success(MovieDetail())
        coroutineScope {
            val movieDetailUseCase = async {
                getMovieDetailUseCase.execute(movieId)
            }
            val movieDetailUseCaseResult = movieDetailUseCase.await()
            val actorsUseCase = async {
                getActorsUseCase.execute(movieId)
            }
            val actorsUseCaseResult = actorsUseCase.await()
            val similarMoviesUseCase = async {
                getSimilarMoviesUseCase.execute(movieId)
            }
            val similarMoviesUseCaseResult = similarMoviesUseCase.await()
            val genresUseCase = async {
                getGenresUseCase.execute()
            }
            val genresUseCaseResult = genresUseCase.await()
//            val movieDetailUseCaseResult = movieDetailUseCase.await()
//            val actorsUseCaseResult = actorsUseCase.await()
//            val similarMoviesUseCaseResult = similarMoviesUseCase.await()
//            val genresUseCaseResult = genresUseCase.await()
            if (movieDetailUseCaseResult is Result.Error) {
                result = movieDetailUseCaseResult
                return@coroutineScope
            }
            val returnData = MovieDetail()
            var genres = emptyList<Genre>()
            if (movieDetailUseCaseResult is Result.Success) {
                val movieDetail = movieDetailUseCaseResult.data
                returnData.category = movieDetail.category
                returnData.content = movieDetail.content
                returnData.cover = movieDetail.cover
                returnData.language = movieDetail.language
                returnData.rating = movieDetail.rating
                returnData.time = movieDetail.time
                returnData.title = movieDetail.title
            }
            if (actorsUseCaseResult is Result.Success) {
                val actors = actorsUseCaseResult.data
                returnData.actors.addAll(actors)
            }
            if (similarMoviesUseCaseResult is Result.Success) {
                val similarMovies = similarMoviesUseCaseResult.data
                returnData.similarMovies.addAll(similarMovies)
            }
            if (genresUseCaseResult is Result.Success) {
                genres = genresUseCaseResult.data
            }
            if (genres.isNotEmpty()) {
                returnData.similarMovies.forEach { movie ->
                    var category = ""
                    val genresMap = genres.associateBy({ it.id }, { it.name })
                        .filter { it.key in movie.genreIDS }
                    if (genresMap.isNotEmpty()) {
                        category = genresMap.values.joinToString(" | ")
                    }
                    movie.category = category
                }
            }
            result = Result.Success(returnData)
        }
        return result
    }
}