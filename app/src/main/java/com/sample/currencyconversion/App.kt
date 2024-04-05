package com.sample.currencyconversion

import android.app.Application
import com.sample.currencyconversion.koin.appModule
import com.sample.currencyconversion.koin.dataSourceModule
import com.sample.currencyconversion.koin.domainModule
import com.sample.currencyconversion.koin.jobModule
import com.sample.currencyconversion.koin.repositoryModule
import com.sample.currencyconversion.koin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                viewModelModule,
                domainModule,
                repositoryModule,
                dataSourceModule,
                appModule,
                jobModule
            )
        }
    }
}