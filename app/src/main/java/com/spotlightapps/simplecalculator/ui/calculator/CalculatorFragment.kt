package com.spotlightapps.simplecalculator.ui.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.spotlightapps.simplecalculator.R
import com.spotlightapps.simplecalculator.databinding.FragmentCalculatorBinding
import com.spotlightapps.simplecalculator.ui.views.CustomNumericKeyboard
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
            binding.consoleLayout.tvResult.apply {
                text = if (!it.isNullOrEmpty()) {
                    getString(R.string.equal_place_holder, it)
                } else {
                    ""
                }
            }
        })

        vieModel.expression.observe(viewLifecycleOwner, {
            binding.consoleLayout.tvOperation.text = it
        })

        setClickListeners()
    }

    private fun setClickListeners() {
        binding.apply {
            tvAdd.setOnClickListener {
                vieModel.addOperatorOnExpression(tvAdd.text.toString(), OperatorType.ADD)
            }
            tvMinus.setOnClickListener {
                vieModel.addOperatorOnExpression(tvMinus.text.toString(), OperatorType.MINUS)
            }
            tvMultiply.setOnClickListener {
                vieModel.addOperatorOnExpression(tvMultiply.text.toString(), OperatorType.MULTIPLY)
            }
            tvDivide.setOnClickListener {
                vieModel.addOperatorOnExpression(tvDivide.text.toString(), OperatorType.DIVIDE)
            }
            tvPercent.setOnClickListener {
                vieModel.addOperatorOnExpression(tvPercent.text.toString(), OperatorType.PERCENT)
            }

            ivBackSpace.setOnClickListener {
                vieModel.onBackspaceClicked()
            }
        }
    }
}