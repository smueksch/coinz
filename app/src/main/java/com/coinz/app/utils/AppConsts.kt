package com.coinz.app.utils

/**
 * Application Constants.
 *
 * Object to make all globally relevant constants available to all classes.
 *
 */
object AppConsts {
    /*
     * General:
     */
    const val dateFormat = "yyyy/MM/dd"

    /*
     * Mapbox:
     */
    const val mapboxToken = "pk.eyJ1Ijoic2VibXVlayIsImEiOiJjam12MWE0a3kwNW92M3Bxdmxxcnk1ZmYwIn0.1tI9T6CLf7Qq0ZvGtCK9QQ"

    /*
     * Remote data:
     */
    const val mapBaseUrl = "http://homepages.inf.ed.ac.uk/stg/coinz"
    const val mapFilename = "coinzmap.geojson"

    /*
     * SharedPreferences:
     */
    const val preferencesFilename = "CoinzPreferences"
    const val mapDownloadDate = "mapDownloadDate"

    /*
     * Internal Storage:
     */
    const val coinDbName = "coin.db"

    /*
     * Coin Collection:
     */
    // Maximum coin collection distance in meters.
    const val maxCollectDist = 25.0
}