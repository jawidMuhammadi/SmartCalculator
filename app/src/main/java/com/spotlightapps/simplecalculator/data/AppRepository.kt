package com.spotlightapps.simplecalculator.data

import com.spotlightapps.simplecalculator.data.remote.ApiManager
import com.spotlightapps.simplecalculator.model.rate.RatesResponse
import com.spotlightapps.simplecalculator.model.symbol.SymbolsResponse
import kotlinx.coroutines.Deferred
import javax.inject.Inject

/**
 * Created by Ahmad Jawid Muhammadi
 * on 10-10-2021.
 */

class AppRepository @Inject constructor(
    private val apiManager: ApiManager
) {

    fun getExchangeRatesAsync(isToForceRemote: Boolean = true): Deferred<RatesResponse?> {
        return if (isToForceRemote) {
            apiManager.currencyRateService.getExchangeRatesAsync()
        } else TODO("implement offline feature logic")
    }

    fun getCountrySymbolsAsync(isToForceRemote: Boolean = true): Deferred<SymbolsResponse?> {
        return if (isToForceRemote) {
            apiManager.currencyRateService.getCountrySymbolsAsync()
        } else TODO("implement offline feature logic")
    }
}