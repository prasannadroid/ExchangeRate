package com.sample.currencyconversion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.sample.currencyconversion.core.data.apistate.APIState
import com.sample.currencyconversion.core.data.dto.CurrencyDTO
import com.sample.currencyconversion.core.data.local.MyKeyProvider
import com.sample.currencyconversion.core.data.remote.response.ExchangeRatesResponse
import com.sample.currencyconversion.core.data.remote.response.status.ApiResponse
import com.sample.currencyconversion.core.data.service.MyScheduler
import com.sample.currencyconversion.core.presenter.CurrencyButton
import com.sample.currencyconversion.core.presenter.ExchangeRateContent
import com.sample.currencyconversion.core.presenter.ExchangeRateViewModel
import com.sample.currencyconversion.ui.theme.CurrencyConversionTheme
import com.sample.currencyconversion.util.ResourceIdleManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModel<ExchangeRateViewModel>()

    private val myScheduler: MyScheduler by inject()

    // hold api key / app id
    private val myKeyProvider: MyKeyProvider by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConversionTheme {
                ContainMain()
            }

            mainViewModel.loadState.observe(this) {
                when (it) {
                    APIState.STARTED -> {
                        // use for ui testing resource idling
                        ResourceIdleManager.increment()
                    }

                    APIState.STOPPED -> {
                        ResourceIdleManager.decrement()

                    }

                    else -> {
                        ResourceIdleManager.decrement()
                    }
                }

            }

        }

        lifecycleScope.launch(Dispatchers.IO) {
            myScheduler.scheduleBackgroundJob()
            mainViewModel.saveExchangeRateJobState(Dispatchers.IO, false)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // stop background data fetching
        myScheduler.cancelJob()
    }

    private fun getSavedExchangeRate(
        snackBarState: MutableState<Boolean>,
        currencyList: MutableState<List<CurrencyDTO>>,
        onFetchFromServer: () -> Unit
    ) {

        mainViewModel.savedExchangeRateLiveData.observe(this) {

            when (it) {
                is ApiResponse.Success -> {
                    val data = it.data
                    if (data == null) {
                        //fetching callback from api
                        onFetchFromServer()
                    } else {
                        processExchangeRateResponse(snackBarState, currencyList, it)
                    }
                }

                is ApiResponse.Error -> {
                    // re-fetch if there is any issue
                    onFetchFromServer()
                }

                else -> {
                    // re-fetch if there is any issue
                    onFetchFromServer()
                }
            }

        }
    }

    private fun fetchExchangeRate(
        snackBarState: MutableState<Boolean>, currencyList: MutableState<List<CurrencyDTO>>
    ) {

        // handle the http call response live data
        mainViewModel.responseLiveData.observe(this) {
            // set data to the ui
            processExchangeRateResponse(snackBarState, currencyList, it)

        }
    }

    // process the fetch data from server or saved data from the local data storage
    private fun processExchangeRateResponse(
        snackBarState: MutableState<Boolean>,
        currencyList: MutableState<List<CurrencyDTO>>,
        apiResponse: ApiResponse<ExchangeRatesResponse>
    ) {
        try {

            when (apiResponse) {
                is ApiResponse.Success -> {
                    apiResponse.data?.let { exchangeRate ->
                        currencyList.value =
                            mainViewModel.getArrayListFromCurrencyMap(exchangeRate.ratesList)
                        snackBarState.value = exchangeRate.ratesList.isEmpty()
                    }
                }

                is ApiResponse.Error -> {
                    snackBarState.value = true
                }

            }
        } catch (e: Exception) {
            snackBarState.value = true
            e.printStackTrace()
        }
    }


    @Composable
    fun ContainMain() {

        val snackBarHostState = remember {
            SnackbarHostState()
        }

        val currencyList = remember { mutableStateOf(emptyList<CurrencyDTO>()) }

        val showSnackBarState = remember { mutableStateOf(false) }

        LaunchedEffect(showSnackBarState.value) {
            if (showSnackBarState.value) {
                snackBarHostState.showSnackbar(
                    getString(R.string.empty), duration = SnackbarDuration.Indefinite
                )
            } else {
                snackBarHostState.currentSnackbarData?.dismiss()
            }
        }

        Scaffold(modifier = Modifier
            .background(Color.Gray)
            .fillMaxSize(), snackbarHost = {

            SnackbarHost(hostState = snackBarHostState) {
                Snackbar(
                    action = {
                        Text(
                            modifier = Modifier
                                .width(60.dp)
                                .clickable {
                                    // re fetch the api call when error
                                    mainViewModel.getExchangeRates(getApiKey(), Dispatchers.IO)
                                    showSnackBarState.value = false
                                },
                            text = stringResource(id = R.string.retry),

                            )

                    },
                    modifier = Modifier.padding(8.dp),

                    ) { Text(text = stringResource(id = R.string.something_went_wrong)) }
            }

        }, content = { padding ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
            ) {

                // observing the saved api response first
                getSavedExchangeRate(showSnackBarState, currencyList) {
                    // observing the listeners to show fetched response from api call
                    fetchExchangeRate(showSnackBarState, currencyList)

                    // fetch new response from the api
                    mainViewModel.getExchangeRates(getApiKey(), Dispatchers.IO)
                }

                mainViewModel.getSavedExchangeRate(Dispatchers.IO)
                ExchangeRateContent(currencyList)

            }
        })

    }

    private fun getApiKey() = myKeyProvider.readFromPropertiesFile()


    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MaterialTheme {
            CurrencyButton("USD", true) {}
        }
    }
}
