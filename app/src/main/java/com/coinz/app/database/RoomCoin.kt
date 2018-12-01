package com.coinz.app.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point

// TODO: Think about whether there isn't a better way to structure the DB, e.g. have separate table
// for currencies available, separate table for marker-related info, e.g. marker_*, lat, long.
// TODO: Document
@Entity(tableName = "coins")
data class RoomCoin(
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

    companion object {
        /**
         * Create list of coins from raw map data
         *
         * @param geoJson Raw map data.
         * @return List of coins initialized with raw map data.
         */
        fun fromGeoJSON(geoJson: String, validDate: String): List<RoomCoin>? {
            val coins = ArrayList<RoomCoin>()
            with(FeatureCollection.fromJson(geoJson)) {
                this.features()?.forEach { coins.add(RoomCoin(it, validDate)) }
            }
            return coins
        }
    }

    /**
     * Create coin from GeoJSON feature.
     *
     * @param feature GeoJSON feature holding data for the coin.
     * @param multiplier RoomCoin value multiplier.
     */
    constructor(feature: Feature, validDate: String, multiplier: Double = 1.0):
            this("", "", 0.0, 0.0, "", "", 0.0, 0.0, false, validDate) {
        // TODO: softcode these string, maybe in AppConsts
        feature.properties()?.let {
            id = it["id"].asString
            currency = it["currency"].asString
            originalValue = it["value"].asDouble
            storedValue = originalValue * multiplier
            markerSymbol = it["marker-symbol"].asString
            markerColor = it["marker-color"].asString
            // TODO: Set valid date appropriately!
        }

        with(feature.geometry()) {
            if (this is Point) {
                latitude = latitude()
                longitude = longitude()
            }
        }
    }

}