package com.sample.currencyconversion.koin

import com.sample.currencyconversion.core.data.repository.ExchangeRatesRepository
import com.sample.currencyconversion.core.data.repository.ExchangeRatesRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<ExchangeRatesRepository> {
        ExchangeRatesRepositoryImpl(get(), get())
    }
}