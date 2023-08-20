package com.tclow.composecurrencyconverter.data.repository

import com.tclow.composecurrencyconverter.data.model.CurrencyResponse
import com.tclow.composecurrencyconverter.domain.repository.CurrencyRepository
import com.tclow.composecurrencyconverter.utils.Resource

class CurrencyRepositoryImpl: CurrencyRepository {
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