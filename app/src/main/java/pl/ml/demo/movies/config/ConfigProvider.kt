package pl.ml.demo.movies.config

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import pl.ml.demo.movies.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigProvider @Inject constructor(
    @ApplicationContext private val application: Context
) {

    fun getMoviesApiBaseUrl(): String =
        application.getString(R.string.movies_api_base_url)

    fun getImageBaseUrl(): String =
        application.getString(R.string.images_base_url)

}