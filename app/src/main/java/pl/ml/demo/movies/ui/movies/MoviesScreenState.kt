package pl.ml.demo.movies.ui.movies

import pl.ml.demo.movies.domain.model.MovieItem

sealed class MoviesScreenState {
    data object Loading : MoviesScreenState()
    data class Error(val errorMessage: String) : MoviesScreenState()
    data class Content(val movies: List<MovieItem>) : MoviesScreenState()
}