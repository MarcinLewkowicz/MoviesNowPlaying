package pl.ml.demo.movies.network

import com.google.common.truth.Truth.assertThat
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test
import pl.ml.demo.movies.BuildConfig


class AuthenticationInterceptorTest {

    private lateinit var okHttpClient: OkHttpClient
    private lateinit var mockWebServer: MockWebServer
    private lateinit var authenticationInterceptor: AuthenticationInterceptor

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)

        authenticationInterceptor =
            AuthenticationInterceptor()
        okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(authenticationInterceptor)
            .build()
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `authenticationInterceptorTest header added to request `() {
        // GIVEN
        mockWebServer.dispatcher = getDispatcher()
        // WHEN
        okHttpClient.newCall(
            Request.Builder()
                .url(mockWebServer.url("/"))
                .build()
        ).execute()
        // THEN
        val request = mockWebServer.takeRequest()
        with(request) {
            assertThat(getHeader("Authorization"))
                .isEqualTo("Bearer ${BuildConfig.API_KEY}")
        }
    }

    private fun getDispatcher() = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(200)
        }
    }

}