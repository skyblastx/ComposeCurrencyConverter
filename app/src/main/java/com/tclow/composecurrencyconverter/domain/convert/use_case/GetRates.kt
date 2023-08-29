package com.tclow.composecurrencyconverter.domain.convert.use_case

import com.tclow.composecurrencyconverter.data.database.dao.CurrencyRate
import com.tclow.composecurrencyconverter.domain.convert.repository.CurrencyRepository
import com.tclow.composecurrencyconverter.presentation.convert.ConvertEvent
import com.tclow.composecurrencyconverter.utils.Resource

class GetRates(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(): ConvertEvent {
        return when (val ratesResponse = repository.getRates())
        {
            is Resource.Error -> ConvertEvent.Failure(ratesResponse.message ?: "Error while getting rates")
            is Resource.Success -> {

                // Map response from api and store into database
                val rspRate = CurrencyRate().apply {
                    this.rates = ratesResponse.data?.rates
                    this.date = ratesResponse.data?.date
                    this.currencyCode = ratesResponse.data?.base
                }

                repository.insertRates(rspRate)

                ConvertEvent.Success("Success")
            }
        }
    }
}