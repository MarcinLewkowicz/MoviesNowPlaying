package pl.ml.demo.movies.network

import android.content.Context
import coil.ImageLoader
import coil.util.DebugLogger
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.ml.demo.movies.BuildConfig
import pl.ml.demo.movies.R
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(interceptor)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun imageLoader(
        okHttpCallFactory: Call.Factory,
        @ApplicationContext application: Context,
    ): ImageLoader {
        val builder = ImageLoader.Builder(application)
            .callFactory(okHttpCallFactory)
        if (BuildConfig.DEBUG) {
            builder.logger(DebugLogger())
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun moviesApiService(
        networkJson: Json,
        okHttpCallFactory: Call.Factory,
        @ApplicationContext application: Context,
    ) : MoviesApi {
        return Retrofit.Builder()
            .baseUrl(application.getString(R.string.movies_api_base_url))
            .callFactory(okHttpCallFactory)
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .build()
            .create(MoviesApi::class.java)
    }
}
