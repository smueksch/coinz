package com.coinz.app

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

// TODO: should this be "OldCoin"?
// TODO: Think about whether there isn't a better way to structure the DB, e.g. have separate table
// for currencies available, separate table for marker-related info, e.g. marker_*, lat, long.
// TODO: Document
@Entity(tableName = "coins")
data class Coin(
        @PrimaryKey @ColumnInfo(name = "id") var id: String,
        @ColumnInfo(name = "currency") var currency: String,
        @ColumnInfo(name = "original_value") var originalValue: Double,
        @ColumnInfo(name = "stored_value") var storedValue: Double,
        @ColumnInfo(name = "marker_symbol") var markerSymbol: String,
        @ColumnInfo(name = "marker_color") var markerColor: String, // TODO: Better stored as number?
        @ColumnInfo(name = "latitude") var latitude: Double,
        @ColumnInfo(name = "longitude") var longitude: Double,
        @ColumnInfo(name = "is_collected") var isCollected: Boolean,
        @ColumnInfo(name = "valid_date") var validDate: String // Date on which coin is valid, expires after.
) {

    // TODO: Constructor for Coin from Feature.

}