package com.fave.cinemabooking.utils.network.custom_components

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import java.lang.reflect.Type

/*
* This class functions to adapt {@code S} to our custom network call with Success & Error as response
*/
class NetworkCallAdapter<S : Any, E : Any>(
    private val respType: Type,
    private val errorConverter: Converter<ResponseBody, E>
) : CallAdapter<S, NetworkCall<S, E>> {

    override fun adapt(call: Call<S>): NetworkCall<S, E> = NetworkCall(call, errorConverter)

    override fun responseType(): Type = respType
}