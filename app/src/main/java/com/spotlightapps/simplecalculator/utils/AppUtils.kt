package com.spotlightapps.simplecalculator.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
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
