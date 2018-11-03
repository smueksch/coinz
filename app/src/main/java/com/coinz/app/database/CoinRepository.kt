package com.coinz.app.database

import android.arch.lifecycle.LiveData
import android.content.Context
import com.coinz.app.database.asynctasks.*

import com.coinz.app.utils.AppStrings
import com.coinz.app.utils.DateUtil
import com.coinz.app.utils.UrlUtil
import com.coinz.app.utils.AppLog

class CoinRepository(val context: Context) {

    companion object {
        const val tag = "CoinRepository"
        const val COLLECTED = true
        const val NOT_COLLECTED = false
    }

    private var coinDao: CoinDAO?

    init {
        val db = CoinDatabase.getInstance(context)
        coinDao = db?.coinDao()

        updateDatabase()
    }

    // TODO: Is this actually returning something?
    fun getAllCollected() = coinDao?.let {
        updateDatabase()
        GetByIsCollectedTask(it).execute(COLLECTED).get()
    }

    // TODO: Can we turn this into a 'let' statement again? -> Neater code.
    fun getAllNotCollected(): List<Coin>? {
        updateDatabase()
        if (coinDao != null) {
            return GetByIsCollectedTask(coinDao!!).execute(NOT_COLLECTED).get()
        } else {
            return null
        }
    }

    fun insert(coin: Coin) = coinDao?.let { InsertTask(it).execute(coin) }

    fun setCollected(id: String) = coinDao?.let { SetCollectedTask(it).execute(id) }

    /**
     * Update database from network if needed.
     *
     * ATTENTION: This needs to be done before any form of access to the database to make sure that
     * coin data read from the database is valid.
     *
     * Note that a coin is considered valid if the date it was created (i.e. map download date) is
     * the same as the current date.
     */
    private fun updateDatabase() {
        val settings = context.getSharedPreferences(AppStrings.preferencesFilename,
                                                    Context.MODE_PRIVATE)
        val mapDownloadDate = settings.getString(AppStrings.mapDownloadDate, "")
        val currentDate = DateUtil.currentDate()

        coinDao?.let {
            if (mapDownloadDate != currentDate) {
                AppLog(tag, "updateDatabase", "Database invalid, mapDownloadDate=$mapDownloadDate")

                // Current database invalid, delete coins and wait for it to finish.
                AppLog(tag, "updateDatabase", "Deleting invalid coins")
                DeleteInvalidsTask(it).execute(currentDate).get()

                // Download map file data and convert it directly to coins.
                AppLog(tag, "updateDatabase", "Downloading new map")
                val rawMapData = DownloadMapFileTask().execute(UrlUtil.mapUrl()).get()
                AppLog(tag, "updateDatabase", "rawMapData=${rawMapData.take(100)}")

                // Update the last map download date.
                val editor = settings.edit()

                AppLog(tag, "updateDatabase", "Setting mapDownloadDate to $currentDate in" +
                       "SharedPreferences")
                editor.putString(AppStrings.mapDownloadDate, currentDate)

                editor.apply()

                val coins = Coin.fromGeoJSON(rawMapData, currentDate) ?: ArrayList<Coin>()
                AppLog(tag, "updateDatabase", "coins[0]=${coins[0]}")

                // Insert the new coins into the database and wait for task to finish.
                AppLog(tag, "updateDatabase", "Inserting new coins into database")
                InsertTask(it).execute(*coins.toTypedArray()).get()

                AppLog(tag, "updateDatabase", "Finished updating database")
            }
        }
    }

}