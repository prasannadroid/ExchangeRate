package com.sample.currencyconversion.helper

import java.io.InputStreamReader

class TestHelper {

    companion object {

        const val validUrl = "validUrl"

        const val invalidUrl = "invalidUrl"

        /**
         * Reads the contents of a json file located in the resources directory and returns it as a single json string.
         *
         * @param fileName The name of the file to be read. text formal will be filename.json
         * @return The contents of the file as a single string.
         */
        fun readFileResponse(fileName: String): String {
            val inputStream = TestHelper::class.java.getResourceAsStream(fileName)
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
            }
            return builder.toString()
        }
    }
}