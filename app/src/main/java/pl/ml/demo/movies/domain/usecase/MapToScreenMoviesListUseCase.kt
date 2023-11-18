package pl.ml.demo.movies.domain.usecase

import pl.ml.demo.movies.config.ConfigProvider
import pl.ml.demo.movies.data.model.Movie
import pl.ml.demo.movies.data.repository.FavoritesRepository
import pl.ml.demo.movies.domain.model.MovieItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapToScreenMoviesListUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val configProvider: ConfigProvider,
) {

    operator fun invoke(movies: List<Movie>): List<MovieItem> {
        return movies.map {
            MovieItem(
                id = it.id,
                title = it.title,
                overview = it.overview,
                backdropUrl = configProvider.getImageBaseUrl() + it.backdropPath,
                posterUrl = configProvider.getImageBaseUrl() + it.posterPath,
                releaseDate = it.releaseDate,
                voteAverage = String.format("%.1f", it.voteAverage),
                isFavorite = favoritesRepository.isFavorite(it.id)
            )
        }
    }

}