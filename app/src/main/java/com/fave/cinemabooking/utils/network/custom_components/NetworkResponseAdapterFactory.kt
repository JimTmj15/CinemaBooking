package com.fave.cinemabooking.utils.network.custom_components

import com.fave.cinemabooking.utils.network.responses.NetworkResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * This class is responsible to get the response from network API call,
 * check if the return type is NetworkResponse (self-defined),
 * if yes, it will return Success/Error,
 * otherwise, it will return null
 */
class NetworkResponseAdapterFactory: CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // suspend functions wrap the response type in `Call`
        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        /* check first that the return type is `ParameterizedType`
        *  The parameterized type (from Generic type) must be equivalent to
        *  our defined NetworkResponse<Success, Error>
        */
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<NetworkResponse<Success, Error> or Call<NetworkResponse<out Success, out Error>>"
        }

        // get the response type inside the `Call` type
        val responseType = getParameterUpperBound(0, returnType)
        // if the response type is not NetworkResponse then we can't handle this type, so we return null
        if (getRawType(responseType) != NetworkResponse::class.java) {
            return null
        }

        // the response type is NetworkResponse and should be parameterized
        check(responseType is ParameterizedType) { "Response must be parameterized as NetworkResponse<Success, Error> or NetworkResponse<out Success, out Error>" }

        val successBodyType = getParameterUpperBound(0, responseType)
        val errorBodyType = getParameterUpperBound(1, responseType)

        val errorBodyConverter =
            retrofit.nextResponseBodyConverter<Any>(null, errorBodyType, annotations)

        return NetworkCallAdapter<Any, Any>(successBodyType, errorBodyConverter)
    }
}