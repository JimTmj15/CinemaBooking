package com.fave.cinemabooking.utils.network.custom_components

import com.fave.cinemabooking.utils.network.responses.NetworkResponse
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

/*
 * This class custom retrofit call which purpose is to standardise the network response.
 * This class only support async network call. Sync network call is not supported.
 *
 * The responses will stream down into 5 categories:
 * 1. Success
 * 2. Error
 * 3. Unknown error
 * 4. Socket Timeout error
 * 5. Network error
 */
class NetworkCall<S : Any, E : Any>(
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, E>
) : Call<NetworkResponse<S, E>> {

    override fun enqueue(callback: Callback<NetworkResponse<S, E>>) {
        delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                //compose network response into body, code and error
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@NetworkCall,
                            Response.success(NetworkResponse.Success(body))
                        )
                    } else {
                        // Response is successful but the body is null
                        callback.onResponse(
                            this@NetworkCall,
                            Response.success(NetworkResponse.UnknownError(Throwable()))
                        )
                    }
                } else {
                    val errorBody = when {
                        error == null -> null
                        error.contentLength() == 0L -> null
                        else -> try {
                            errorConverter.convert(error)
                        } catch (ex: Exception) {
                            null
                        }
                    }
                    if (errorBody != null) {
                        callback.onResponse(
                            this@NetworkCall,
                            Response.success(NetworkResponse.Failure(errorBody, code))
                        )
                    } else {
                        callback.onResponse(
                            this@NetworkCall,
                            Response.success(NetworkResponse.UnknownError(Throwable()))
                        )
                    }
                }
            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                val networkResponse = when (throwable) {
                    //timeout is under IOException
                    is IOException -> NetworkResponse.NetworkError(throwable)
                    //socket timeout
                    is SocketTimeoutException -> NetworkResponse.SocketTimeoutError(throwable)
                    else -> NetworkResponse.UnknownError(throwable)
                }
                callback.onResponse(this@NetworkCall, Response.success(networkResponse))
            }
        })
    }

    //use default function method by retrofit
    override fun isExecuted(): Boolean = delegate.isExecuted

    //use default function method by retrofit
    override fun timeout(): Timeout = delegate.timeout()

    //use default function method by retrofit
    override fun clone(): Call<NetworkResponse<S, E>> = NetworkCall(delegate.clone(), errorConverter)

    //use default function method by retrofit
    override fun isCanceled(): Boolean = delegate.isCanceled

    //use default function method by retrofit
    override fun cancel() = delegate.cancel()

    //override function method by retrofit
    override fun execute(): Response<NetworkResponse<S, E>>  {
        throw UnsupportedOperationException("Doesn't support execute operation")
    }

    //use default function method by retrofit  
    override fun request(): Request = delegate.request()

}