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
     * Permissions:
     */
    const val REQUEST_ACCESS_FINE_LOCATION = 1729

    /*
     * Fragments:
     */
    // Tags used when adding fragments and to find them after they've been added.
    const val mapFragmentTag = "MAP_FRAGMENT"
    const val localWalletFragmentTag = "LOCALE_WALLET_FRAGMENT"

    /*
     * Log in and account creation
     */
    const val minPasswordLength = 6 // Minimum password length in characters.

    /*
     * Mapbox:
     */
    const val mapboxToken = "pk.eyJ1Ijoic2VibXVlayIsImEiOiJjam12MWE0a3kwNW92M3Bxdmxxcnk1ZmYwIn0.1tI9T6CLf7Qq0ZvGtCK9QQ"
    const val initialCameraZoom = 16.0

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
    // Name of SQL database in which local data is stored.
    const val coinDbName = "coin.db"
    // Name of table in which coins are stored.
    const val coinsTableName = "coins"
    // Name of table in which GOLD exchange rates are stored.
    const val ratesTableName = "rates"

    /*
     * Coin Collection:
     */
    /**
     * Currently supported currencies.
     */
    val supportedCurrencies = arrayOf("SHIL",
                                      "DOLR",
                                      "QUID",
                                      "PENY")
    // Maximum coin collection distance in meters.
    const val maxCollectDist = 25.0
    // Maximum bankable coins per day.
    const val maxBankable = 25
}