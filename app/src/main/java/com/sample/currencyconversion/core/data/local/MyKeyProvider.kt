package com.sample.currencyconversion.core.data.local

import android.content.Context
import com.sample.currencyconversion.util.Const
import java.util.Properties

class MyKeyProvider(private val context: Context) {

    fun readFromPropertiesFile(): String {
        val properties = Properties()
        val assetManager = context.assets

        // read from asset folder
        val inputStream = assetManager.open("secret.properties")
        properties.load(inputStream)

        // read key name and return
        return properties.getProperty(Const.API_KEY)
    }
}