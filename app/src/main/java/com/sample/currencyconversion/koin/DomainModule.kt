package com.sample.currencyconversion.koin

import com.sample.currencyconversion.core.domain.ExchangeRateUserCase
import org.koin.dsl.module

val domainModule = module {
    single {
        ExchangeRateUserCase(get())
    }
}