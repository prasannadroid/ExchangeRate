package com.sample.currencyconversion.core.data.remote

import com.sample.currencyconversion.core.data.remote.response.ExchangeRatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v3/66ee995d-86ae-45c5-adad-064fcf4c3305") // app id is not using now
    suspend fun fetchExchangeRates(@Query("app_id") appId: String): Response<ExchangeRatesResponse>

}
