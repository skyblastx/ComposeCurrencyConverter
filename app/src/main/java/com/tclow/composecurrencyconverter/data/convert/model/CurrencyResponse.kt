package com.tclow.composecurrencyconverter.data.convert.model

data class CurrencyResponse (
    val base: String,
    val date: String,
    val rates: Map<String, Double>,
    val success: Boolean,
    val timestamp: Int
)