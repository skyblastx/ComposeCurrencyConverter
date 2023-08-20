package com.tclow.composecurrencyconverter.domain.repository

import com.tclow.composecurrencyconverter.data.model.CurrencyResponse
import com.tclow.composecurrencyconverter.utils.Resource

interface CurrencyRepository {
    suspend fun getRates(): Resource<CurrencyResponse>
    suspend fun getRates(base: String): Resource<CurrencyResponse>
    suspend fun getHistoricalRates(date: String): Resource<CurrencyResponse>
}