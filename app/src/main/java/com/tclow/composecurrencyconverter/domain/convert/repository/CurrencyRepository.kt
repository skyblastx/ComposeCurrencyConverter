package com.tclow.composecurrencyconverter.domain.convert.repository

import com.tclow.composecurrencyconverter.data.convert.model.CurrencyResponse
import com.tclow.composecurrencyconverter.data.database.dao.CurrencyRate
import com.tclow.composecurrencyconverter.utils.Resource

interface CurrencyRepository {

    //===============================
    // Api functions
    //===============================
    suspend fun getRates(): Resource<CurrencyResponse>
    suspend fun getRates(base: String): Resource<CurrencyResponse>
    suspend fun getHistoricalRates(date: String): Resource<CurrencyResponse>

    //===============================
    // Database functions
    //===============================
    suspend fun insertRates(rate: CurrencyRate)

    suspend fun getRatesWithDate(date: String): List<CurrencyRate>

    suspend fun getMissingDates(): List<String>
}