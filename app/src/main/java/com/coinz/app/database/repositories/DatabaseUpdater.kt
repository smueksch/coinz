package com.coinz.app.database.repositories

import android.content.Context
import com.coinz.app.database.entities.Coin
import com.coinz.app.database.daos.CoinDAO
import com.coinz.app.database.daos.RateDAO
import com.coinz.app.database.asynctasks.DeleteInvalidsTask
import com.coinz.app.database.asynctasks.DownloadMapFileTask
import com.coinz.app.database.asynctasks.InsertCoinsTask
import com.coinz.app.utils.AppConsts
import com.coinz.app.utils.AppLog
import com.coinz.app.utils.DateUtil
import com.coinz.app.utils.UrlUtil
import com.google.gson.JsonObject
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
        val mapDownloadDate = settings.getString(AppConsts.mapDownloadDate, "")
        val currentDate = DateUtil.currentDate()

        // TODO: Need to update exchange rates.
        //if (mapDownloadDate != currentDate) {
        if (true) { // FOR TESTING ONLY
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
            val editor = settings.edit()

            AppLog(tag, "updateDatabase", "Setting mapDownloadDate to $currentDate in" +
                    "SharedPreferences")
            editor.putString(AppConsts.mapDownloadDate, currentDate)

            editor.apply()

            // Insert the new coins into the database and wait for task to finish.
            val coins = Coin.fromGeoJSON(rawMapData, currentDate)
                    ?: ArrayList()
            AppLog(CoinRepository.tag, "updateDatabase", "coins[0]=${coins[0]}")

            AppLog(tag, "updateDatabase", "Inserting new coins into database")
            InsertCoinsTask(coinDao).execute(*coins.toTypedArray()).get()

            // Insert new GOLD exchange rates into the database and wait for task to finish.
            // TODO: Extract the exchange rates from the GeoJSON.
            //val parsedMap = JSONObject(rawMapData)

            //AppLog(tag, "updateDatabase", "parsedMap.optJSONObject(\"rates\")=${parsedMap.optJSONObject("rates")}")

            AppLog(tag, "updateDatabase", "Finished updating database")
        }
    }

}