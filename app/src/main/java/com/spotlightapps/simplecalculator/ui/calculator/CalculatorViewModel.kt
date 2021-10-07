package com.spotlightapps.simplecalculator.ui.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by Ahmad Jawid Muhammadi
 * on 06-10-2021.
 */

@HiltViewModel
class CalculatorViewModel @Inject constructor() : ViewModel() {

    private var operandList: MutableList<String> = LinkedList()
    private var operatorsList: MutableList<OperationType> = LinkedList()

    private var _result = MutableLiveData<String?>()
    val result: LiveData<String?> = _result

    private var _expression = MutableLiveData("")
    val expression: LiveData<String> = _expression

    private var currentOperand: String = ""
    private var currentOperator: OperationType = OperationType.NOTING

    private var leftOperand: String = ""

    fun addNewValue(value: String) {
        var operands = _expression.value
        operands += value
        _expression.value = operands
        currentOperand += value
        if (currentOperator != OperationType.NOTING) {
            performOperation(currentOperator)
        } else {
            _result.value = currentOperand
        }
    }

    fun addSignOExpression(sign: String?, operationType: OperationType) {
        if (!sign.isNullOrEmpty()) {
            _expression.value += sign

            operatorsList.add(operationType)
            currentOperator = operatorsList[operatorsList.lastIndex]

            operandList.add(currentOperand)
            leftOperand = result.value!!
            currentOperand = ""
        } else {
            performOperation(operationType)
        }
    }

    private fun performOperation(operationType: OperationType) {
        val result = _result.value
        when (operationType) {
            OperationType.CLEAR -> TODO()
            OperationType.BACKSPACE -> {
                if (currentOperand.isNotEmpty()) {
                    if (currentOperand.length >= 2) {
                        currentOperand = currentOperand.removeRange(
                            currentOperand.length - 1,
                            currentOperand.length
                        )
                    }
                    if (currentOperand.isEmpty()) {
                        currentOperand = "0"
                        performOperation(currentOperator)
                        val operands = _expression.value!!
                        _expression.value = operands.removeRange(
                            operands.length - 1, operands.length
                        )

                        leftOperand = if (operandList.size > 2) {
                            operandList[operandList.lastIndex - 1]
                        } else {
                            "0"
                        }
                        currentOperand = operandList[operandList.lastIndex]
                        currentOperator = operatorsList[operatorsList.lastIndex]
                    } else {
                        performOperation(currentOperator)
                        val operands = _expression.value!!
                        _expression.value = operands.removeRange(
                            operands.length - 1, operands.length
                        )
                    }
                } else {

                }

            }
            OperationType.PERCENT -> {
                _result.value = leftOperand.toInt().rem(currentOperand.toInt()).toString()
            }
            OperationType.DIVIDE -> {
                _result.value = leftOperand.toInt().div(currentOperand.toInt()).toString()
            }
            OperationType.ADD -> {
                _result.value = currentOperand.toInt().plus(leftOperand.toInt()).toString()
            }
            OperationType.MINUS -> {
                _result.value = leftOperand.toInt().minus(currentOperand.toInt()).toString()
            }
            OperationType.MULTIPLY -> {
                _result.value = leftOperand.toInt().times(currentOperand.toInt()).toString()
            }
            OperationType.EQUAL -> TODO()
            OperationType.NOTING -> {
            }
        }
    }
}