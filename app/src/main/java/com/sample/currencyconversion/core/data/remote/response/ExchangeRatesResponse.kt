package com.sample.currencyconversion.core.data.remote.response

import com.google.gson.annotations.SerializedName


data class ExchangeRatesResponse(
    @SerializedName("disclaimer") val disclaimer: String,
    @SerializedName("license") val license: String,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("base") val base: String,
    @SerializedName("rates") val ratesList: Map<String, Double>,
)