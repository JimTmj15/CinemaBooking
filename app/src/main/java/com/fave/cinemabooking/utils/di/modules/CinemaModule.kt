package com.fave.cinemabooking.utils.di.modules

import com.fave.cinemabooking.BuildConfig
import com.fave.cinemabooking.utils.di.qualifiers.BaseUrl
import com.fave.cinemabooking.utils.network.custom_components.NetworkResponseAdapterFactory
import com.fave.cinemabooking.utils.network.endpoints.ApiHelper
import com.fave.cinemabooking.utils.network.endpoints.ApiHelperImpl
import com.fave.cinemabooking.utils.network.endpoints.ApiService
import com.fave.cinemabooking.utils.network.interceptor.Interceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.Timeout
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class CinemaModule {

    @BaseUrl()
    val baseUrl = "http://api.themoviedb.org"

    /*
    * okhttp will intercept the network request (BODY level) if the app is built for debug purpose.
    * The http logging interceptor is merely for debugging purpose, therefore it will be removed
    * from production build.
    */
    @Provides
    @Singleton
    fun httpClient(interceptor: Interceptor): OkHttpClient =
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()
        } else OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()


    @Provides
    @Singleton
    fun moshiInstance(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
    .build()


    /*
   * Use moshi as JSON to Object converter
   * Use custom adapter factory to adapt the http response to our NetworkResponse<Success, Error>
   */
    @Provides
    @Singleton
    fun retrofitInstance(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .client(okHttpClient)
            .build()
    }


    /*
    * Provides api service instance throughout the application
    */
    @Provides
    @Singleton
    fun apiServiceInstance(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    /*
    * Provides api helper instance throughout the application
    */
    @Provides
    @Singleton
    fun apiHelperInstance(apiHelperImpl: ApiHelperImpl): ApiHelper = apiHelperImpl

}