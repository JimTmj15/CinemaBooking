package com.fave.cinemabooking.utils.network.responses

import java.io.IOException
import java.net.SocketTimeoutException

/*
* This class will stream down the network response into 5 possible
* outcomes:
*
* 1. Success (API success)
* 2. Failure (API fail)
* 3. Network Error
* 4. Unknown Error
* 5. Socket timeout Error
*/
sealed class NetworkResponse<out T:Any, out U: Any> {
    data class Success<T: Any>(val response: Any): NetworkResponse<T, Nothing>()
    data class Failure<U: Any>(val body: Any, val code: Int): NetworkResponse<Nothing, U>()
    data class NetworkError(val msg: IOException): NetworkResponse<Nothing, Nothing>()
    data class UnknownError(val err: Throwable): NetworkResponse<Nothing, Nothing>()
    data class SocketTimeoutError(val msg: SocketTimeoutException): NetworkResponse<Nothing, Nothing>()
}