package com.tclow.composecurrencyconverter.data

import com.tclow.composecurrencyconverter.data.model.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface CurrencyApi {

    @GET("/latest")
    suspend fun getRates(
        @QueryMap options: Map<String, String>
    ): Response<CurrencyResponse>

    @GET("/{date}")
    suspend fun getHistoricalRates(
        @Path("date") date: String,
        @QueryMap options: Map<String, String>
    ): Response<CurrencyResponse>
}