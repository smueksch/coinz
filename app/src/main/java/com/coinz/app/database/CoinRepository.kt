package com.coinz.app.database

import android.content.Context
import com.coinz.app.database.asynctasks.*

import com.coinz.app.utils.AppConsts
import com.coinz.app.utils.DateUtil
import com.coinz.app.utils.UrlUtil
import com.coinz.app.utils.AppLog

class CoinRepository(private val context: Context, private val coinDao: CoinDAO?) {

    companion object {
        const val tag = "CoinRepository"
        const val COLLECTED = true
        const val NOT_COLLECTED = false
    }

    init {
        updateDatabase()
    }

    fun getCoinById(id: String) = coinDao?.let {
        updateDatabase()
        GetCoinTask(it).execute(id).get()
    }

    fun getAllCollected() = coinDao?.let {
        updateDatabase()
        GetByIsCollectedTask(it).execute(COLLECTED).get()
    }

    fun getAllNotCollected() = coinDao?.let {
        updateDatabase()
        GetByIsCollectedTask(it).execute(NOT_COLLECTED).get()
    }

    fun insert(coin: Coin) = coinDao?.let { InsertTask(it).execute(coin) }

    fun setCollected(id: String) = coinDao?.let { SetCollectedTask(it).execute(id) }

    fun deleteById(id: String) = coinDao?.let { DeleteByIdTask(it).execute(id) }

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
        val settings = context.getSharedPreferences(AppConsts.preferencesFilename,
                                                    Context.MODE_PRIVATE)
        val mapDownloadDate = settings.getString(AppConsts.mapDownloadDate, "")
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
                editor.putString(AppConsts.mapDownloadDate, currentDate)

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