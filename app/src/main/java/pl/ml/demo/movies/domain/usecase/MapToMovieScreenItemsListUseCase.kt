package pl.ml.demo.movies.domain.usecase

import pl.ml.demo.movies.data.model.Movie
import pl.ml.demo.movies.domain.model.MovieScreenItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapToMovieScreenItemsListUseCase @Inject constructor(
    private val mapToMovieScreenItemUseCase: MapToMovieScreenItemUseCase,
) {

    operator fun invoke(movies: List<Movie>): List<MovieScreenItem> {
        return movies.map {
            mapToMovieScreenItemUseCase(it)
        }
    }

}