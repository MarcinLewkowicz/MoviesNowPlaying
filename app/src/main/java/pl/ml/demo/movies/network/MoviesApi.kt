package pl.ml.demo.movies.network

import pl.ml.demo.movies.data.model.Movie
import retrofit2.http.GET

interface MoviesApi {

    @GET("/movie/now_playing")
    fun getMoviesNowPlaying() : List<Movie>

}