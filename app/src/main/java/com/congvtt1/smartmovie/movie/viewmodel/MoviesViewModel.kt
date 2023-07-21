package com.congvtt1.smartmovie.movie.viewmodel

import androidx.lifecycle.viewModelScope
import com.congvtt1.domain.Result
import com.congvtt1.domain.interactor.*
import com.congvtt1.domain.model.Movie
import com.congvtt1.domain.model.MovieList
import com.congvtt1.smartmovie.base.viewmodel.BaseViewModel
import com.congvtt1.smartmovie.movie.common.MovieConstant
import com.congvtt1.smartmovie.movie.viewstate.ApiPage
import com.congvtt1.smartmovie.movie.viewstate.LoadMore
import com.congvtt1.smartmovie.movie.viewstate.MoviesViewState
import com.congvtt1.smartmovie.movie.viewstate.TabMovie
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val showMovieListUseCase: ShowMovieListUseCase,
    private val getMoviePopularUseCase: GetMoviePopularUseCase,
    private val getTopRatedUseCase: GetTopRatedUseCase,
    private val getUpComingUseCase: GetUpComingUseCase,
    private val getNowPlayingUseCase: GetNowPlayingUseCase,
) : BaseViewModel<MoviesViewState>() {
    private var job: Job? = null

    fun loadMovies() {
        job?.cancel()
        dispatchState(state = store.state.copy(loading = true))
        job = viewModelScope.launch(handlerException) {
            val result = showMovieListUseCase.execute()
            dispatchState(state = store.state.copy(loading = false))
            handleResultUseCase(result)
        }
    }

    private fun handleResultUseCase(
        result: Result<MovieList>,
    ) {
        if (result is Result.Success) {
            val movieList = result.data
            val popularList = movieList.popularList
            val nowPlayingList = movieList.nowPlayingList
            val topRatedList = movieList.topRatedList
            val upComingList = movieList.upComingList
            dispatchState(
                state = currentState.copy(
                    tabMovie = currentState.tabMovie.copy(
                        tabPopular = popularList.isNotEmpty(),
                        tabUpComing = upComingList.isNotEmpty(),
                        tabTopRated = topRatedList.isNotEmpty(),
                        tabNowPlaying = nowPlayingList.isNotEmpty()
                    )
                )
            )
            dispatchState(
                state = currentState.copy(
                    apiPage = currentState.apiPage.copy(
                        apiPagePopular = 1,
                        apiPageTopRated = 1,
                        apiPageUpComing = 1,
                        apiPageNowPlaying = 1
                    )
                )
            )
            dispatchState(state = currentState.copy(moviesPopular = popularList))
            dispatchState(state = currentState.copy(moviesTopRated = topRatedList))
            dispatchState(state = currentState.copy(moviesUpComing = upComingList))
            dispatchState(state = currentState.copy(moviesNowPlaying = nowPlayingList))
        } else if (result is Result.Error) {
            val error = result.exception
            dispatchState(state = currentState.copy(error = error))
        }
    }

    fun loadPopularMovies(page: Int) {
        if (page == 1) {
            dispatchState(state = store.state.copy(loading = true))
        } else {
            dispatchState(state = currentState.copy(loadMore = currentState.loadMore.copy(loadMorePopular = true)))
        }
        viewModelScope.launch(handlerException) {
            val result = getMoviePopularUseCase.execute(page)
            if (result is Result.Success) {
                val popularList = mutableListOf<Movie>()
                if (page != 1) {
                    popularList.addAll(currentState.moviesPopular)
                }
                popularList.addAll(result.data)
                dispatchState(state = currentState.copy(apiPage = currentState.apiPage.copy(apiPagePopular = page)))
                dispatchState(state = currentState.copy(moviesPopular = popularList))
            } else if (result is Result.Error) {
                if (page == 1) {
                    dispatchState(state = currentState.copy(apiPage = currentState.apiPage.copy(apiPagePopular = page)))
                    dispatchState(state = currentState.copy(moviesPopular = emptyList()))
                }
            }
            if (page == 1) {
                dispatchState(state = store.state.copy(loading = false))
            } else {
                dispatchState(state = currentState.copy(loadMore = currentState.loadMore.copy(loadMorePopular = false)))
            }
        }
    }

    fun loadTopRatedMovies(page: Int) {
        if (page == 1) {
            dispatchState(state = store.state.copy(loading = true))
        } else {
            dispatchState(state = currentState.copy(loadMore = currentState.loadMore.copy(loadMoreTopRated = true)))
        }
        viewModelScope.launch(handlerException) {
            val result = getTopRatedUseCase.execute(page)
            if (result is Result.Success) {
                val topRatedList = mutableListOf<Movie>()
                if (page != 1) {
                    topRatedList.addAll(currentState.moviesTopRated)
                }
                topRatedList.addAll(result.data)
                dispatchState(state = currentState.copy(apiPage = currentState.apiPage.copy(apiPageTopRated = page)))
                dispatchState(state = currentState.copy(moviesTopRated = topRatedList))
            } else if (result is Result.Error) {
                if (page == 1) {
                    dispatchState(state = currentState.copy(apiPage = currentState.apiPage.copy(apiPageTopRated = page)))
                    dispatchState(state = currentState.copy(moviesTopRated = emptyList()))
                }
            }
            if (page == 1) {
                dispatchState(state = store.state.copy(loading = false))
            } else {
                dispatchState(state = currentState.copy(loadMore = currentState.loadMore.copy(loadMoreTopRated = false)))
            }
        }
    }

    fun loadUpComingMovies(page: Int) {
        if (page == 1) {
            dispatchState(state = store.state.copy(loading = true))
        } else {
            dispatchState(state = currentState.copy(loadMore = currentState.loadMore.copy(loadMoreUpComing = true)))
        }
        viewModelScope.launch(handlerException) {
            val result = getUpComingUseCase.execute(page)
            if (result is Result.Success) {
                val upComingList = mutableListOf<Movie>()
                if (page != 1) {
                    upComingList.addAll(currentState.moviesUpComing)
                }
                upComingList.addAll(result.data)
                dispatchState(state = currentState.copy(apiPage = currentState.apiPage.copy(apiPageUpComing = page)))
                dispatchState(state = currentState.copy(moviesUpComing = upComingList))
            } else if (result is Result.Error) {
                if (page == 1) {
                    dispatchState(state = currentState.copy(apiPage = currentState.apiPage.copy(apiPageUpComing = page)))
                    dispatchState(state = currentState.copy(moviesUpComing = emptyList()))
                }
            }
            if (page == 1) {
                dispatchState(state = store.state.copy(loading = false))
            } else {
                dispatchState(state = currentState.copy(loadMore = currentState.loadMore.copy(loadMoreUpComing = false)))
            }
        }
    }

    fun loadNowPlayingMovies(page: Int) {
        if (page == 1) {
            dispatchState(state = store.state.copy(loading = true))
        } else {
            dispatchState(state = currentState.copy(loadMore = currentState.loadMore.copy(loadMoreNowPlaying = true)))
        }
        viewModelScope.launch(handlerException) {
            val result = getNowPlayingUseCase.execute(page)
            if (result is Result.Success) {
                val nowPlayingList = mutableListOf<Movie>()
                if (page != 1) {
                    nowPlayingList.addAll(currentState.moviesNowPlaying)
                }
                nowPlayingList.addAll(result.data)
                dispatchState(state = currentState.copy(apiPage = currentState.apiPage.copy(apiPageNowPlaying = page)))
                dispatchState(state = currentState.copy(moviesNowPlaying = nowPlayingList))
            } else if (result is Result.Error) {
                if (page == 1) {
                    dispatchState(state = currentState.copy(apiPage = currentState.apiPage.copy(apiPageNowPlaying = page)))
                    dispatchState(state = currentState.copy(moviesNowPlaying = emptyList()))
                }
            }
            if (page == 1) {
                dispatchState(state = store.state.copy(loading = false))
            } else {
                dispatchState(state = currentState.copy(loadMore = currentState.loadMore.copy(loadMoreNowPlaying = false)))
            }
        }
    }

    fun setTabTitle(tabTitle: String) {
        dispatchState(state = currentState.copy(tabTitle = tabTitle))
    }

    fun setViewType(viewType: Int) {
        dispatchState(state = currentState.copy(viewType = viewType))
    }

    fun resetApiPage() {
        dispatchState(
            state = currentState.copy(
                apiPage = currentState.apiPage.copy(
                    apiPagePopular = 0,
                    apiPageTopRated = 0,
                    apiPageUpComing = 0,
                    apiPageNowPlaying = 0
                )
            )
        )
    }

    fun resetError() {
        if (currentState.error != null) {
            dispatchState(state = currentState.copy(error = null))
        }
    }

    fun resetApiPagePopular() {
        dispatchState(state = currentState.copy(apiPage = currentState.apiPage.copy(apiPagePopular = 0)))
    }

    fun resetApiPageTopRated() {
        dispatchState(state = currentState.copy(apiPage = currentState.apiPage.copy(apiPageTopRated = 0)))
    }

    fun resetApiPageUpComing() {
        dispatchState(state = currentState.copy(apiPage = currentState.apiPage.copy(apiPageUpComing = 0)))
    }

    fun resetApiPageNowPlaying() {
        dispatchState(state = currentState.copy(apiPage = currentState.apiPage.copy(apiPageNowPlaying = 0)))
    }

    override fun initState(): MoviesViewState {
        return MoviesViewState(
            error = null,
            loading = false,
            viewType = MovieConstant.VIEW_TYPE_GRID,
            moviesPopular = emptyList(),
            moviesNowPlaying = emptyList(),
            moviesTopRated = emptyList(),
            moviesUpComing = emptyList(),
            tabMovie = TabMovie(),
            tabTitle = "",
            apiPage = ApiPage(),
            loadMore = LoadMore()
        )
    }
}