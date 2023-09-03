package com.tclow.composecurrencyconverter.presentation.convert.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.tclow.composecurrencyconverter.domain.convert.use_case.ConvertUseCases
import com.tclow.composecurrencyconverter.presentation.convert.ConvertEvent
import com.tclow.composecurrencyconverter.utils.Screen
import com.tclow.composecurrencyconverter.utils.navigation.CustomNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConvertViewModel @Inject constructor(
    private val navigation: CustomNavigation,
    private val convertUseCases: ConvertUseCases
): ViewModel() {

    private val _conversion = MutableStateFlow<ConvertEvent>(ConvertEvent.Empty)
    val conversion: StateFlow<ConvertEvent> = _conversion

    fun getRates() {
        viewModelScope.launch(Dispatchers.IO) {
            _conversion.value = ConvertEvent.Loading
            async { _conversion.value = convertUseCases.getRates() }.await()
        }
    }

    fun convert(
        strAmount: String,
        fromCurrency: String,
        toCurrency: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _conversion.value = convertUseCases.convertRates(strAmount, fromCurrency, toCurrency)
        }
    }
}