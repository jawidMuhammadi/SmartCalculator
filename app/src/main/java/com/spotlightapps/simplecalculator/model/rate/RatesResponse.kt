package com.spotlightapps.simplecalculator.model.rate


import com.google.gson.annotations.SerializedName
import com.spotlightapps.simplecalculator.model.rate.Rates

data class RatesResponse(
    @SerializedName("base")
    var base: String?,
    @SerializedName("date")
    var date: String?,
    @SerializedName("rates")
    var rates: Rates?,
    @SerializedName("success")
    var success: Boolean?,
    @SerializedName("timestamp")
    var timestamp: Int?
)