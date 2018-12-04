package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.entities.Coin
import com.coinz.app.database.daos.CoinDAO
import com.coinz.app.utils.AppLog

/**
 * Task to asynchronously insert one or more coins into the local database.
 *
 * @param dao Data access object for coins.
 */
class InsertCoinsTask(dao: CoinDAO) : AsyncTask<Coin, Void, Void>() {

    companion object {
        // Tag used to identify log output of this class.
        const val tag = "InsertCoinsTask"
    }

    // Data access object used to access coin data.
    private var coinDao = dao

    /**
     * Insert given coins into local database.
     *
     * @param coins List of coins to insert.
     */
    override fun doInBackground(vararg coins: Coin): Void? {
        AppLog(tag, "doInBackground", "coins[0]=")
        coinDao.insert(*coins)
        return null
    }

}