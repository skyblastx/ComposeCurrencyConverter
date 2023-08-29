package com.tclow.composecurrencyconverter.di

import android.app.Application
import androidx.room.Room
import com.tclow.composecurrencyconverter.data.convert.CurrencyApi
import com.tclow.composecurrencyconverter.data.convert.repository.CurrencyRepositoryImpl
import com.tclow.composecurrencyconverter.data.database.RateDatabase
import com.tclow.composecurrencyconverter.domain.convert.repository.CurrencyRepository
import com.tclow.composecurrencyconverter.domain.convert.use_case.ConvertRates
import com.tclow.composecurrencyconverter.domain.convert.use_case.ConvertUseCases
import com.tclow.composecurrencyconverter.domain.convert.use_case.GetRates
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

    @Provides
    @Singleton
    fun provideRateDatabase(app: Application): RateDatabase {
        return Room.databaseBuilder(
            app,
            RateDatabase::class.java,
            RateDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideCurrencyRepository(db: RateDatabase): CurrencyRepository {
        return CurrencyRepositoryImpl(provideCurrencyApi(),  db.rateDAO)
    }

    @Provides
    @Singleton
    fun provideConvertUseCases(repository: CurrencyRepository): ConvertUseCases {
        return ConvertUseCases(
            getRates = GetRates(repository),
            convertRates = ConvertRates(repository)
        )
    }
}