package com.fave.cinemabooking

import com.fave.cinemabooking.utils.network.custom_components.NetworkResponseAdapterFactory
import com.fave.cinemabooking.utils.network.endpoints.ApiHelperImpl
import com.fave.cinemabooking.utils.network.endpoints.ApiService
import com.fave.cinemabooking.utils.network.responses.NetworkResponse
import com.fave.cinemabooking.utils.repository.Repository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.bouncycastle.crypto.tls.ConnectionEnd.server
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleUnitTest {


    @Before
    fun setup() {
    }

    @After
    fun teardown() {
    }
}