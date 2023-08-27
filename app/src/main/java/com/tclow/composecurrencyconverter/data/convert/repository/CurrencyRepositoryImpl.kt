package com.tclow.composecurrencyconverter.data.convert.repository

import com.tclow.composecurrencyconverter.data.convert.CurrencyApi
import com.tclow.composecurrencyconverter.data.convert.model.CurrencyResponse
import com.tclow.composecurrencyconverter.domain.convert.repository.CurrencyRepository
import com.tclow.composecurrencyconverter.utils.Resource
import com.tclow.composecurrencyconverter.utils.data.ACCESS_KEY
import java.lang.Exception
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val api: CurrencyApi
) : CurrencyRepository {
    override suspend fun getRates(): Resource<CurrencyResponse> {
        return try {
            val data = getDataMapForQuery(null)
            val response = api.getRates(data)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getRates(base: String): Resource<CurrencyResponse> {
        return try {
            val data = getDataMapForQuery(base)
            val response = api.getRates(data)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getHistoricalRates(date: String): Resource<CurrencyResponse> {
        return try {
            val data = getDataMapForQuery(null)
            val response = api.getHistoricalRates(date, data)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    private fun getDataMapForQuery(base: String?): Map<String, String> {
        val data = mutableMapOf<String, String>()

        data["access_key"] = ACCESS_KEY
        if (!base.isNullOrEmpty()) { data["base"] = base }

        return data
    }
}