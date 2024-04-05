package com.sample.currencyconversion.koin

import com.sample.currencyconversion.core.presenter.ExchangeRateViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        ExchangeRateViewModel(get())
    }
}