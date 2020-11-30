package com.fave.cinemabooking.utils.network.endpoints

import com.fave.cinemabooking.models.network.ApiResponse.Error
import com.fave.cinemabooking.models.network.models.movie_details.NetworkMovieDetailsModel
import com.fave.cinemabooking.models.network.models.movie_list.NetworkMovieListModel
import com.fave.cinemabooking.utils.network.responses.NetworkResponse

interface ApiHelper {
    suspend fun getMovieList(apiKey:String,
                             releaseDate: String,
                             sortBy: String,
                             page: String): NetworkResponse<NetworkMovieListModel, Error>

    suspend fun getMovieDetails(movieId:String,
                             apiKey: String): NetworkResponse<NetworkMovieDetailsModel, Error>
}