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

        binding.customNumericKeyboard.setOnKeyClickListener(object :
            CustomNumericKeyboard.OnCustomNumericKeyboardClickListener {
            override fun onKeyClicked(value: String) {
                if (viewModel.isEqualButtonClicked) {
                    changeTextViewAttributesToDefault()
                }
                if (binding.tvClear.text == getString(R.string.all_clear)) {
                    binding.tvClear.text = getString(R.string.clear_sign)
                }

                viewModel.addNewValue(value)
            }
        })

        binding.rvHistory.adapter = historyAdapter.apply {
            itemClickListener = {
                viewModel.onHistoryItemClicked(it)
                changeTextViewAttributesToDefault()
            }
        }

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

        viewModel.errorMessage.observe(viewLifecycleOwner, {
            showErrorMessage(getString(it))
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
                if (viewModel.isErrorDisplayed) {
                    removeErrorMessage()
                    viewModel.isErrorDisplayed = false
                }
                viewModel.onBackspaceClicked()
            }
            tvEqual.setOnClickListener {
                if (!viewModel.isEqualButtonClicked) {
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
            }
            tvClear.setOnClickListener {
                if (tvClear.text == getString(R.string.clear_sign)) {
                    if (historyAdapter.getHistoryListSiz() != 0) {
                        tvClear.text = getString(R.string.all_clear)
                    }
                    viewModel.clearAllData()
                } else {
                    historyAdapter.clearHistory()
                    tvClear.text = getString(R.string.clear_sign)
                }

                if (viewModel.isErrorDisplayed) {
                    removeErrorMessage()
                    viewModel.isErrorDisplayed = false
                }
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

    private fun showErrorMessage(message: String) {
        binding.tvResult.apply {
            text = message
            setTextColor(
                ResourcesCompat.getColor(resources, android.R.color.holo_red_dark, null)
            )
        }
    }

    private fun removeErrorMessage() {
        binding.tvResult.apply {
            setTextColor(
                ResourcesCompat.getColor(resources, R.color.grey_700, null)
            )
        }
    }
}