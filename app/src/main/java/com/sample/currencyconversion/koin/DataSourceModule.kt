package com.sample.currencyconversion.koin

import com.sample.currencyconversion.core.data.datasource.ExchangeRatesDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    single {
        ExchangeRatesDataSource(get())
    }
}