package com.fave.cinemabooking

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fave.cinemabooking.models.network.ApiResponse
import com.fave.cinemabooking.models.network.models.movie_list.NetworkMovieListModel
import com.fave.cinemabooking.ui.activity.MovieListActivity
import com.fave.cinemabooking.utils.network.custom_components.NetworkResponseAdapterFactory
import com.fave.cinemabooking.utils.network.endpoints.ApiService
import com.fave.cinemabooking.utils.network.responses.NetworkResponse
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.*
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import kotlin.jvm.Throws

/**
 * If the test is successful, the app is able to run in the physical device with a green tick
 */
@RunWith(AndroidJUnit4::class)
class MovieListActivityTest {
    private var mockWebServer: MockWebServer = MockWebServer()

    lateinit var testApi:TestApi

    @get:Rule
    var activityRule: ActivityScenarioRule<MovieListActivity> =
        ActivityScenarioRule(MovieListActivity::class.java)

    @Before
    fun setup() {
        //provide okhttp3 client instance
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.SECONDS)
            .readTimeout(2, TimeUnit.SECONDS)
            .writeTimeout(2, TimeUnit.SECONDS)
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit
            .Builder()
            .baseUrl("http://api.themoviedb.org")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .client(okHttpClient)
            .build()

        testApi = retrofit.create(TestApi::class.java)

        IdlingRegistry.getInstance().register(
            OkHttp3IdlingResource.create(
                "okhttp",
                okHttpClient
            )
        )

        mockWebServer.url("http://api.themoviedb.org")
    }

    @After
    fun teardown() {
        mockWebServer.shutdown() //tear down mock server after test
    }

    //cater for successful response - test get movie list
    @Test
    fun testSuccessfulMovieListResponse() {
        //mock response return from server
        val mockResponse =  MockResponse()
                    .setResponseCode(200)

        mockWebServer.enqueue(mockResponse)
        testApi.test().enqueue(object: Callback<NetworkResponse<NetworkMovieListModel, ApiResponse.Error>> {
            override fun onResponse(
                call: Call<NetworkResponse<NetworkMovieListModel, ApiResponse.Error>>,
                response: Response<NetworkResponse<NetworkMovieListModel, ApiResponse.Error>>
            ) {
                assertThat(response.isSuccessful, `is`(true))
                assertThat(response.code(), `is`(mockResponse.status.toInt()))
            }

            override fun onFailure(
                call: Call<NetworkResponse<NetworkMovieListModel, ApiResponse.Error>>,
                t: Throwable
            ) {
                //not expecting any error here
            }
        })
    }


    //cater for fail response
    @Test
    fun testFailedResponse()  {
        //mock 1MB dummy data to throttle for 5 seconds - simulate server loading
        val response = MockResponse()
            .setSocketPolicy(SocketPolicy.NO_RESPONSE)
            .throttleBody(1024, 5, TimeUnit.SECONDS)

        mockWebServer.enqueue(response)
        testApi.test().enqueue(object: Callback<NetworkResponse<NetworkMovieListModel, ApiResponse.Error>> {
            override fun onResponse(
                call: Call<NetworkResponse<NetworkMovieListModel, ApiResponse.Error>>,
                response: Response<NetworkResponse<NetworkMovieListModel, ApiResponse.Error>>
            ) {
                //not expecting result here
            }

            override fun onFailure(
                call: Call<NetworkResponse<NetworkMovieListModel, ApiResponse.Error>>,
                t: Throwable
            ) {

                val err = t as SocketTimeoutException
                assertThat(err, isA(SocketTimeoutException::class.java))
                onView(withId(R.id.loader))
                    .check((doesNotExist()))
            }

        })
    }

    interface TestApi {

        @GET("/3/discover/movie?api_key=328c283cd27bd1877d9080ccb1604c91&primary_release_date.lte=2016-01-01&sort_by=release_date.desc&page=1")
        fun test(): Call<NetworkResponse<NetworkMovieListModel, ApiResponse.Error>>
    }

}