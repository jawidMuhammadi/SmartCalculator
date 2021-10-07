package com.spotlightapps.simplecalculator.network

import com.spotlightapps.simplecalculator.model.rate.ExchangeRate
import com.spotlightapps.simplecalculator.model.symbol.CountrySymbol
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

/**
 * Created by Ahmad Jawid Muhammadi
 * on 07-10-2021.
 */

interface CurrencyRateService {

    @GET(ENDPOINT_LATEST)
    fun getExchangeRatesAsync(): Deferred<ExchangeRate?>

    @GET(ENDPOINT_SYMBOLS)
    fun getCountrySymbolsAsync(): Deferred<CountrySymbol?>

}