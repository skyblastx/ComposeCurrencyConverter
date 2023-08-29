package com.tclow.composecurrencyconverter.presentation.convert

sealed class ConvertEvent {
    class Success(val resultMsg: String): ConvertEvent()
    class Failure(val errorMsg: String): ConvertEvent()
    object Loading: ConvertEvent()
    object Empty: ConvertEvent()
}
