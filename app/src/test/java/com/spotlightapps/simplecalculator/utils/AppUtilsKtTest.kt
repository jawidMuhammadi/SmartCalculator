package com.spotlightapps.simplecalculator.utils

import junit.framework.TestCase

/**
 * Created by Ahmad Jawid Muhammadi
 * on 07-10-2021.
 */

class AppUtilsKtTest : TestCase() {

    fun testGetSymbolItemListFromJsonObject() {
        val value = getSymbolItemListFromJsonObject("{af: Afghanistan, In: India }")

        assertEquals("af", value[0].currencySymbol)
        assertEquals("Afghanistan", value[0].countryName)

        assertEquals("In", value[1].currencySymbol)
        assertEquals("India", value[1].countryName)
    }
}