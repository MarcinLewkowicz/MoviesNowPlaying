package pl.ml.demo.movies.network

import kotlinx.serialization.Serializable
import pl.ml.demo.movies.data.model.Movie

@Serializable
data class MoviesListResponse(
    val results: List<Movie>
)