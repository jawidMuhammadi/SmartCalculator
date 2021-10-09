package com.spotlightapps.simplecalculator.ui.calculator

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.spotlightapps.simplecalculator.R
import com.spotlightapps.simplecalculator.adapters.HistoryAdapter
import com.spotlightapps.simplecalculator.databinding.FragmentCalculatorLayoutBinding
import com.spotlightapps.simplecalculator.ui.views.CustomNumericKeyboard
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CalculatorFragment : Fragment() {

    private lateinit var binding: FragmentCalculatorLayoutBinding
    private val viewModel: CalculatorViewModel by viewModels()

    @Inject
    lateinit var historyAdapter: HistoryAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalculatorLayoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvHistory.adapter = historyAdapter

        binding.customNumericKeyboard.setOnKeyClickListener(object :
            CustomNumericKeyboard.OnCustomNumericKeyboardClickListener {
            override fun onKeyClicked(value: String) {
                if (viewModel.isEqualButtonClicked) {
                    changeTextViewAttributesToDefault()
                }
                viewModel.addNewValue(value)
            }

            private fun changeTextViewAttributesToDefault() {
                binding.apply {
                    tvExpression.textSize = 48f
                    tvExpression.setTextColor(
                        ResourcesCompat.getColor(resources, R.color.black, null)
                    )

                    tvResult.textSize = 26f
                    tvResult.setTextColor(
                        ResourcesCompat.getColor(
                            resources, R.color.grey_700, null
                        )
                    )
                }
            }
        })

        initObservers()

        setClickListeners()
    }

    private fun initObservers() {
        viewModel.result.observe(viewLifecycleOwner, {
            binding.tvResult.apply {
                text = if (!it.isNullOrEmpty()) {
                    getString(R.string.equal_place_holder, it)
                } else "0"
            }
        })

        viewModel.expression.observe(viewLifecycleOwner, {
            binding.tvExpression.text = it
        })

        viewModel.historyItem.observe(viewLifecycleOwner, {
            historyAdapter.addNewItem(it)
            //Scroll to latest added history
            binding.rvHistory.scrollToPosition(0)
        })
    }

    private fun setClickListeners() {
        binding.apply {
            tvAdd.setOnClickListener {
                viewModel.addOperatorOnExpression(tvAdd.text.toString(), OperatorType.ADD)
            }
            tvMinus.setOnClickListener {
                viewModel.addOperatorOnExpression(tvMinus.text.toString(), OperatorType.MINUS)
            }
            tvMultiply.setOnClickListener {
                viewModel.addOperatorOnExpression(tvMultiply.text.toString(), OperatorType.MULTIPLY)
            }
            tvDivide.setOnClickListener {
                viewModel.addOperatorOnExpression(tvDivide.text.toString(), OperatorType.DIVIDE)
            }
            tvPercent.setOnClickListener {
                viewModel.addOperatorOnExpression(tvPercent.text.toString(), OperatorType.PERCENT)
            }
            ivBackSpace.setOnClickListener {
                viewModel.onBackspaceClicked()
            }
            tvEqual.setOnClickListener {
                viewModel.isEqualButtonClicked = true
                binding.apply {
                    tvExpression.textSize = 26f
                    tvExpression.setTextColor(
                        ResourcesCompat.getColor(resources, R.color.grey_700, null)
                    )
                    tvResult.setTextColor(
                        ResourcesCompat.getColor(resources, R.color.black, null)
                    )
                }
                animateResultTextView()
            }
            tvClear.setOnClickListener {
                viewModel.clearAllData()
            }
        }
    }

    private fun animateResultTextView() {
        val animator: ValueAnimator = ObjectAnimator.ofFloat(
            binding.tvResult,
            "textSize",
            26F, 48f
        ).apply {
            duration = 300L
        }
        animator.start()
    }
}