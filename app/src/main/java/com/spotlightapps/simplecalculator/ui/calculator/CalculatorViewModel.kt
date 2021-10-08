package com.spotlightapps.simplecalculator.ui.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    private var operatorsList: MutableList<OperatorType> = LinkedList()
    private var resultList: MutableList<String> = LinkedList()

    private var _result = MutableLiveData<String?>()
    val result: LiveData<String?> = _result

    private var _expression = MutableLiveData("")
    val expression: LiveData<String> = _expression

    private var currentOperand: String = ""

    private var leftOperand: String = ""

    private var latestOperator: OperatorType? = null

    fun addNewValue(value: String) {
        _expression.value += value
        currentOperand += value
        if (latestOperator != null) {
            performOperation(latestOperator!!)
        } else {
            _result.value = currentOperand
        }
    }

    fun addOperatorOnExpression(operatorSign: String?, operatorType: OperatorType) {
        _expression.value += operatorSign
        operatorsList.add(operatorType)
        latestOperator = operatorType
        leftOperand = result.value!!
        resultList.add(leftOperand)
        operandList.add(currentOperand)
        currentOperand = ""

    }

    private fun performOperation(operatorType: OperatorType) {
        when (operatorType) {
            OperatorType.PERCENT -> {
                _result.value = leftOperand.toInt().rem(currentOperand.toInt()).toString()
            }
            OperatorType.DIVIDE -> {
                _result.value = leftOperand.toInt().div(currentOperand.toInt()).toString()
            }
            OperatorType.ADD -> {
                _result.value = currentOperand.toInt().plus(leftOperand.toInt()).toString()
            }
            OperatorType.MINUS -> {
                _result.value = leftOperand.toInt().minus(currentOperand.toInt()).toString()
            }
            OperatorType.MULTIPLY -> {
                _result.value = leftOperand.toInt().times(currentOperand.toInt()).toString()
            }
        }
    }

    fun onBackspaceClicked() {
        if (currentOperand.isNotEmpty()) {
            currentOperand = currentOperand.removeRange(
                currentOperand.length - 1,
                currentOperand.length
            )
            if (currentOperand.isNotEmpty() && latestOperator != null) {
                leftOperand = resultList[resultList.lastIndex]
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

            if (operatorsList.isNotEmpty()) {
                operatorsList.removeLast()
            }
            latestOperator = if (operatorsList.isNotEmpty()) {
                operatorsList[operatorsList.lastIndex]
            } else null
        }
    }
}