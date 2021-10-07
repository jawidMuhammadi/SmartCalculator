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
        //  viewModel.getSymbolList()

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
                    if (binding.etFrom.hasFocus()) {
                        val currentValue = etFrom.text.toString()
                        val newValue = currentValue + value
                        binding.etFrom.setText(newValue)
                    }
                    if (binding.etTo.hasFocus()) {
                        val currentValue = etTo.text.toString()
                        val newValue = currentValue + value
                        binding.etTo.setText(newValue)
                    }
                }
            })
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
        viewModel.symbols.observe(viewLifecycleOwner, {
            Toast.makeText(context, "result: ${it?.aFN}", Toast.LENGTH_SHORT).show()
        })
    }

    private fun displayBottomSheetDialog(isFrom: Boolean) {
        val symbolBottomSheet = SymbolsBottomSheet(
            viewModel.symbols.value,
            object : SymbolsBottomSheet.OnBottomSheetItemClicked {
                override fun onItemClicked(item: SymbolItem) {
                    binding.apply {
                        if (isFrom) {
                            tvFromSymbol.text = item.currencySymbol
                            tvFromCountryName.text = item.countryName
                        } else {
                            tvToSymbol.text = item.currencySymbol
                            tvToCountryName.text = item.countryName
                        }
                    }
                }

            })
        symbolBottomSheet.show(childFragmentManager, SymbolsBottomSheet.TAG)
    }
}