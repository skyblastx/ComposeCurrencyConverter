package com.tclow.composecurrencyconverter.data.convert.repository

import com.tclow.composecurrencyconverter.data.convert.CurrencyApi
import com.tclow.composecurrencyconverter.data.convert.model.CurrencyResponse
import com.tclow.composecurrencyconverter.domain.convert.repository.CurrencyRepository
import com.tclow.composecurrencyconverter.utils.Resource
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val api: CurrencyApi
) : CurrencyRepository {
    override suspend fun getRates(): Resource<CurrencyResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getRates(base: String): Resource<CurrencyResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getHistoricalRates(date: String): Resource<CurrencyResponse> {
        TODO("Not yet implemented")
    }
}