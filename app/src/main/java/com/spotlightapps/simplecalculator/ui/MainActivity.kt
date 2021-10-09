package com.spotlightapps.simplecalculator.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.spotlightapps.simplecalculator.R
import com.spotlightapps.simplecalculator.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        navController = findNavController(R.id.nav_host_fragment)

        supportActionBar?.title = getString(R.string.calculator)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.calculatorFragment -> {
                if (navController.currentDestination?.id != R.id.calculatorFragment) {
                    navController.popBackStack()
                    navController.navigate(R.id.calculatorFragment)
                    supportActionBar?.title = getString(R.string.calculator)
                }
                true
            }
            R.id.exchangeFragment -> {
                if (navController.currentDestination?.id != R.id.exchangeFragment) {
                    navController.popBackStack()
                    navController.navigate(R.id.exchangeFragment)
                    supportActionBar?.title = getString(R.string.converter)
                }
                true
            }
            else -> onNavigateUp()
        }
    }
}