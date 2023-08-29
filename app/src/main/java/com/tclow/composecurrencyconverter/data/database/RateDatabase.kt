package com.tclow.composecurrencyconverter.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tclow.composecurrencyconverter.data.database.converter.HashMapConverter
import com.tclow.composecurrencyconverter.data.database.converter.RateConverter
import com.tclow.composecurrencyconverter.data.database.dao.CurrencyRate
import com.tclow.composecurrencyconverter.data.database.dao.RateDAO

@Database(
    entities = [CurrencyRate::class],
    version = 1
)
@TypeConverters(
    RateConverter::class,
    HashMapConverter::class
)
abstract class RateDatabase: RoomDatabase() {

    abstract val rateDAO: RateDAO

    companion object{
        const val DATABASE_NAME = "rates_db"
    }
}