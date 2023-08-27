package com.tclow.composecurrencyconverter.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object{
        var rateDatabase: RateDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): RateDatabase {
            if (rateDatabase == null) {
                rateDatabase = Room.databaseBuilder(
                    context,
                    RateDatabase::class.java,
                    "rates.db"
                ).build()
            }
            return rateDatabase!!
        }
    }
    abstract fun rateDAO(): RateDAO
}