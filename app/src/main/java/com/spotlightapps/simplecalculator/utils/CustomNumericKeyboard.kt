package com.spotlightapps.simplecalculator.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.spotlightapps.simplecalculator.databinding.CustomKeyboardLayoutBinding
import com.spotlightapps.simplecalculator.databinding.CustomNumericKeyboardItemBinding

/**
 * Created by Ahmad Jawid Muhammadi
 * on 06-10-2021.
 */

class CustomNumericKeyboard constructor(
    context: Context,
    attrs: AttributeSet
) : RelativeLayout(context, attrs) {

    private val adapter = CustomNumericKeyboardAdapter()
    private var binding: CustomKeyboardLayoutBinding = CustomKeyboardLayoutBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    init {
        binding.apply {
            adapter.itemClickListener = { clickedKey ->
                Toast.makeText(context, "Clicked Key: $clickedKey", Toast.LENGTH_SHORT).show()
            }
            rvKeyboard.adapter = adapter
        }
    }


    internal class CustomNumericKeyboardAdapter : RecyclerView.Adapter<CustomNumericKeyboardVH>() {

        private val numberList = listOf(
            "7", "8", "9", "4", "5", "6", "1", "2", "3", "0", "00", "."
        )

        internal var itemClickListener: (String) -> Unit = { _ -> }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomNumericKeyboardVH {
            return CustomNumericKeyboardVH.from(parent)
        }

        override fun onBindViewHolder(holder: CustomNumericKeyboardVH, position: Int) {
            holder.apply {
                bind(numberList[position])
                itemView.setOnClickListener {
                    itemClickListener(numberList[position])
                }
            }
        }

        override fun getItemCount(): Int = numberList.size
    }

    internal class CustomNumericKeyboardVH private constructor(
        private val binding: CustomNumericKeyboardItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): CustomNumericKeyboardVH {
                val layoutInflater = LayoutInflater.from(parent.context)
                return CustomNumericKeyboardVH(
                    CustomNumericKeyboardItemBinding.inflate(layoutInflater, parent, false)
                )
            }
        }

        fun bind(item: String) {
            binding.tvNumber.text = item
        }
    }
}