package com.tclow.composecurrencyconverter.domain.convert.use_case

import android.util.Log
import com.tclow.composecurrencyconverter.data.database.dao.CurrencyRate
import com.tclow.composecurrencyconverter.domain.convert.repository.CurrencyRepository
import com.tclow.composecurrencyconverter.presentation.convert.ConvertEvent
import com.tclow.composecurrencyconverter.utils.Resource
import java.time.LocalDate

class GetRates(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(): ConvertEvent {
        if (repository.getRatesWithDate(LocalDate.now().toString()).isNotEmpty()) {
            Log.d("getRates", "Did not call API")
            return ConvertEvent.Success("Success")
        } else {
            Log.d("getRates", "Did call API")
            return when (val ratesResponse = repository.getRates()) {
                is Resource.Error -> ConvertEvent.Failure(
                    ratesResponse.message ?: "Error while getting rates"
                )

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
}