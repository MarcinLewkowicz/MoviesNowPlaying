package pl.ml.demo.movies.ui.movies

import pl.ml.demo.movies.data.model.Movie

sealed class MoviesScreenState {
    data object Loading : MoviesScreenState()
    data class Error(val errorMessage: String) : MoviesScreenState()
    data class Content(val movies: List<Movie>) : MoviesScreenState()
}