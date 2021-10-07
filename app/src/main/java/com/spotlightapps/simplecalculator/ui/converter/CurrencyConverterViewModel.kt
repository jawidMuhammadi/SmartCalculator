package com.spotlightapps.simplecalculator.ui.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spotlightapps.simplecalculator.model.symbol.Symbols
import com.spotlightapps.simplecalculator.network.BaseApiManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Ahmad Jawid Muhammadi
 * on 07-10-2021.
 */

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val baseApiManager: BaseApiManager
) : ViewModel() {

    private var _symbols = MutableLiveData<Symbols?>()
    val symbols: LiveData<Symbols?> = _symbols

    fun getSymbolList() {
        viewModelScope.launch {
            try {
                val list = baseApiManager.currencyRateService.getCountrySymbolsAsync().await()
                _symbols.value = list?.symbols
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}