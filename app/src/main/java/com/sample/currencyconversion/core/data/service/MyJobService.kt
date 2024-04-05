package com.sample.currencyconversion.core.data.service

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import com.sample.currencyconversion.core.data.local.MyKeyProvider
import com.sample.currencyconversion.core.data.remote.response.status.ApiResponse
import com.sample.currencyconversion.core.data.repository.ExchangeRatesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MyJobService : JobService() {

    private val exchangeRatesRepositoryImpl: ExchangeRatesRepository by inject()

    private val myKeyProvider: MyKeyProvider by inject()

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("MissingPermission") // Required for network access (if applicable)
    override fun onStartJob(params: JobParameters): Boolean {
        // Perform your background task here (e.g., network call)

        GlobalScope.launch(dispatcher) {

            val jobSavedState = exchangeRatesRepositoryImpl.isExchangeRateJobExecuted()

            // prevent fetching the exchange rate from the first time
            if (jobSavedState == false || jobSavedState == null) {
                exchangeRatesRepositoryImpl.saveExchangeRateJobState(true)
                return@launch
            }

            // fetch exchange rates from the api
            val response = exchangeRatesRepositoryImpl.getExchangeRateInBackground(
                myKeyProvider.readFromPropertiesFile(), this
            )

            when (response) {
                is ApiResponse.Success -> {
                    response.data?.let {
                        exchangeRatesRepositoryImpl.saveExchangeRateResponse(response.data)

                    }
                }

                is ApiResponse.Error -> {
                    Log.d(
                        "MyJobService",
                        "Error Fetching Data"
                    )
                }
            }

        }
        Log.d(
            "MyJobService",
            "Background task started!"
        )

        // Job finished
        jobFinished(params, false) // Set needsReschedule to false as it's a periodic job

        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        // If the job was cancelled before finishing, perform any cleanup here
        return false
    }


}
