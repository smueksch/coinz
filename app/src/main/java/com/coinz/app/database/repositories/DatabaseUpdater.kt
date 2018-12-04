package com.coinz.app.database.repositories

import android.content.Context
import com.coinz.app.database.entities.Coin
import com.coinz.app.database.daos.CoinDAO
import com.coinz.app.database.daos.RateDAO
import com.coinz.app.database.asynctasks.DeleteInvalidsTask
import com.coinz.app.database.asynctasks.DownloadMapFileTask
import com.coinz.app.database.asynctasks.InsertCoinsTask
import com.coinz.app.database.asynctasks.InsertRatesTask
import com.coinz.app.database.entities.Rate
import com.coinz.app.utils.AppConsts
import com.coinz.app.utils.AppLog
import com.coinz.app.utils.DateUtil
import com.coinz.app.utils.UrlUtil
import org.json.JSONObject

/**
 * Parent class to all repositories relying on up to date map data.
 *
 * Any class that requires GeoJSON map data to be up to date should inherit from this class as it
 * implements a routine to update the internal database should a new GeoJSON map be available.
 */
abstract class DatabaseUpdater(private val context: Context,
                               private val coinDao: CoinDAO,
                               private val rateDao: RateDAO) {

    companion object {
        const val tag = "DatabaseUpdater"
    }

    /**
     * Update database from network if needed.
     *
     * ATTENTION: This needs to be done before any form of access to the database to make sure that
     * any data read from the database is valid.
     *
     * Note that data is considered valid if the date it was created (i.e. map download date) is
     * the same as the current date.
     */
    protected fun updateDatabase() {
        val settings = context.getSharedPreferences(AppConsts.preferencesFilename,
                Context.MODE_PRIVATE)

        // Get last map download date and current date.
        val mapDownloadDate = settings.getString(AppConsts.mapDownloadDate, "")
        val currentDate = DateUtil.currentDate()

        // Only run the next bit of code if we need a new map, because the map download date is not
        // today's date.
        if (mapDownloadDate != currentDate) {
            AppLog(tag, "updateDatabase", "Database invalid, mapDownloadDate=$mapDownloadDate")

            // Current database invalid, delete all data and wait for it to finish.
            AppLog(tag, "updateDatabase", "Deleting invalid data")
            DeleteInvalidsTask(coinDao, rateDao).execute(currentDate).get()

            // Download map file data and convert it directly to coins.
            AppLog(tag, "updateDatabase", "Downloading new map")
            AppLog(tag, "updateDatabase", "Download URL=${UrlUtil.mapUrl()}")
            val rawMapData = DownloadMapFileTask().execute(UrlUtil.mapUrl()).get()
            AppLog(tag, "updateDatabase", "rawMapData=${rawMapData.take(100)}")

            // Update the last map download date.
            updateMapDownloadDate(currentDate)

            // Update locally stored coins with new coins from downloaded map.
            updateCoins(rawMapData, currentDate)

            // Update locally stored GOLD exchange rates from downloaded map.
            updateRates(rawMapData, currentDate)

            AppLog(tag, "updateDatabase", "Finished updating database")
        }
    }

    /**
     * Update map download date in SharedPreferences.
     *
     * @param mapDownloadDate New map download date.
     */
    private fun updateMapDownloadDate(mapDownloadDate: String) {
        val settings = context.getSharedPreferences(AppConsts.preferencesFilename,
                Context.MODE_PRIVATE)

        // Update the last map download date.
        val editor = settings.edit()

        AppLog(tag, "updateDatabase", "Setting mapDownloadDate to $mapDownloadDate in" +
                "SharedPreferences")
        editor.putString(AppConsts.mapDownloadDate, mapDownloadDate)

        editor.apply()
    }

    /**
     * Update locally stored coins from raw GeoJSON map data.
     *
     * @param rawMapData Raw GeoJSON map data with new coins.
     * @param validDate Date for which coins will be valid.
     */
    private fun updateCoins(rawMapData: String, validDate: String) {
        // Extract coin data from map data.
        val coins = Coin.fromGeoJSON(rawMapData, validDate)
                ?: ArrayList()
        AppLog(CoinRepository.tag, "updateDatabase", "coins[0]=${coins[0]}")

        // Insert the new coins into the database and wait for task to finish.
        AppLog(tag, "updateDatabase", "Inserting new coins into database")
        InsertCoinsTask(coinDao).execute(*coins.toTypedArray()).get()
    }

    /**
     * Update locally stored GOLD exchange rates from raw GeoJSON map data.
     *
     * @param rawMapData Raw GeoJSON map data with new coins.
     * @param validDate Date for which exchange rates will be valid.
     */
    private fun updateRates(rawMapData: String, validDate: String) {
        // Extract GOLD exchange rates from map data.
        val parsedMap = JSONObject(rawMapData)
        val parsedRates = parsedMap.optJSONObject("rates") // TODO: Make "rates AppConsts.
        AppLog(tag, "updateDatabase", "parsedMap.optJSONObject(\"rates\")=$parsedRates")

        // Convert JSON rates into Rate objects we can store in our database.
        val rates = ArrayList<Rate>()
        AppConsts.supportedCurrencies.forEach {
            val rate = Rate(it,
                            parsedRates.optDouble(it),
                            validDate)
            rates.add(rate)
        }

        // Insert new GOLD exchange rates into the database and wait for task to finish.
        InsertRatesTask(rateDao).execute(*rates.toTypedArray()).get()
    }

}