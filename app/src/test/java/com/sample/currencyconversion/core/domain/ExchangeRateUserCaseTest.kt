package com.sample.currencyconversion.core.domain

import com.sample.currencyconversion.core.data.remote.response.ExchangeRatesResponse
import com.sample.currencyconversion.core.data.remote.response.status.ApiResponse
import com.sample.currencyconversion.core.data.repository.ExchangeRatesRepository
import com.sample.currencyconversion.helper.TestHelper
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.verify
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class ExchangeRateUserCaseTest {

    private lateinit var exchangeRateUserCase: ExchangeRateUserCase

    private val exchangeRatesRepository: ExchangeRatesRepository = Mockito.mock()

    private val exchangeRatesResponse: ExchangeRatesResponse = Mockito.mock()

    private val exchangeRatesResponseError: Throwable = Mockito.mock()

    @Before
    fun setup() = runTest {

        exchangeRateUserCase = ExchangeRateUserCase(exchangeRatesRepository)

    }

    @Test
    fun testGetExchangeRatesList_Success() = runTest {
        Mockito.`when`(exchangeRateUserCase.getExchangeRates(TestHelper.validUrl)).thenReturn(
            ApiResponse.Success(
                exchangeRatesResponse
            )
        )

        val result = exchangeRateUserCase.getExchangeRates(TestHelper.validUrl)
        assert(result is ApiResponse.Success)
    }

    @Test
    fun testGetExchangeRatesList_Error() = runTest {
        Mockito.`when`(exchangeRateUserCase.getExchangeRates(TestHelper.invalidUrl)).thenReturn(
            ApiResponse.Error(
                exchangeRatesResponseError
            )
        )

        val result = exchangeRateUserCase.getExchangeRates(TestHelper.invalidUrl)
        assert(result is ApiResponse.Error)
    }

    @Test
    fun testGetExchangeRatesList_MethodCall() = runTest {

        exchangeRateUserCase.getExchangeRates(TestHelper.validUrl)

        verify(exchangeRatesRepository).getExchangeRatesList(TestHelper.validUrl)
    }

    @Test
    fun testGetArrayList_FromMapTrue_or_False() = runTest {

        val map = HashMap<String, Double>()
        val emptyMap = HashMap<String, Double>()
        map["USD"] = 1.0

        // test with values
        val list = exchangeRateUserCase.getArrayListFromCurrencyMap(map)
        assertEquals(list.size, 1)
        assertEquals(list[0].currencyValue, 1.0)

        // test with empty values
        val emptyList = exchangeRateUserCase.getArrayListFromCurrencyMap(emptyMap)
        assertEquals(emptyList.size, 0)

    }

    @Test
    fun testGetSavedExchangeRate_Success() = runTest {
        // read sample json from file
        val savedExchangeRateStr = TestHelper.readFileResponse("/response_200.json")

        val flowValue = flowOf(savedExchangeRateStr)
        Mockito.`when`(exchangeRatesRepository.getSavedExchangeRate()).thenReturn(
            flowValue
        )

        val result = exchangeRateUserCase.getSavedExchangeRate()
        println("///// result $result")
        assert(result is ApiResponse.Success)
    }

    @Test
    fun testGetSavedExchangeRate_WithError() = runTest {

        // test with invalid json format
        val flowValue = flowOf("some invalid json response ")
        Mockito.`when`(exchangeRatesRepository.getSavedExchangeRate()).thenReturn(
            flowValue
        )

        val result = exchangeRateUserCase.getSavedExchangeRate()
        assert(result is ApiResponse.Error)
    }

    @Test
    fun testSaveExchangeRateJobState_MethodInvoke() = runTest {

        exchangeRateUserCase.saveExchangeRateJobState(true)

        Mockito.verify(exchangeRatesRepository).saveExchangeRateJobState(true)

    }

}