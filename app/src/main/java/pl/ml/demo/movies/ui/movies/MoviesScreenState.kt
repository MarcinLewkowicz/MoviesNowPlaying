package pl.ml.demo.movies.ui.movies

import androidx.annotation.DrawableRes
import pl.ml.demo.movies.domain.model.MovieScreenItem

sealed class MoviesScreenState {

    data object Loading : MoviesScreenState()

    data class Error(
        val errorMessage: String,
        @DrawableRes val iconId: Int,
        val retryButtonVisible: Boolean
    ) : MoviesScreenState()

    data class Content(
        val movies: List<MovieScreenItem>
    ) : MoviesScreenState()

}