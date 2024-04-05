package com.sample.currencyconversion.core.data.remote.response.status

sealed class ApiResponse<out T> {
    data class Success<T>(val data: T?) : ApiResponse<T>()
    data class Error<T>(val exception: Throwable?) : ApiResponse<T>()
}
