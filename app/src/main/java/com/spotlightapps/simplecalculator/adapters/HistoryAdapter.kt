package com.spotlightapps.simplecalculator.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spotlightapps.simplecalculator.databinding.ItemHistoryBinding
import com.spotlightapps.simplecalculator.model.HistoryItem
import java.util.*
import javax.inject.Inject

/**
 * Created by Ahmad Jawid Muhammadi
 * on 09-10-2021.
 */

class HistoryAdapter @Inject constructor() : RecyclerView.Adapter<HistoryAdapterVH>() {

    private var historyList: MutableList<HistoryItem> = LinkedList()

    internal var itemClickListener: (HistoryItem) -> Unit = { _ -> }

    fun addNewItem(item: HistoryItem) {
        historyList.add(0, item)
        notifyDataSetChanged()
    }

    fun clearHistory() {
        historyList = LinkedList()
        notifyDataSetChanged()
    }

    fun getHistoryListSiz(): Int = historyList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapterVH {
        return HistoryAdapterVH.from(parent)
    }

    override fun onBindViewHolder(holder: HistoryAdapterVH, position: Int) {
        holder.apply {
            bind(historyList[position])
            itemView.setOnClickListener {
                itemClickListener(historyList[position])
            }
        }
    }

    override fun getItemCount(): Int = historyList.size
}

class HistoryAdapterVH private constructor(
    private val binding: ItemHistoryBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): HistoryAdapterVH {
            val layoutInflater = LayoutInflater.from(parent.context)
            return HistoryAdapterVH(
                ItemHistoryBinding.inflate(layoutInflater, parent, false)
            )
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: HistoryItem) {
        with(binding) {
            tvExpression.text = item.expression
            tvResult.text = "= ${item.result}"
        }
    }
}