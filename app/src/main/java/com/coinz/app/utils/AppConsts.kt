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
    // Standard date format used throughout the app.
    const val dateFormat = "yyyy/MM/dd"

    /*
     * Permissions:
     */
    // Arbitrary code used to identify permission being requested and listen to the outcome.
    const val REQUEST_ACCESS_FINE_LOCATION = 1729

    /*
     * Fragments:
     */
    // Tags used when adding fragments and to find them after they've been added.
    const val mapFragmentTag = "MAP_FRAGMENT"
    const val localWalletFragmentTag = "LOCAL_WALLET_FRAGMENT"
    const val centralBankFragmentTag = "CENTRAL_BANK_FRAGMENT"

    /*
     * Log in and account creation
     */
    // Minimum password length in characters.
    const val minPasswordLength = 6

    /*
     * Mapbox:
     */
    // Token used to get access to Mapbox functionality.
    const val mapboxToken = "pk.eyJ1Ijoic2VibXVlayIsImEiOiJjam12MWE0a3kwNW92M3Bxdmxxcnk1ZmYwIn0.1tI9T6CLf7Qq0ZvGtCK9QQ"
    // Initial zoom of the camera, determines what area the user sees around them at app start.
    const val initialCameraZoom = 16.0

    /*
     * Remote data:
     */
    // Base URL used to identify the source of the raw map files.
    const val mapBaseUrl = "http://homepages.inf.ed.ac.uk/stg/coinz"
    // Name of raw GeoJSON map file on the server.
    const val mapFilename = "coinzmap.geojson"

    /*
     * SharedPreferences:
     */
    // Name of the SharedPreferences file, use to identify it throughout the app.
    const val preferencesFilename = "CoinzPreferences"
    // Name of field in which the current download date of the current map is stored.
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