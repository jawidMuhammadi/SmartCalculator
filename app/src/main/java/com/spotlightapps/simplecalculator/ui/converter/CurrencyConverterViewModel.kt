package com.spotlightapps.simplecalculator.ui.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.spotlightapps.simplecalculator.data.AppRepository
import com.spotlightapps.simplecalculator.model.RateSymbolItem
import com.spotlightapps.simplecalculator.model.SymbolItem
import com.spotlightapps.simplecalculator.model.symbol.Symbols
import com.spotlightapps.simplecalculator.data.remote.ApiCallStatus
import com.spotlightapps.simplecalculator.utils.calculateExchangeToRateValue
import com.spotlightapps.simplecalculator.utils.getRatesInMapFromJsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Ahmad Jawid Muhammadi
 * on 07-10-2021.
 */

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    private var _symbols = MutableLiveData<Symbols?>()
    val symbols: LiveData<Symbols?> = _symbols

    private var ratesMap: MutableMap<String, Double> = hashMapOf()

    private var _selectedFromRateItem = MutableLiveData<RateSymbolItem?>()
    val selectedFromRateSymbolItem: LiveData<RateSymbolItem?> = _selectedFromRateItem

    private var _selectedToRateItem = MutableLiveData<RateSymbolItem?>()
    val selectedToRateSymbolItem: LiveData<RateSymbolItem?> = _selectedToRateItem

    private var _fromAmount = MutableLiveData<String>()
    val fromAmount: LiveData<String> = _fromAmount

    private var _toAmount = MutableLiveData<String>()
    val toAmount: LiveData<String> = _toAmount

    private var _apiCallStatus = MutableLiveData<ApiCallStatus>()
    val apiCallStatus: LiveData<ApiCallStatus> = _apiCallStatus

    fun getSymbolList() {
        viewModelScope.launch {
            _apiCallStatus.value = ApiCallStatus.PROGRESS
            try {
                val symbolsResponse = appRepository.getCountrySymbolsAsync().await()
                _symbols.value = symbolsResponse?.symbols
                getRatesList()
            } catch (e: Exception) {
                e.printStackTrace()
                _apiCallStatus.value = ApiCallStatus.FAILED
            }
        }
    }

    private fun getRatesList() {
        viewModelScope.launch {
            try {
                val rateResponse = appRepository.getExchangeRatesAsync().await()
                ratesMap = getRatesInMapFromJsonObject(
                    Gson().toJson(rateResponse?.rates)
                ).toMutableMap()
                selectDefaultRates()
                _apiCallStatus.value = ApiCallStatus.SUCCESS
            } catch (e: Exception) {
                e.printStackTrace()
                _apiCallStatus.value = ApiCallStatus.FAILED
            }
        }
    }

    private fun selectDefaultRates() {
        _selectedFromRateItem.value = RateSymbolItem(
            currencySymbol = "USD",
            rate = ratesMap["USD"],
            countryName = "United States"
        )
        _selectedToRateItem.value = RateSymbolItem(
            currencySymbol = "AFN",
            rate = ratesMap["AFN"],
            countryName = "Afghanistan"
        )
        calculateFromToValue("1")
        _fromAmount.value = "1"
    }

    fun onSymbolItemSelected(symbolItem: SymbolItem, isFromItem: Boolean) {
        if (isFromItem) {
            _selectedFromRateItem.value = RateSymbolItem(
                currencySymbol = symbolItem.currencySymbol,
                rate = ratesMap[symbolItem.currencySymbol],
                countryName = symbolItem.countryName
            )
        } else {
            _selectedToRateItem.value = RateSymbolItem(
                currencySymbol = symbolItem.currencySymbol,
                rate = ratesMap[symbolItem.currencySymbol],
                countryName = symbolItem.countryName
            )
        }
        _fromAmount.value = "1"
        calculateFromToValue(_fromAmount.value!!)
    }

    fun calculateFromToValue(newValue: String) {
        _toAmount.value = calculateExchangeToRateValue(
            fromRate = _selectedFromRateItem.value?.rate!!,
            toRate = _selectedToRateItem.value?.rate!!,
            fromAmount = newValue.toDouble()
        ).toString()
    }

    fun calculateToFromValue(newValue: Double) {
        _fromAmount.value = calculateExchangeToRateValue(
            fromRate = _selectedToRateItem.value?.rate!!,
            toRate = _selectedFromRateItem.value?.rate!!,
            fromAmount = newValue
        ).toString()
    }
}