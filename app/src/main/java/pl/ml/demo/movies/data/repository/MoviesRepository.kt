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

    suspend fun getMoviesNowPlaying() : Result<List<Movie>> {
        return try {
            val movies = api.getMoviesNowPlaying()
            Result.Success(movies)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}