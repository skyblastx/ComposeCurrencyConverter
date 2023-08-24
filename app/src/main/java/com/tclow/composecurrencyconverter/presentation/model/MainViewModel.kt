package com.tclow.composecurrencyconverter.presentation.model

import androidx.lifecycle.ViewModel
import com.tclow.composecurrencyconverter.utils.navigation.CustomNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    customNavigation: CustomNavigation
): ViewModel() {
    val navigationChannel = customNavigation.navigationChannel
}