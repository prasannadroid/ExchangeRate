package com.sample.currencyconversion.core.data.datasource

import com.sample.currencyconversion.core.data.remote.ApiService
import com.sample.currencyconversion.core.data.remote.response.status.ApiResponse
import com.sample.currencyconversion.helper.TestHelper
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class ExchangeRateDataSourceTest {

    private lateinit var apiService: ApiService
    private lateinit var exchangeRatesDataSource: ExchangeRatesDataSource
    private lateinit var mockWebServer: MockWebServer
    private lateinit var response: MockResponse

    @Before
    fun setup() {

        mockWebServer = MockWebServer()
        mockWebServer.start()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)

        response = MockResponse()

        exchangeRatesDataSource = ExchangeRatesDataSource(apiService)
    }

    @Test
    fun testGetCurrencyList_Success() = runTest {
       // reading response from response_200.json file
        val content = TestHelper.readFileResponse("/response_200.json")
        response.setBody(content)
        response.setResponseCode(200)
        mockWebServer.enqueue(response)

        val response = exchangeRatesDataSource.fetchExchangeRates(TestHelper.validUrl)
        mockWebServer.takeRequest()

        assert(response is ApiResponse.Success)
        assert((response as ApiResponse.Success).data != null)
        // assert few currency codes and values to make sure the response is correct
        assert(response.data!!.base == "USD")
        assert(response.data!!.ratesList["AED"] == 3.673)
        assert(response.data!!.ratesList["AFN"] == 72.268373)
    }

    @Test
    fun testGetCurrencyList_NotAuthorized() = runTest {
        // test with http code 401
        response.setResponseCode(401)
        mockWebServer.enqueue(response)

        val result = exchangeRatesDataSource.fetchExchangeRates(TestHelper.invalidUrl)
        mockWebServer.takeRequest()

        assert(result is ApiResponse.Error)
        assert((result as ApiResponse.Error).exception?.message == "Client Error")

    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}