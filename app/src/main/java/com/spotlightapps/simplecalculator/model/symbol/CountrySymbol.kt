package com.spotlightapps.simplecalculator.model.symbol


import com.google.gson.annotations.SerializedName

data class CountrySymbol(
    @SerializedName("success")
    var success: Boolean?,
    @SerializedName("symbols")
    var symbols: Symbols?
)