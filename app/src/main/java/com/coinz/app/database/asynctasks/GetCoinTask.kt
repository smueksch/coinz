package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.entities.Coin
import com.coinz.app.database.daos.CoinDAO

/**
 * Task to get a coin for requested ID asynchronously from local database..
 *
 * @param dao Data access object for coins.
 */
class GetCoinTask(dao: CoinDAO) : AsyncTask<String, Void, Coin>() {

    // Data access object used to access coin data.
    private var coinDao = dao

    /**
     * Get coin for a given ID.
     *
     * @param ids List of coin IDs, only the first will be taken into account.
     *
     * @return Coin corresponding to given ID.
     */
    override fun doInBackground(vararg ids: String): Coin = coinDao.get(ids[0])

}