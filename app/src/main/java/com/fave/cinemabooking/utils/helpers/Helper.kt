package com.fave.cinemabooking.utils.helpers

import android.content.Context
import android.widget.Toast
import com.fave.cinemabooking.utils.network.responses.ApiResults
import com.fave.cinemabooking.utils.network.responses.NetworkResponse
import com.fave.cinemabooking.models.network.ApiResponse.*

/*
* Helper class to handle app-related logics
*/
class Helper {
    companion object {
        fun dataTransformer(result: NetworkResponse<Any, Error>): ApiResults<Any> {
            when (result) {
                is NetworkResponse.Success -> {
                    val data = result.response
                    return ApiResults.Success(data)
                }
                is NetworkResponse.SocketTimeoutError -> {
                    val err = result.msg.message ?: ""
                    return ApiResults.Failure(err)
                }
                is NetworkResponse.NetworkError -> {
                    val err = result.msg.localizedMessage ?: ""
                    return ApiResults.Failure(err)
                }
                is NetworkResponse.UnknownError -> {
                    val err = result.err.message ?: ""
                    return ApiResults.Failure(err)
                }
                is NetworkResponse.Failure -> {
                    val err = result.body as String
                    return ApiResults.Failure(err)
                }
            }
        }

        fun showToast(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }

    }
}