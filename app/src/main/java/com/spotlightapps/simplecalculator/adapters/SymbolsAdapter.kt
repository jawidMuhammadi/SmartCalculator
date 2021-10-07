package com.spotlightapps.simplecalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.spotlightapps.simplecalculator.databinding.SymbolsItemBinding
import com.spotlightapps.simplecalculator.model.SymbolItem
import com.spotlightapps.simplecalculator.model.symbol.Symbols
import com.spotlightapps.simplecalculator.utils.getSymbolItemListFromJsonObject


/**
 * Created by Ahmad Jawid Muhammadi
 * on 07-10-2021.
 */


class SymbolsAdapter : RecyclerView.Adapter<SymbolsViewHolder>() {

    private var symbolList: MutableList<SymbolItem> = ArrayList()

    internal var itemClickListener: (SymbolItem) -> Unit = { _ -> }

    fun submitList(symbols: Symbols) {
        val list = getSymbolItemListFromJsonObject(Gson().toJson(symbols))
        symbolList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymbolsViewHolder {
        return SymbolsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SymbolsViewHolder, position: Int) {
        holder.apply {
            bind(symbolList[position])
            itemView.setOnClickListener {
                itemClickListener(symbolList[position])
            }
        }
    }

    override fun getItemCount(): Int = symbolList.size
}

class SymbolsViewHolder private constructor(
    private val binding: SymbolsItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): SymbolsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            return SymbolsViewHolder(
                SymbolsItemBinding.inflate(layoutInflater, parent, false)
            )
        }
    }

    fun bind(item: SymbolItem) {
        binding.tvSymbolName.text = "${item.countryName} (${item.currencySymbol})"
    }
}