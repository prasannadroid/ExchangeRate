package com.sample.currencyconversion.core.domain

import com.google.gson.Gson
import com.sample.currencyconversion.core.data.dto.CurrencyDTO
import com.sample.currencyconversion.core.data.remote.response.ExchangeRatesResponse
import com.sample.currencyconversion.core.data.remote.response.status.ApiResponse
import com.sample.currencyconversion.core.data.repository.ExchangeRatesRepository
import kotlinx.coroutines.flow.first

open class ExchangeRateUserCase(private val exchangeRatesRepository: ExchangeRatesRepository) {

    suspend fun getExchangeRates(appId: String) =
        exchangeRatesRepository.getExchangeRatesList(appId)

    fun getArrayListFromCurrencyMap(map: Map<String, Double>): List<CurrencyDTO> {
        return map.map { (key, value) -> CurrencyDTO(key, value) }
    }

    suspend fun getSavedExchangeRate(): ApiResponse<ExchangeRatesResponse> {
        return try {
            val exchangeRateJsonResponseJson =
                exchangeRatesRepository.getSavedExchangeRate().first()

            // Parse the JSON response to ApiResponse<ExchangeRatesResponse>
            val exchangeRatesResponse =
                Gson().fromJson(exchangeRateJsonResponseJson, ExchangeRatesResponse::class.java)

            ApiResponse.Success(exchangeRatesResponse)
        } catch (e: Exception) {
            ApiResponse.Error(Throwable(e))
        }
    }

    suspend fun saveExchangeRateJobState(jobState:Boolean){
       exchangeRatesRepository.saveExchangeRateJobState(jobState)
    }

}