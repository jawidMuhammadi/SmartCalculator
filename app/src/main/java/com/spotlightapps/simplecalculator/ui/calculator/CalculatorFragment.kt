package com.spotlightapps.simplecalculator.ui.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.spotlightapps.simplecalculator.databinding.FragmentCalculatorBinding
import com.spotlightapps.simplecalculator.utils.CustomNumericKeyboard
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CalculatorFragment : Fragment() {

    private lateinit var binding: FragmentCalculatorBinding
    private val vieModel: CalculatorViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalculatorBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.customNumericKeyboard.setOnKeyClickListener(object :
            CustomNumericKeyboard.OnCustomNumericKeyboardClickListener {
            override fun onKeyClicked(value: String) {
                vieModel.addNewValue(value)
            }
        })

        vieModel.result.observe(viewLifecycleOwner, {
            binding.consoleLayout.tvResult.text = "= $it"
        })

        vieModel.expression.observe(viewLifecycleOwner, {
            binding.consoleLayout.tvOperation.text = it
        })

        setClickListeners()
    }

    private fun setClickListeners() {
        binding.apply {
            tvAdd.setOnClickListener {
                vieModel.addSignOExpression(tvAdd.text.toString(), OperationType.ADD)
            }
            tvMinus.setOnClickListener {
                vieModel.addSignOExpression(tvMinus.text.toString(), OperationType.MINUS)
            }
            tvMultiply.setOnClickListener {
                vieModel.addSignOExpression(tvMultiply.text.toString(), OperationType.MULTIPLY)
            }
            tvDivide.setOnClickListener {
                vieModel.addSignOExpression(tvDivide.text.toString(), OperationType.DIVIDE)
            }
            tvPercent.setOnClickListener {
                vieModel.addSignOExpression(tvPercent.text.toString(), OperationType.PERCENT)
            }

            ivBackSpace.setOnClickListener {
                vieModel.addSignOExpression(null, OperationType.BACKSPACE)
            }
        }
    }
}