package com.fave.cinemabooking.utils.network.endpoints

import com.fave.cinemabooking.models.network.ApiResponse.*
import com.fave.cinemabooking.models.network.models.movie_details.NetworkMovieDetailsModel
import com.fave.cinemabooking.models.network.models.movie_list.NetworkMovieListModel
import com.fave.cinemabooking.utils.network.responses.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/*
* Define all the API endpoints. Standardise network response with success/error
*/
interface ApiService {

    @GET("/3/discover/movie")
    suspend fun getMovieList(
        @Query("api_key") apiKey:String,
        @Query("primary_release_date.lte") releaseDate: String,
        @Query("sort_by") sortBy: String,
        @Query("page") page: String): NetworkResponse<NetworkMovieListModel, Error>

    @GET("/3/movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId:String,
        @Query("api_key") apiKey: String): NetworkResponse<NetworkMovieDetailsModel, Error>

}