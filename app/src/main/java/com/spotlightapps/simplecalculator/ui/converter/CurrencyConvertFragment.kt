package com.spotlightapps.simplecalculator.ui.converter

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.spotlightapps.simplecalculator.R
import com.spotlightapps.simplecalculator.databinding.FragmentCurrencyConverterBinding
import com.spotlightapps.simplecalculator.model.SymbolItem
import com.spotlightapps.simplecalculator.utils.CustomNumericKeyboard
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CurrencyConvertFragment : Fragment() {

    private lateinit var binding: FragmentCurrencyConverterBinding
    private val viewModel: CurrencyConverterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrencyConverterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        initObservers()
        viewModel.getSymbolList()
        // viewModel.getRatesList()

        binding.apply {
            cvFrom.setOnClickListener {
                displayBottomSheetDialog(true)
            }
            cvTo.setOnClickListener {
                displayBottomSheetDialog(false)
            }
            customNumericKeyboard2.setOnKeyClickListener(object :
                CustomNumericKeyboard.OnCustomNumericKeyboardClickListener {
                override fun onKeyClicked(value: String) {
                    if (etFrom.hasFocus()) {
                        val currentValue = etFrom.text.toString()
                        val newValue = currentValue + value
                        etFrom.setText(newValue)
                    }
                    if (etTo.hasFocus()) {
                        val currentValue = etTo.text.toString()
                        val newValue = currentValue + value
                        etTo.setText(newValue)
                    }
                }
            })
            btnBackspace.setOnClickListener {
                if (etFrom.hasFocus()) {
                    val currentValue = etFrom.text.toString()
                    if (currentValue.isNotEmpty()) {
                        val newValue = currentValue.removeRange(
                            currentValue.length - 1, currentValue.length
                        )
                        etFrom.setText(newValue)
                    }
                }
                if (etTo.hasFocus()) {
                    val currentValue = etTo.text.toString()
                    if (currentValue.isNotEmpty()) {
                        val newValue = currentValue.removeRange(
                            currentValue.length - 1, currentValue.length
                        )
                        etTo.setText(newValue)
                    }
                }
            }
            btnClear.setOnClickListener {
                etTo.setText("")
                etFrom.setText("")
            }
        }
        setTouchListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListeners() {
        binding.etFrom.setOnTouchListener { v, event ->
            binding.etTo.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.edit_text_border,
                null
            )
            binding.etFrom.let {
                val inType: Int = it.inputType
                it.inputType = InputType.TYPE_NULL
                it.onTouchEvent(event)
                it.inputType = inType
                it.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.edit_text_active_border,
                    null
                )
            }
            true
        }

        binding.etTo.setOnTouchListener { v, event ->
            binding.etFrom.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.edit_text_border,
                null
            )
            binding.etTo.let {
                val inType: Int = it.inputType
                it.inputType = InputType.TYPE_NULL
                it.onTouchEvent(event)
                it.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.edit_text_active_border,
                    null
                )
                it.inputType = inType
            }
            true
        }
    }

    private fun initObservers() {
        viewModel.exchangeRate.observe(viewLifecycleOwner, {
            Toast.makeText(context, "base: ${it?.base}", Toast.LENGTH_SHORT).show()
        })
        viewModel.selectedFromRateSymbolItem.observe(viewLifecycleOwner, {
            binding.apply {
                tvFromCountryName.text = it?.countryName
                tvFromSymbol.text = it?.currencySymbol
                // viewModel.calculateFromToValue(binding.etFrom.text.toString().toDouble())
            }
        })
        viewModel.selectedToRateSymbolItem.observe(viewLifecycleOwner, {
            binding.apply {
                tvToCountryName.text = it?.countryName
                tvToSymbol.text = it?.currencySymbol
            }
        })
        viewModel.toAmount.observe(viewLifecycleOwner, {
            binding.etTo.setText(String.format("%.2f", it))
        })
        viewModel.fromAmount.observe(viewLifecycleOwner, {
            binding.etFrom.setText(String.format("%.2f", it))
        })
    }

    private fun displayBottomSheetDialog(isFrom: Boolean) {
        val symbolBottomSheet = SymbolsBottomSheet(
            viewModel.symbols.value,
            object : SymbolsBottomSheet.OnBottomSheetItemClicked {
                override fun onItemClicked(item: SymbolItem) {
                    viewModel.onSymbolItemSelected(item, isFrom)
                }

            })
        symbolBottomSheet.show(childFragmentManager, SymbolsBottomSheet.TAG)
    }
}