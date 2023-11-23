package pl.ml.demo.movies.network

import okhttp3.Interceptor
import okhttp3.Response
import pl.ml.demo.movies.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
            .build()
        return chain.proceed(request)
    }
}