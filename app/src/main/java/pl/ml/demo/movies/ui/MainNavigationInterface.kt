package pl.ml.demo.movies.ui

import pl.ml.demo.movies.domain.model.MovieScreenItem

interface MainNavigationInterface {
    fun navigateBack()
    fun navigateToMovieDetails(movie: MovieScreenItem)
}