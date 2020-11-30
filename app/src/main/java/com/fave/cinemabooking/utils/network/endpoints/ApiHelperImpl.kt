package com.fave.cinemabooking.utils.network.endpoints

import com.fave.cinemabooking.models.network.ApiResponse.*
import com.fave.cinemabooking.models.network.models.movie_details.NetworkMovieDetailsModel
import com.fave.cinemabooking.models.network.models.movie_list.NetworkMovieListModel
import com.fave.cinemabooking.utils.network.responses.NetworkResponse
import javax.inject.Inject

/*
* This is the implementation class for Api Helper interface which required for DI purpose.
*/

class ApiHelperImpl @Inject constructor(private val apiService: ApiService): ApiHelper {

    override suspend fun getMovieList(apiKey:String,
                                      releaseDate: String,
                                      sortBy: String,
                                      page: String): NetworkResponse<NetworkMovieListModel, Error> = apiService.getMovieList(apiKey, releaseDate, sortBy, page)

    override suspend fun getMovieDetails(
        movieId: String,
        apiKey: String
    ): NetworkResponse<NetworkMovieDetailsModel, Error> =
        apiService.getMovieDetails(movieId, apiKey)
}