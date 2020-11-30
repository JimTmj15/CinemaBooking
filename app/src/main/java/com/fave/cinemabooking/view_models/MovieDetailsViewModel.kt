package com.fave.cinemabooking.view_models

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import com.fave.cinemabooking.models.network.models.movie_details.NetworkMovieDetailsModel
import com.fave.cinemabooking.models.network.models.movie_list.NetworkMovieListModel
import com.fave.cinemabooking.utils.constant.Constants
import com.fave.cinemabooking.utils.di.qualifiers.ApiKey
import com.fave.cinemabooking.utils.helpers.Helper
import com.fave.cinemabooking.utils.mapper.DomainMapper
import com.fave.cinemabooking.utils.network.responses.ApiResults
import com.fave.cinemabooking.utils.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailsViewModel  @ViewModelInject constructor(
    private val repository: Repository,
    private val domainMapper: DomainMapper
): ViewModel() {

    val apiKey = Constants.API_KEY.key

    //live data for get products
    val movieDetails: LiveData<ApiResults<Any>> get() = mutableMovieDetails
    private val mutableMovieDetails = MutableLiveData<ApiResults<Any>>()


    fun getMovieDetails(movieId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mutableMovieDetails.postValue(ApiResults.Loading(true))
            val result = repository.getMovieDetails(movieId = movieId, apiKey = apiKey)
            when(val transformedResult = Helper.dataTransformer(result = result)){
                is ApiResults.Success -> {
                    val res = transformedResult.data as NetworkMovieDetailsModel
                    val items = domainMapper.mapNetworkMovieDetailsToDomainModel(result = res)
                    mutableMovieDetails.postValue(ApiResults.Success(items))
                }
                is ApiResults.Failure -> {
                    val err = transformedResult.err
                    mutableMovieDetails.postValue(ApiResults.Failure(err))
                }
            }
        }
    }
}