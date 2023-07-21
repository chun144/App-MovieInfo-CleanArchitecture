package com.congvtt1.domain.interactor

import com.congvtt1.domain.Result
import com.congvtt1.domain.SingleUseCaseWithParameter
import com.congvtt1.domain.model.Genre
import com.congvtt1.domain.model.Movie
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ShowMovieSearchUseCase(
    private val getMovieSearchUseCase: GetMovieSearchUseCase,
    private val getGenresUseCase: GetGenresUseCase,
) : SingleUseCaseWithParameter<String, Result<List<Movie>>> {

    override suspend fun execute(title: String): Result<List<Movie>> {
        var result: Result<List<Movie>> = Result.Success(emptyList())
        coroutineScope {
            val movieSearchUseCase = async {
                getMovieSearchUseCase.execute(title)
            }
            val genresUseCase = async {
                getGenresUseCase.execute()
            }
            val movieSearchUseCaseResult = movieSearchUseCase.await()
            val genresUseCaseResult = genresUseCase.await()
            if (movieSearchUseCaseResult is Result.Error) {
                result = movieSearchUseCaseResult
                return@coroutineScope
            }
            var movies = emptyList<Movie>()
            var genres = emptyList<Genre>()
            if (movieSearchUseCaseResult is Result.Success) {
                movies = movieSearchUseCaseResult.data
            }
            if (genresUseCaseResult is Result.Success) {
                genres = genresUseCaseResult.data
            }
            if (genres.isNotEmpty()) {
                movies.forEach { movie ->
                    var category = ""
                    val genresMap = genres.associateBy({ it.id }, { it.name })
                        .filter { it.key in movie.genreIDS }
                    if (genresMap.isNotEmpty()) {
                        category = genresMap.values.joinToString(" | ")
                    }
                    movie.category = category
                }
            }
            result = Result.Success(movies)
        }
        return result
    }
}