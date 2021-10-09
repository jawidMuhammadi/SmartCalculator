package com.spotlightapps.simplecalculator.model

import com.spotlightapps.simplecalculator.ui.calculator.OperatorType

/**
 * Created by Ahmad Jawid Muhammadi
 * on 09-10-2021.
 */

data class HistoryItem(
    var expression: String? = null,
    var result: String? = null,
    var operatorList: List<OperatorType>? = null,
    var operandList: List<String>? = null,
    var resultList: List<String>? = null
)