package com.spotlightapps.simplecalculator.ui.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spotlightapps.simplecalculator.model.HistoryItem
import com.spotlightapps.simplecalculator.utils.isValueOdd
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

/**
 * Created by Ahmad Jawid Muhammadi
 * on 06-10-2021.
 */

@HiltViewModel
class CalculatorViewModel @Inject constructor() : ViewModel() {

    private var operandList: MutableList<String> = LinkedList()
    private var operatorList: MutableList<OperatorType> = LinkedList()
    private var resultList: MutableList<String> = LinkedList()

    private var _result = MutableLiveData<String?>()
    val result: LiveData<String?> = _result

    private var _expression = MutableLiveData("")
    val expression: LiveData<String> = _expression

    private var _historyItem = MutableLiveData<HistoryItem>()
    val historyItem: LiveData<HistoryItem> = _historyItem

    private var currentOperand: String = ""

    private var leftOperand: String = ""

    private var latestOperator: OperatorType? = null

    private var isAllowEnteringOperatorSign = false

    var isEqualButtonClicked = false

    fun addNewValue(value: String) {
        if (isEqualButtonClicked && !_expression.value.isNullOrEmpty()) {
            addExpressionToHistory()
        }
        if (value == ".") {
            if (currentOperand.isNotEmpty() && !currentOperand.contains(".", true)) {
                _expression.value += value
                currentOperand += value
            }
        } else {
            _expression.value += value
            currentOperand += value
            if (latestOperator != null) {
                performOperation(latestOperator!!)
            } else {
                _result.value = currentOperand
            }
            isAllowEnteringOperatorSign = true
        }
    }

    fun addOperatorOnExpression(operatorSign: String?, operatorType: OperatorType) {
        if (isAllowEnteringOperatorSign) {
            _expression.value += operatorSign
            if (operatorSign != "%") {
                operatorList.add(operatorType)
                latestOperator = operatorType
                leftOperand = result.value!!
                resultList.add(leftOperand)
                operandList.add(currentOperand)
                currentOperand = ""
                isAllowEnteringOperatorSign = false
            } else {
                calculatePercentage()
            }
        }

    }

    private fun calculatePercentage() {
        val isExpressionDecimal = (currentOperand.contains(".") || leftOperand.contains("."))

        currentOperand = if (isExpressionDecimal ||
            currentOperand.toInt().rem(100) != 0
        ) {
            currentOperand.toDouble().div(100.0).toString()
        } else currentOperand.toInt().div(100).toString()

        if (latestOperator != null) {
            performOperation(latestOperator!!)
        } else {
            _result.value = currentOperand
        }
    }

    private fun performOperation(operatorType: OperatorType) {
        val isExpressionDecimal = (currentOperand.contains(".") || leftOperand.contains("."))
        _result.value = when (operatorType) {

            OperatorType.DIVIDE -> {
                if (isExpressionDecimal || leftOperand.toInt().rem(currentOperand.toInt()) != 0) {
                    leftOperand.toDouble().div(currentOperand.toDouble()).toString()
                } else {
                    leftOperand.toInt().div(currentOperand.toInt()).toString()
                }
            }

            OperatorType.ADD -> {
                if (isExpressionDecimal) {
                    currentOperand.toBigDecimal().plus(leftOperand.toBigDecimal()).toString()
                } else currentOperand.toInt().plus(leftOperand.toInt()).toString()
            }

            OperatorType.MINUS -> {
                if (isExpressionDecimal) {
                    leftOperand.toBigDecimal().minus(currentOperand.toBigDecimal()).toString()
                } else leftOperand.toInt().minus(currentOperand.toInt()).toString()
            }

            OperatorType.MULTIPLY -> {
                if (isExpressionDecimal) {
                    leftOperand.toBigDecimal().times(currentOperand.toBigDecimal()).toString()
                } else leftOperand.toInt().times(currentOperand.toInt()).toString()
            }
            else -> ""

        }
    }

    fun onBackspaceClicked() {
        if (currentOperand.isNotEmpty()) {
            currentOperand = currentOperand.removeRange(
                currentOperand.length - 1,
                currentOperand.length
            )
            if (currentOperand.isNotEmpty() && latestOperator != null) {
                if (resultList.isNotEmpty()) leftOperand = resultList[resultList.lastIndex]
                performOperation(latestOperator!!)
            } else {
                if (latestOperator == null) {
                    leftOperand = currentOperand
                } else {
                    resultList.removeLast()
                }
                _result.value = leftOperand
            }

            val operands = _expression.value!!
            _expression.value = operands.removeRange(
                operands.length - 1, operands.length
            )
        } else {
            _expression.value?.let {
                if (it.length > 1) {
                    _expression.value = it.removeRange(
                        it.length - 1, it.length
                    )
                } else {
                    _expression.value = ""
                }
            }
            if (operandList.isNotEmpty()) {
                currentOperand = operandList[operandList.lastIndex]
                operandList.removeLast()
            }

            if (operatorList.isNotEmpty()) {
                operatorList.removeLast()
            }
            latestOperator = if (operatorList.isNotEmpty()) {
                operatorList[operatorList.lastIndex]
            } else null
        }
    }

    private fun addExpressionToHistory() {
        _historyItem.value = HistoryItem(
            expression = _expression.value,
            result = _result.value,
            resultList = resultList,
            operandList = operandList,
            operatorList = operatorList
        )
        clearAllData()
        isEqualButtonClicked = false
    }

    fun clearAllData() {
        operandList = LinkedList()
        operatorList = LinkedList()
        resultList = LinkedList()
        _result.value = ""
        _expression.value = ""
        currentOperand = ""
        leftOperand = ""
        latestOperator = null
        isAllowEnteringOperatorSign = false
    }

    fun onHistoryItemClicked(item: HistoryItem) {
        _expression.value = item.expression
        _result.value = item.result
        operandList = item.operandList?.toMutableList()!!
        operatorList = item.operatorList?.toMutableList()!!
        resultList = item.resultList?.toMutableList()!!

        isEqualButtonClicked = false
    }
}