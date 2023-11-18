package pl.ml.demo.movies.domain.usecase

import pl.ml.demo.movies.data.repository.FavoritesRepository
import pl.ml.demo.movies.domain.model.MovieScreenItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToggleFavoriteMovieUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
) {

    operator fun invoke(movie: MovieScreenItem) {
        favoritesRepository.toggleFavoriteMovie(movie)
    }

}