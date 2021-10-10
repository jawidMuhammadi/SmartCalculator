package com.spotlightapps.simplecalculator.ui.calculator

/**
 * Created by Ahmad Jawid Muhammadi
 * on 06-10-2021.
 */

enum class OperatorType constructor(val sign: String) {
    DIVIDE("÷"),
    ADD("+"),
    MINUS("−"),
    PERCENT("%"),
    MULTIPLY("×")
}