package com.spotlightapps.simplecalculator.utils

import com.google.gson.Gson
import com.spotlightapps.simplecalculator.model.SymbolItem


/**
 * Created by Ahmad Jawid Muhammadi
 * on 07-10-2021.
 */

fun getSymbolItemListFromJsonObject(json: String): List<SymbolItem> {
    val map = Gson().fromJson<Map<String, String>>(
        json,
        MutableMap::class.java
    )
    val symbolList: MutableList<SymbolItem> = ArrayList()
    for ((key, value) in map) {
        symbolList.add(SymbolItem(currencySymbol = key, countryName = value))
    }
    return symbolList
}

fun getRatesInMapFromJsonObject(json: String): Map<String, Double> {
    return Gson().fromJson<Map<String, Double>>(
        json,
        MutableMap::class.java
    )
}

fun calculateExchangeToRateValue(
    fromRate: Double,
    toRate: Double,
    fromAmount: Double
): Double {
    val amountEuro = fromAmount.div(fromRate)
    return amountEuro.times(toRate)
}

fun isValueOdd(value: String): Boolean {
    val oddNumber = listOf("1", "3", "5", "7", "9")
    return oddNumber.contains(value[value.length-1])
}
