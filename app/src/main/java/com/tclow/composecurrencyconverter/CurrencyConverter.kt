package com.tclow.composecurrencyconverter

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// For hilt to provide application context
@HiltAndroidApp
class CurrencyConverter: Application()