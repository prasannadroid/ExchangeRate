package com.sample.currencyconversion.core.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sample.currencyconversion.core.data.apistate.APIState
import com.sample.currencyconversion.core.data.dto.CurrencyDTO
import com.sample.currencyconversion.core.data.remote.response.ExchangeRatesResponse
import com.sample.currencyconversion.core.data.remote.response.status.ApiResponse
import com.sample.currencyconversion.core.domain.ExchangeRateUserCase
import com.sample.currencyconversion.helper.TestHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ExchangeRateViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val dispatcherIO = StandardTestDispatcher()

    private lateinit var exchangeRateViewModel: ExchangeRateViewModel

    private val exchangeRateUserCase: ExchangeRateUserCase = Mockito.mock()

    private val exchangeRatesResponse: ExchangeRatesResponse = Mockito.mock()


    @Before
    fun setup() = runTest {
        Dispatchers.setMain(dispatcherIO)
        exchangeRateViewModel = ExchangeRateViewModel(exchangeRateUserCase)
    }

    @After
    fun tearDown() = runTest {
        Dispatchers.resetMain()
        dispatcherIO.cancel() // Cleanup test coroutines

    }

    @Test
    fun testGetExchangeRatesList_UserCaseMethodInvoke() = runTest {

        exchangeRateViewModel.getExchangeRates(TestHelper.validUrl, dispatcherIO)
        dispatcherIO.scheduler.advanceUntilIdle()

        // Verify user case call
        verify(exchangeRateUserCase).getExchangeRates(TestHelper.validUrl)

    }

    @Test
    fun testExchangeRate_SuccessState() = runTest {

        exchangeRateViewModel.getExchangeRates(TestHelper.validUrl, dispatcherIO)

        `when`(exchangeRateUserCase.getExchangeRates(TestHelper.validUrl))
            .thenReturn(ApiResponse.Success(exchangeRatesResponse))
        exchangeRateUserCase.getExchangeRates(TestHelper.validUrl)

        dispatcherIO.scheduler.advanceUntilIdle()
        // Verify user case call
        assert(exchangeRateViewModel.responseLiveData.value is ApiResponse.Success)

    }

    @Test
    fun testExchangeRate_ErrorStateWithInvalidUrl() = runTest {

        exchangeRateViewModel.getExchangeRates(TestHelper.invalidUrl, dispatcherIO)

        `when`(exchangeRateUserCase.getExchangeRates(TestHelper.invalidUrl))
            .thenReturn(ApiResponse.Error(Exception()))
        exchangeRateUserCase.getExchangeRates(TestHelper.invalidUrl)

        dispatcherIO.scheduler.advanceUntilIdle()
        // Verify user case call
        assert(exchangeRateViewModel.responseLiveData.value is ApiResponse.Error)

    }

    @Test
    fun testLiveDataLoading_afterApiCallSuccess() = runTest {

        exchangeRateViewModel.getExchangeRates(TestHelper.validUrl, dispatcherIO)
        `when`(exchangeRateUserCase.getExchangeRates(TestHelper.validUrl)).thenReturn(
            ApiResponse.Success(
                exchangeRatesResponse
            )
        )

        dispatcherIO.scheduler.advanceUntilIdle() // Advance to finish the coroutine
        assertEquals(exchangeRateViewModel.loadState.value, APIState.STOPPED)
    }

    @Test
    fun testGetArrayList_FromCurrencyMap() = runTest {

        // map with dummy data
        val map = HashMap<String, Double>()
        val list = ArrayList<CurrencyDTO>()

        map["USD"] = 1.0
        map["AED"] = 1.5

        val currencyDTO1 = CurrencyDTO("USD", 1.0)
        val currencyDTO2 = CurrencyDTO("AED", 1.5)
        list.add(currencyDTO1)
        list.add(currencyDTO2)

        // return the dummy data list
        `when`(exchangeRateUserCase.getArrayListFromCurrencyMap(map)).thenReturn(
            list
        )

        val result = exchangeRateViewModel.getArrayListFromCurrencyMap(map)
        verify(exchangeRateUserCase).getArrayListFromCurrencyMap(map)

        assertEquals(result.size, 2)
        assertEquals(result.isEmpty(), false)
        assertEquals(result[0].currencyValue, 1.0)
        assertEquals(result[1].currencyValue, 1.5)
    }

    @Test
    fun testSaveExchangeRate_Loading() = runTest {

        `when`(exchangeRateUserCase.getSavedExchangeRate()).thenReturn(
            ApiResponse.Success(
                exchangeRatesResponse
            )
        )

        exchangeRateViewModel.getSavedExchangeRate(dispatcherIO)

        dispatcherIO.scheduler.advanceUntilIdle() // Advance to finish the coroutine
        verify(exchangeRateUserCase).getSavedExchangeRate()

        assertEquals(exchangeRateViewModel.savedExchangeRateLiveData.value, ApiResponse.Success(exchangeRatesResponse))
    }

    @Test
    fun testSaveExchangeRate_JobState() = runTest {

        exchangeRateViewModel.saveExchangeRateJobState(dispatcherIO,true)

        dispatcherIO.scheduler.advanceUntilIdle() // Advance to finish the coroutine

        verify(exchangeRateUserCase).saveExchangeRateJobState(true)

    }



}