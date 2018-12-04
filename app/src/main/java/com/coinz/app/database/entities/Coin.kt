package com.coinz.app.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.coinz.app.utils.AppConsts
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point

/**
 * Class representing a coin that can also be displayed on a map as a marker.
 */
@Entity(tableName = AppConsts.coinsTableName)
data class Coin(
        // Coin ID to identify each coin later on.
        @PrimaryKey @ColumnInfo(name = "id") var id: String,
        // Coin currency, relevant when exchanging coin to GOLD.
        @ColumnInfo(name = "currency") var currency: String,
        // Original value of coin when loaded from GeoJSON.
        @ColumnInfo(name = "original_value") var originalValue: Double,
        // Stored value of coin when collected, use for bonus feature: value multipliers.
        @ColumnInfo(name = "stored_value") var storedValue: Double,
        // Symbol on coin marker, used to decide which marker will be displayed on map.
        @ColumnInfo(name = "marker_symbol") var markerSymbol: String,
        // Color of marker, used to decide which marker will be displayed on map.
        @ColumnInfo(name = "marker_color") var markerColor: String,
        // Latitude, used to determine correct marker location.
        @ColumnInfo(name = "latitude") var latitude: Double,
        // Longitude, used to determine correct marker location.
        @ColumnInfo(name = "longitude") var longitude: Double,
        // Flag to determine whether a coin has been collected or not.
        @ColumnInfo(name = "is_collected") var isCollected: Boolean,
        // Date for which coin is valid, expires after.
        @ColumnInfo(name = "valid_date") var validDate: String
) {

    companion object {
        /**
         * Create list of coins from raw map data
         *
         * @param geoJson Raw map data.
         * @return List of coins initialized with raw map data.
         */
        fun fromGeoJSON(geoJson: String, validDate: String): List<Coin>? {
            val coins = ArrayList<Coin>()
            with(FeatureCollection.fromJson(geoJson)) {
                this.features()?.forEach { coins.add(Coin(it, validDate)) }
            }
            return coins
        }
    }

    /**
     * Create coin from GeoJSON feature.
     *
     * @param feature GeoJSON feature holding data for the coin.
     * @param multiplier Coin value multiplier.
     */
    constructor(feature: Feature, validDate: String, multiplier: Double = 1.0):
            this("", "", 0.0, 0.0, "", "", 0.0, 0.0, false, validDate) {
        // Fill the coin with the data from the GeoJSON feature.
        feature.properties()?.let {
            id = it["id"].asString
            currency = it["currency"].asString
            originalValue = it["value"].asDouble
            storedValue = originalValue * multiplier
            markerSymbol = it["marker-symbol"].asString
            markerColor = it["marker-color"].asString
            // TODO: Set valid date appropriately!
        }

        // Ensure we have coordinates and then store them.
        with(feature.geometry()) {
            if (this is Point) {
                latitude = latitude()
                longitude = longitude()
            }
        }
    }

}