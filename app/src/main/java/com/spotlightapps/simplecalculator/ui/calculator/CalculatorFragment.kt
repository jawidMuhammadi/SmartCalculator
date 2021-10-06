package com.spotlightapps.simplecalculator.ui.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.spotlightapps.simplecalculator.databinding.FragmentCalculatorBinding
import com.spotlightapps.simplecalculator.utils.CustomNumericKeyboard
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
                Toast.makeText(context, "Clicked Key $value", Toast.LENGTH_SHORT).show()
            }
        })
    }
}