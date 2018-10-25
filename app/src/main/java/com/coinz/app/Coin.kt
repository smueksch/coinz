package com.coinz.app

import com.mapbox.geojson.Feature

/**
 * Coin representation.
 *
 * This class represents a coin in the game.
 *
 * @property currency Currency of the coin
 * @property value Value of the coin
 * @constructor Create a coin with specified properties.
 */
data class Coin(var currency: String, var value: Double) {

    companion object {
        const val DEFAULT_CURRENCY = ""
        const val DEFAULT_VALUE = 0.0
    }

    /**
     * Create coin from GeoJSON Feature.
     *
     * Note in the case that `feature` has no properties, the coin will be initialized with
     * default currency and value instead.
     *
     * @param feature GeoJSON feature from which to extract coin data.
     */
    constructor(feature: Feature) : this(DEFAULT_CURRENCY, DEFAULT_VALUE) {
        feature.properties()?.let {
            this.currency = it.get("currency").asString
            this.value = it.get("value").asDouble
        }
    }

}