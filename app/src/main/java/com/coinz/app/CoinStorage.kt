package com.coinz.app

import com.mapbox.geojson.Feature

/**
 * Abstract class to handle a collection of coins.
 */
abstract class CoinStorage {

    protected var coins = HashMap<String, Coin>()

    open fun addCoin(id:String, coin: Coin) = coins.put(id, coin)

    /**
     * Add coin directly from GeoJSON feature.
     *
     * If Feature has no properties, nothing will be added.
     *
     * @param feature Feature representing coin.
     */
    open fun addCoinFromFeature(feature: Feature) = feature.properties()?.let {
        addCoin(it.get("id").asString, Coin(feature))
    }

    open fun removeCoin(id: String) = coins.remove(id)

    open fun getCoin(id: String) = coins.get(id)

    /**
     * Get array of all stored coins.
     * @return ArrayList of stored coins.
     */
    open fun getCoins() = ArrayList<Coin>(coins.values)

    abstract fun saveCoins()

    abstract fun loadCoins()

}

