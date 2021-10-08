package com.spotlightapps.simplecalculator.model

/**
 * Created by Ahmad Jawid Muhammadi
 * on 08-10-2021.
 */

data class RateSymbolItem(
    var currencySymbol: String? = null,
    var countryName: String? = null,
    var rate: Double? = null,
    var base: Double? = null
)