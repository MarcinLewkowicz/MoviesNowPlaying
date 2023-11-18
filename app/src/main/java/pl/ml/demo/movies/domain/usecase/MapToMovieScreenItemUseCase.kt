package pl.ml.demo.movies.domain.usecase

import pl.ml.demo.movies.config.ConfigProvider
import pl.ml.demo.movies.data.model.Movie
import pl.ml.demo.movies.data.repository.FavoritesRepository
import pl.ml.demo.movies.domain.model.MovieScreenItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapToMovieScreenItemUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val configProvider: ConfigProvider,
) {

    operator fun invoke(movie: Movie): MovieScreenItem {
        return MovieScreenItem(
            id = movie.id,
            title = movie.title,
            overview = movie.overview,
            backdropUrl = movie.backdropPath?.let { configProvider.getImageBaseUrl() + it },
            posterUrl = movie.posterPath?.let { configProvider.getImageBaseUrl() + it},
            releaseDate = movie.releaseDate,
            voteAverage = String.format("%.1f", movie.voteAverage),
            isFavorite = favoritesRepository.isFavorite(movie.id)
        )
    }

}