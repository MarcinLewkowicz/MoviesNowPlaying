package pl.ml.demo.movies.data.repository

import pl.ml.demo.movies.domain.model.MovieItem
import pl.ml.demo.movies.storage.FavoritesStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository @Inject constructor(
    private val storage: FavoritesStorage
) {

    private val favorites: MutableSet<Int> =
        storage.getFavorites().toMutableSet()

    fun toggleFavoriteMovie(movie: MovieItem) {
        if (favorites.contains(movie.id)) {
            removeFromFavorites(movie.id)
        } else {
            addToFavorites(movie.id)
        }
    }

    private fun addToFavorites(movieId: Int) {
        favorites += movieId
        storage.setFavorites(favorites)
    }

    private fun removeFromFavorites(movieId: Int) {
        favorites -= movieId
        storage.setFavorites(favorites)
    }

    fun isFavorite(id: Int): Boolean =
        favorites.contains(id)

}