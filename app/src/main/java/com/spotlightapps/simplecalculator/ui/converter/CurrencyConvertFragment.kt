package com.spotlightapps.simplecalculator.ui.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.spotlightapps.simplecalculator.databinding.FragmentCurrencyConverterBinding
import com.spotlightapps.simplecalculator.model.SymbolItem
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

        initObservers()
        viewModel.getSymbolList()

        binding.cvFrom.setOnClickListener {
            displayBottomSheetDialog(true)
        }

        binding.cvTo.setOnClickListener {
            displayBottomSheetDialog(false)
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