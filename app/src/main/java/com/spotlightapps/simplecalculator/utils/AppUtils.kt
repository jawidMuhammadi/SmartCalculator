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