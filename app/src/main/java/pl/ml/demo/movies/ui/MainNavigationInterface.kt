package pl.ml.demo.movies.ui

import pl.ml.demo.movies.data.model.Movie

interface MainNavigationInterface {
    fun navigateBack()
    fun navigateToMovieDetails(movie: Movie)
}