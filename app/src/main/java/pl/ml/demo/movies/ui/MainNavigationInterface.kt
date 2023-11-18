package pl.ml.demo.movies.ui

import pl.ml.demo.movies.domain.model.MovieItem

interface MainNavigationInterface {
    fun navigateBack()
    fun navigateToMovieDetails(movie: MovieItem)
}