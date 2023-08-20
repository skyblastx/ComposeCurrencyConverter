package com.tclow.composecurrencyconverter.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// For hilt to provide application context
@HiltAndroidApp
class CurrencyConverter: Application()