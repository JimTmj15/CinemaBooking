package com.fave.cinemabooking.utils.network.responses

/**
 * This class is to further converted response return from NetworkResponse class
 * into loading, success, failure for the ease of showing in UI
 */
sealed class ApiResults<out T:Any> {
    data class Loading(val isLoading: Boolean): ApiResults<Nothing>()
    data class Success<T:Any>(val data: Any): ApiResults<T>()
    data class Failure(val err: String): ApiResults<Nothing>()
}