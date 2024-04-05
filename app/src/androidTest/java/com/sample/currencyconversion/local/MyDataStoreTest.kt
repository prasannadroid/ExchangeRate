package com.sample.currencyconversion.local

import androidx.datastore.preferences.core.edit
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.sample.currencyconversion.core.data.local.MyDataStore
import com.sample.currencyconversion.helper.TestHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MyDataStoreTest {

    private var context = getInstrumentation().targetContext

    private var dataStore = TestHelper.getDataStore(context)

    private lateinit var myDataStore: MyDataStore

    @Before
    fun setup() {
        myDataStore = MyDataStore(dataStore)
    }

    @Test
    fun saveExchangeRate_Success() = runTest {
        val testJson = "{\"key\": \"value\"}"
        myDataStore.saveExchangeRate(testJson)

        val jsonResult = myDataStore.getSavedExchangeRateJson().first()
        Assert.assertEquals(testJson, jsonResult)

    }

    @Test
    fun clearExchangeRate_Success() = runTest {
        val testJson = "{\"key\": \"value\"}"
        myDataStore.saveExchangeRate(testJson)

        val jsonResult = myDataStore.getSavedExchangeRateJson().first()
        Assert.assertEquals(testJson, jsonResult)
        myDataStore.clearApiResponse()
        val resultAfterClear = myDataStore.getSavedExchangeRateJson().first()
        Assert.assertEquals(resultAfterClear, null)
    }

    @After
    fun tearDown() {
        runTest {
            dataStore.edit {
                it.clear()
            }
        }
    }
}