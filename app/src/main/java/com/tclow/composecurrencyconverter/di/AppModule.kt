package com.tclow.composecurrencyconverter.di

import com.tclow.composecurrencyconverter.data.convert.CurrencyApi
import com.tclow.composecurrencyconverter.utils.data.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCurrencyApi(): CurrencyApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()) // convert json classes to gson
        .build()
        .create(CurrencyApi::class.java)
}