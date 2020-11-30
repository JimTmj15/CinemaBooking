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
//        mockWebServer.start()
//
//        val moshi = Moshi.Builder()
//            .add(KotlinJsonAdapterFactory())
//            .build()
//
//        val loggingInterceptor = HttpLoggingInterceptor()
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .connectTimeout(10, TimeUnit.SECONDS)
//            .readTimeout(10, TimeUnit.SECONDS)
//            .writeTimeout(10, TimeUnit.SECONDS)
//            .build()
//
//        val retrofit = Retrofit
//            .Builder()
//            .baseUrl(mockWebServer.url("/"))
//            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .addCallAdapterFactory(NetworkResponseAdapterFactory())
//            .client(okHttpClient)
//            .build()
//
//        val apiService = retrofit.create(ApiService::class.java)
//        val apiHelperImpl = ApiHelperImpl(apiService = apiService)
//        repository = Repository(apiHelperImpl)
    }

    @After
    fun teardown() {
//        mockWebServer.shutdown()
    }

    @Test
    fun testApiCall() {
//        runBlocking {

//            val dispatcher: Dispatcher = object : Dispatcher() {
//                override fun dispatch(request: RecordedRequest): MockResponse {
//                    when (request.path) {
//                        "/v1/path1"
//                        -> return MockResponse().setResponseCode(204)
//                        "/v1/path2"
//                        -> return  MockResponse()
//                            .setResponseCode(HttpURLConnection.HTTP_OK)
//                            .setBody("{ result: \"success\" }")
//                    }
//                    return MockResponse().setResponseCode(404)
//                }
//            }
//
//            mockWebServer.dispatcher = dispatcher
//            val request: RecordedRequest = mockWebServer.takeRequest()
//            assertEquals(expectedPath, request.path)
//        }
    }
}