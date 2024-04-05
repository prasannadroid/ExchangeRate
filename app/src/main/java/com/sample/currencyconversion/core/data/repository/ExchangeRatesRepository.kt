package com.sample.currencyconversion.core.data.repository

import com.sample.currencyconversion.core.data.remote.response.ExchangeRatesResponse
import com.sample.currencyconversion.core.data.remote.response.status.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface ExchangeRatesRepository {

    suspend fun getExchangeRatesList(appId: String): ApiResponse<ExchangeRatesResponse>

    suspend fun saveExchangeRateResponse(exchangeRatesResponse: ExchangeRatesResponse)

    suspend fun getExchangeRateInBackground(
        appId: String,
        scope: CoroutineScope
    ): ApiResponse<ExchangeRatesResponse>

    fun getSavedExchangeRate(): Flow<String?>

    suspend fun saveExchangeRateJobState(jobState: Boolean)

    suspend fun isExchangeRateJobExecuted(): Boolean?


}