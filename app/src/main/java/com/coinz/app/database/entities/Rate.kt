package com.coinz.app.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.coinz.app.utils.AppConsts

/**
 * Class representing exchange rate for currencies to GOLD.
 */
@Entity(tableName = AppConsts.ratesTableName)
class Rate(
        // Currency in question.
        @PrimaryKey @ColumnInfo(name = "currency") var currency: String,
        // Exchange rate into GOLD.
        @ColumnInfo(name = "rate") var rate: Double,
        // Date for which exchange rate is valid.
        @ColumnInfo(name = "valid_date") var validDate: String
)
