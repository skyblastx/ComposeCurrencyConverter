package com.tclow.composecurrencyconverter.data.database.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencyrate")
class CurrencyRate {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo(name = "date")
    var date: String? = null

    @ColumnInfo(name = "currencycode")
    var currencyCode: String? = null

    @ColumnInfo(name = "rates")
    var rates: Map<String, Double>? = null
}