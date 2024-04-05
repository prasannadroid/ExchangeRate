package com.sample.currencyconversion.core.data.repository

import com.google.gson.Gson
import com.sample.currencyconversion.core.data.datasource.ExchangeRatesDataSource
import com.sample.currencyconversion.core.data.local.MyDataStore
import com.sample.currencyconversion.core.data.remote.response.ExchangeRatesResponse
import com.sample.currencyconversion.core.data.remote.response.status.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ExchangeRatesRepositoryImpl(
    private val exchangeRatesDataSource: ExchangeRatesDataSource,
    private val myDataStore: MyDataStore
) :
    ExchangeRatesRepository {

    // fetch from the server
    override suspend fun getExchangeRatesList(appId: String): ApiResponse<ExchangeRatesResponse> {
        return exchangeRatesDataSource.fetchExchangeRates(appId)
    }

    // save exchange rate to data store as json
    override suspend fun saveExchangeRateResponse(exchangeRatesResponse: ExchangeRatesResponse) {
        myDataStore.saveExchangeRate(Gson().toJson(exchangeRatesResponse))
    }

    // get exchange rates in back ground by job scheduler
    override suspend fun getExchangeRateInBackground(
        appId: String, scope: CoroutineScope
    ) = exchangeRatesDataSource.fetchExchangeRates(appId)

    // convert api response to json response
    fun getApiResponseJson(response: ApiResponse<ExchangeRatesResponse>): String {
        return Gson().toJson(response)
    }

    // get saved exchange rate json from local data storage
    override fun getSavedExchangeRate(): Flow<String?> = myDataStore.getSavedExchangeRateJson()

    // save exchange rate saved state to the local storage
    override suspend fun saveExchangeRateJobState(jobState: Boolean) =
        myDataStore.saveExchangeRateJobState(jobState)

    // check exchange rate has been saved to the local storage
    override suspend fun isExchangeRateJobExecuted() =
        myDataStore.isExchangeRateJobExecuted().first()

}