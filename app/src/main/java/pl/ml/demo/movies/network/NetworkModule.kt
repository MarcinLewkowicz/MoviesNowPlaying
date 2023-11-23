package pl.ml.demo.movies.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.ml.demo.movies.BuildConfig
import pl.ml.demo.movies.config.ConfigProvider
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
    fun okHttpCallFactory(
        loggingInterceptor: HttpLoggingInterceptor,
        authenticationInterceptor: AuthenticationInterceptor
    ): Call.Factory {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor)
        }
        builder.addInterceptor(authenticationInterceptor)
        return builder.build()
    }

    @Provides
    @Singleton
    fun httpLoggingInterceptor() : HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Provides
    @Singleton
    fun moviesApiService(
        networkJson: Json,
        okHttpCallFactory: Call.Factory,
        configProvider: ConfigProvider,
    ) : MoviesApi {
        return Retrofit.Builder()
            .baseUrl(configProvider.getMoviesApiBaseUrl())
            .callFactory(okHttpCallFactory)
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .build()
            .create(MoviesApi::class.java)
    }
}
