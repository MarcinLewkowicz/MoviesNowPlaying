package pl.ml.demo.movies.network

import retrofit2.http.GET

interface MoviesApi {

    @GET("movie/now_playing")
    suspend fun getMoviesNowPlaying() : MoviesListResponse

}