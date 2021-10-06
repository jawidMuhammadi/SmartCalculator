package com.spotlightapps.simplecalculator

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Ahmad Jawid Muhammadi
 * on 06-10-2021.
 */

@HiltAndroidApp
class CalculatorApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}