package com.sample.currencyconversion.core.data.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sample.currencyconversion.core.data.datasource.ExchangeRatesDataSource
import com.sample.currencyconversion.core.data.local.MyDataStore
import com.sample.currencyconversion.core.data.remote.response.ExchangeRatesResponse
import com.sample.currencyconversion.core.data.remote.response.status.ApiResponse
import com.sample.currencyconversion.helper.TestHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@RunWith(JUnit4::class)
class ExchangeRatesRepositoryTest {

    private lateinit var exchangeRatesRepositoryImpl: ExchangeRatesRepositoryImpl

    private val exchangeRatesResponse: ExchangeRatesResponse = mock()

    private val exchangeRatesDataSource: ExchangeRatesDataSource = mock()

    private val myDataStore: MyDataStore = mock()


    @Before
    fun setup() = runTest {

        exchangeRatesRepositoryImpl =
            ExchangeRatesRepositoryImpl(exchangeRatesDataSource, myDataStore)
    }

    @Test
    fun testGetExchangeRatesList_Success() = runTest {

        `when`(exchangeRatesRepositoryImpl.getExchangeRatesList(TestHelper.validUrl)).thenReturn(
            ApiResponse.Success(
                exchangeRatesResponse
            )
        )
        // Act
        val result = exchangeRatesRepositoryImpl.getExchangeRatesList(TestHelper.validUrl)

        `when`(exchangeRatesDataSource.fetchExchangeRates(TestHelper.validUrl)).thenReturn(
            ApiResponse.Success(
                exchangeRatesResponse
            )
        )

        val result2 = exchangeRatesDataSource.fetchExchangeRates(TestHelper.validUrl)

        // Assert
        assert(result is ApiResponse.Success)
        assert(result2 is ApiResponse.Success)
    }

    @Test
    fun testSaveExchangeRateResponse() = runTest {

        // Call the method to be tested
        exchangeRatesRepositoryImpl.saveExchangeRateResponse(exchangeRatesResponse)

        // Verify that saveExchangeRate was called with the correct argument
        val expectedJsonString = Gson().toJson(exchangeRatesResponse)
        // If you're verifying that saveExchangeRate was called with the correct argument, use `verify` from Mockito.
        // Verify that saveExchangeRate was called once with the expected argument
        verify(myDataStore).saveExchangeRate(expectedJsonString)
    }

    @Test
    fun testSavesExchangeRate() = runTest {

        val savedExchangeRateStr = TestHelper.readFileResponse("/response_200.json")
        val savedExchangeRate: Flow<String> = flowOf(savedExchangeRateStr)
        // Arrange
        `when`(myDataStore.getSavedExchangeRateJson()).thenReturn(
            savedExchangeRate
        )

        val result = exchangeRatesRepositoryImpl.getSavedExchangeRate()
        verify(myDataStore).getSavedExchangeRateJson()
        MatcherAssert.assertThat((result.first().toString().isNotBlank()), `is`(true))
    }

    @Test
    fun testGetExchangeRate_InBackground() = runTest {
        // Arrange
        `when`(exchangeRatesDataSource.fetchExchangeRates(TestHelper.validUrl)).thenReturn(
            ApiResponse.Success(exchangeRatesResponse)
        )

        // Act
        val result = exchangeRatesRepositoryImpl.getExchangeRateInBackground(
            TestHelper.validUrl,
            this
        )

        // Assert
        assert(result is ApiResponse.Success)

    }

    @Test
    fun testSaveExchangeRate_jobState() = runTest {
        // check null since method return type is Boolean?
        `when`(myDataStore.isExchangeRateJobExecuted()).thenReturn(
            flowOf(null)
        )

        val result = exchangeRatesRepositoryImpl.isExchangeRateJobExecuted()
        MatcherAssert.assertThat((result == null), `is`(true))

        // check true response
        `when`(myDataStore.isExchangeRateJobExecuted()).thenReturn(
            flowOf(true)
        )

        val result2 = exchangeRatesRepositoryImpl.isExchangeRateJobExecuted()
        MatcherAssert.assertThat((result2), `is`(true))

        // check false response
        `when`(myDataStore.isExchangeRateJobExecuted()).thenReturn(
            flowOf(false)
        )

        val result3 = exchangeRatesRepositoryImpl.isExchangeRateJobExecuted()
        MatcherAssert.assertThat((result3), `is`(false))

        verify(myDataStore, times(3)).isExchangeRateJobExecuted()

    }

    @Test
    fun testSaveExchangeRate_JobState() = runTest {

        `when`(myDataStore.isExchangeRateJobExecuted()).thenReturn(
            flowOf(true)
        )
        // call save job state with true
        exchangeRatesRepositoryImpl.saveExchangeRateJobState(true)

        // call two times with true
        verify(myDataStore).saveExchangeRateJobState(true) // this could be true or false

        val result = exchangeRatesRepositoryImpl.isExchangeRateJobExecuted()

        MatcherAssert.assertThat((result), `is`(true))

        `when`(myDataStore.isExchangeRateJobExecuted()).thenReturn(
            flowOf(false)
        )
        // call save job state with  false
        exchangeRatesRepositoryImpl.saveExchangeRateJobState(false)

        // call two times with false
        verify(myDataStore).saveExchangeRateJobState(false) // this could be true or false

        val result2 = exchangeRatesRepositoryImpl.isExchangeRateJobExecuted()
        MatcherAssert.assertThat((result2), `is`(false))

    }

    @Test
    fun testGetApiResponse_Json() = runTest {
        val apiResponse = ApiResponse.Success(exchangeRatesResponse)
        val result = exchangeRatesRepositoryImpl.getApiResponseJson(apiResponse)
        val jsonObj = Gson().fromJson(result, JsonObject::class.java)
        val value = jsonObj.getAsJsonObject("data").get("timestamp").toString()
        MatcherAssert.assertThat((value == "0"), `is`(true))

    }
}

