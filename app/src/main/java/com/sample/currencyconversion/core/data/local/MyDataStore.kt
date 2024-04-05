package com.sample.currencyconversion.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

class MyDataStore(private val dataStore: DataStore<Preferences>) {

    suspend fun saveExchangeRate(jsonData: String?): Preferences = dataStore.edit { preferences ->
        if (!jsonData.isNullOrBlank()) {
            preferences[EXCHANGE_RATE_JSON] = jsonData
        }
    }

    // display language instruction saved state
    fun getSavedExchangeRateJson() = dataStore.data.map { preferences ->
        preferences[EXCHANGE_RATE_JSON]
    }

    suspend fun clearApiResponse() = dataStore.edit {
        it.clear()
    }

    // use to pre
    fun isExchangeRateJobExecuted() = dataStore.data.map { preferences ->
        preferences[EXCHANGE_RATE_JOB_STATE]
    }

    suspend fun saveExchangeRateJobState(jobState: Boolean) {
        dataStore.edit { preferences ->
            preferences[EXCHANGE_RATE_JOB_STATE] = jobState
        }
    }

    suspend fun get() = dataStore.edit {
        it.clear()
    }

    companion object {
        // data store keys
        val EXCHANGE_RATE_JSON = stringPreferencesKey("EXCHANGE_RATE_JSON")
        val EXCHANGE_RATE_JOB_STATE = booleanPreferencesKey("EXCHANGE_RATE_JOB_STATE")

    }
}