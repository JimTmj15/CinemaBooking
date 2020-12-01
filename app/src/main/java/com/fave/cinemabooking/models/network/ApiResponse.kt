package com.fave.cinemabooking.models.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

class ApiResponse {

/*
 * Standardised API response handling: Success/Error
 *
 * Success case:
 * 1. data return from API
 *
 * Error case:
 * 1. message
 * 2. status
 */
    @JsonClass(generateAdapter = true)
    data class Success(
        @Json(name = "data")
        val data: Any
    )

    @JsonClass(generateAdapter = true)
    data class Error(
        @Json(name = "status")
        val status: String,

        @Json(name = "message")
        val msg: String
    )
}