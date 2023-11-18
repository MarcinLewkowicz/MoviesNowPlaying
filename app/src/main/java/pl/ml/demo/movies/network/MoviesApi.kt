package pl.ml.demo.movies.network

import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    @GET("movie/now_playing")
    suspend fun getMoviesNowPlaying() : MoviesListResponse

    @GET("search/movie")
    suspend fun searchMovie(@Query("query") query: String) : MoviesListResponse

}