package pl.ml.demo.movies.data.repository

import pl.ml.demo.movies.data.model.Movie
import pl.ml.demo.movies.data.util.Result
import pl.ml.demo.movies.network.MoviesApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
    private val api: MoviesApi
) {

    suspend fun getMoviesNowPlaying(page: Int): Result<List<Movie>> {
        return try {
            val movies = api.getMoviesNowPlaying(page)
            Result.Success(movies.results)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun searchMovies(query: String, page: Int): Result<List<Movie>> {
        return try {
            val movies = api.searchMovie(query, page)
            Result.Success(movies.results)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}