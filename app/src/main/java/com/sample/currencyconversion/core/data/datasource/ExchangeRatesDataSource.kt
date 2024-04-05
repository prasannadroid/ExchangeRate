package com.sample.currencyconversion.core.data.datasource

import com.sample.currencyconversion.core.data.remote.ApiService
import com.sample.currencyconversion.core.data.remote.response.ExchangeRatesResponse
import com.sample.currencyconversion.core.data.remote.response.status.ApiResponse

class ExchangeRatesDataSource(private val retrofitService: ApiService) {

    // fetch from the server
    suspend fun fetchExchangeRates(appId: String): ApiResponse<ExchangeRatesResponse> {
        return try {
            val response = retrofitService.fetchExchangeRates(appId)

            if (response.isSuccessful) {
                ApiResponse.Success(response.body())

            } else {
                // Handle error response
                ApiResponse.Error(Throwable(response.message()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse.Error(Throwable(e))

        }
    }

}
