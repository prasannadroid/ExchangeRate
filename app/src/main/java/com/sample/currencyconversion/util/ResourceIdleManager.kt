package com.sample.currencyconversion.util

import androidx.test.espresso.idling.CountingIdlingResource

object ResourceIdleManager {

    private const val RESOURCE = "GLOBAL IDLING"

    @JvmField
    val countingIdleResources = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdleResources.increment()
    }

    fun decrement() {
        if (!countingIdleResources.isIdleNow) {
            countingIdleResources.decrement()
        }
    }
}