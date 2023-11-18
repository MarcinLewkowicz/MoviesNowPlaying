package pl.ml.demo.movies.domain.usecase

import pl.ml.demo.movies.data.model.Movie
import pl.ml.demo.movies.data.repository.MoviesRepository
import pl.ml.demo.movies.data.util.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMoviesListUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {

    suspend operator fun invoke(query: String): Result<List<Movie>> {
        return if (query.isEmpty()) {
            moviesRepository.getMoviesNowPlaying()
        } else {
            moviesRepository.searchMovies(query)
        }
    }

}