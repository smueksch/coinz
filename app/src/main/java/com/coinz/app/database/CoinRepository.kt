package com.coinz.app.database

import android.arch.lifecycle.LiveData
import android.content.Context
import android.os.AsyncTask

import com.coinz.app.utils.AppStrings
import com.coinz.app.utils.DateUtil

import com.coinz.app.database.asynctasks.GetByIsCollectedTask
import com.coinz.app.database.asynctasks.InsertTask
import com.coinz.app.database.asynctasks.SetCollectedTask

// TODO: Fetching the GeoJSON should happen here IF NEEDED.
// TODO: Should this be a singleton?
class CoinRepository(val context: Context) {

    companion object {
        const val COLLECTED = true
        const val NOT_COLLECTED = false
    }

    private var coinDao: CoinDAO?
    //private var coins: LiveData<List<Coin>>?

    init {
        val db = CoinDatabase.getInstance(context)
        coinDao = db?.coinDao()

        checkDatabaseValidity()

        // TODO: This is the crucial bit here. When we initalize the repository we need to think
        // about whether we want to download the new coins or use the old ones. That decision is
        // based on whether the coins are still valid or not.
        // In general, when the repository is created the validity of all coins should be checked.
        //coins = coinDao?.getAll()
    }

    // TODO: Currently no use for this function, code it later if-need-be.
    //fun getAll(): LiveData<List<Coin>>? = coins

    fun getAllCollected(): LiveData<List<Coin>>? = coinDao?.let {
        checkDatabaseValidity()
        GetByIsCollectedTask(it).execute(COLLECTED).get()
    }

    fun getAllNotCollected(): LiveData<List<Coin>>? = coinDao?.let {
        checkDatabaseValidity()
        GetByIsCollectedTask(it).execute(NOT_COLLECTED).get()
    }

    fun insert(coin: Coin) = coinDao?.let { InsertTask(it).execute(coin) }

    fun setCollected(id: String) = coinDao?.let { SetCollectedTask(it).execute(id) }

    /**
     * Check that all coins in database are still valid.
     *
     * ATTENTION: This needs to be done before any form of access to the database.
     *
     * Note that a coin is considered valid if the date it was created (i.e. map download date) is
     * the same as the current date.
     */
    private fun checkDatabaseValidity() {
        val settings = context.getSharedPreferences(AppStrings.preferencesFilename,
                Context.MODE_PRIVATE)
        val mapDownloadDate = settings.getString(AppStrings.mapDownloadDate, "")

        if (mapDownloadDate == DateUtil.currentDate()) {
            // Currently stored coins still valid.
        } else {
            // Need to discard currently stored coins and download new ones.
        }
    }

}