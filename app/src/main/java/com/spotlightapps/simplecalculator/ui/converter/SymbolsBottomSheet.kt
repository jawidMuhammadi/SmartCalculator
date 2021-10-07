package com.spotlightapps.simplecalculator.ui.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.spotlightapps.simplecalculator.adapters.SymbolsAdapter
import com.spotlightapps.simplecalculator.databinding.SymbolBottomSheetDialogBinding
import com.spotlightapps.simplecalculator.model.SymbolItem
import com.spotlightapps.simplecalculator.model.symbol.Symbols

/**
 * Created by Ahmad Jawid Muhammadi
 * on 07-10-2021.
 */

class SymbolsBottomSheet(
    private val symbols: Symbols?,
    private val onBottomSheetItemClicked: OnBottomSheetItemClicked
) : BottomSheetDialogFragment() {

    private lateinit var binding: SymbolBottomSheetDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SymbolBottomSheetDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSymbols.adapter = SymbolsAdapter().also {
            if (symbols != null) {
                it.submitList(symbols)
            }
            it.itemClickListener = { item ->
                onBottomSheetItemClicked.onItemClicked(item)
                dismiss()
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        var TAG: String = SymbolsBottomSheet::class.java.simpleName.toString()
    }

    interface OnBottomSheetItemClicked {
        fun onItemClicked(item: SymbolItem)
    }
}