package com.coinz.app.utils

/**
 * Application Strings.
 *
 * Object to make all globally relevant string constants available to all classes.
 *
 */
object AppStrings {
    /*
     * General:
     */
    const val dateFormat = "yyyy/MM/dd"

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
}