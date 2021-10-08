package com.spotlightapps.simplecalculator.network

import com.spotlightapps.simplecalculator.model.rate.RatesResponse
import com.spotlightapps.simplecalculator.model.symbol.SymbolsResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

/**
 * Created by Ahmad Jawid Muhammadi
 * on 07-10-2021.
 */

interface CurrencyRateService {

    @GET(ENDPOINT_LATEST)
    fun getExchangeRatesAsync(): Deferred<RatesResponse?>

    @GET(ENDPOINT_SYMBOLS)
    fun getCountrySymbolsAsync(): Deferred<SymbolsResponse?>

}