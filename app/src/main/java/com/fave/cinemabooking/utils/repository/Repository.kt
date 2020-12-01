package com.fave.cinemabooking.utils.repository

import com.fave.cinemabooking.models.network.ApiResponse.*
import com.fave.cinemabooking.models.network.models.movie_details.NetworkMovieDetailsModel
import com.fave.cinemabooking.models.network.models.movie_list.NetworkMovieListModel
import com.fave.cinemabooking.utils.network.endpoints.ApiHelper
import com.fave.cinemabooking.utils.network.responses.NetworkResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped

import javax.inject.Inject

/*
 * This is repository design method which the repository will handle
 * data from multiple sources (eg: API call or local DB).
 *
 * The instance for the repository is only retained within activity lifecycle,
 * to reduce memory leak issue.
 */
@ActivityRetainedScoped
class Repository @Inject constructor(
    private val apiHelper: ApiHelper
) {

    suspend fun getMovieList(
        apiKey: String,
        releaseDate: String,
        sortBy: String,
        page: String
    ): NetworkResponse<NetworkMovieListModel, Error> =
        apiHelper.getMovieList(apiKey, releaseDate, sortBy, page)

    suspend fun getMovieDetails(
        movieId: String,
        apiKey: String
    ): NetworkResponse<NetworkMovieDetailsModel, Error> =
        apiHelper.getMovieDetails(movieId, apiKey)

}