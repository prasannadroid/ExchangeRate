package com.sample.currencyconversion.core.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.currencyconversion.core.data.apistate.APIState
import com.sample.currencyconversion.core.data.dto.CurrencyDTO
import com.sample.currencyconversion.core.data.remote.response.ExchangeRatesResponse
import com.sample.currencyconversion.core.data.remote.response.status.ApiResponse
import com.sample.currencyconversion.core.domain.ExchangeRateUserCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

open class ExchangeRateViewModel(private val exchangeRateUserCase: ExchangeRateUserCase) :
    ViewModel() {

    private val response = MutableLiveData<ApiResponse<ExchangeRatesResponse>>()
    val responseLiveData: LiveData<ApiResponse<ExchangeRatesResponse>> = response

    private val isLoadingState = MutableLiveData<APIState>()
    val loadState: LiveData<APIState> = isLoadingState

    private val savedExchangeRate = MutableLiveData<ApiResponse<ExchangeRatesResponse>>()
    val savedExchangeRateLiveData: LiveData<ApiResponse<ExchangeRatesResponse>> = savedExchangeRate

    init {
        isLoadingState.postValue(APIState.STOPPED)
    }

    fun getExchangeRates(
        appId: String,
        dispatcherIO: CoroutineDispatcher
    ) {
        viewModelScope.launch(dispatcherIO) {
            // set loading state true to show progress
            isLoadingState.postValue(APIState.STARTED)

            // fetch data from api
            val result = exchangeRateUserCase.getExchangeRates(appId)

            // set loading state false after fetching result
            isLoadingState.postValue(APIState.STOPPED)

            // set result from api call
            response.postValue(result)

        }
    }

    fun getArrayListFromCurrencyMap(map: Map<String, Double>): List<CurrencyDTO> =
        exchangeRateUserCase.getArrayListFromCurrencyMap(map)

    fun getSavedExchangeRate(dispatcherIO: CoroutineDispatcher) {
        viewModelScope.launch(dispatcherIO) {

            val savedResponse = exchangeRateUserCase.getSavedExchangeRate()

            savedExchangeRate.postValue(savedResponse)
        }
    }

    fun saveExchangeRateJobState(dispatcherIO: CoroutineDispatcher, jobState: Boolean) {
        viewModelScope.launch(dispatcherIO) {
            exchangeRateUserCase.saveExchangeRateJobState(jobState)
        }
    }
}