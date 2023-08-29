package com.tclow.composecurrencyconverter.domain.convert.use_case

import com.tclow.composecurrencyconverter.domain.convert.repository.CurrencyRepository
import com.tclow.composecurrencyconverter.presentation.convert.ConvertEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import kotlin.math.round

class ConvertRates(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(
        strAmount: String,
        fromCurrency: String,
        toCurrency: String
    ): ConvertEvent =
        withContext(Dispatchers.IO) {
            val amount = strAmount.toFloatOrNull()
                ?: return@withContext ConvertEvent.Failure("Not a valid amount")
            val ratesFromDb = repository.getRatesWithDate(LocalDate.now().toString())
            val rates: Map<String, Double>

            if (ratesFromDb.isNotEmpty()) {
                rates = ratesFromDb[0].rates!!

                val toRate = getRateForCurrency(toCurrency, rates)
                val fromRate = getRateForCurrency(fromCurrency, rates)

                if (toRate == null || fromRate == null) {
                    return@withContext ConvertEvent.Failure("Unexpected error")
                } else {
                    val convertedCurrency = currencyConversion(amount, toRate, fromRate)
                    return@withContext ConvertEvent.Success("$strAmount $fromCurrency = $convertedCurrency $toCurrency")
                }
            } else {
                // If database returned empty value, return failed event
                return@withContext ConvertEvent.Failure("Failed to access database.")
            }
        }
}

private fun getRateForCurrency(requestCurrency: String, rates: Map<String, Double>): Double? {
    var result: Double? = null

    for ((currency, rate) in rates) {
        if (requestCurrency == currency) {
            result = rate
            break
        }
    }

    return result
}

private fun currencyConversion(amount: Float, toRate: Double, fromRate: Double): Double {
    val result: Double
    val actualRate = toRate / fromRate

    result = round(amount * actualRate * 100) / 100

    return result
}