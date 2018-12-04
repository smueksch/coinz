package com.coinz.app.database.asynctasks

import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.coinz.app.database.entities.Coin
import com.coinz.app.database.daos.CoinDAO

/**
 * Task to return list of collected/uncollected coins asynchronously from local database.
 *
 * @param dao Data access object for coins.
 */
class GetCoinsByIsCollectedTask(dao: CoinDAO) : AsyncTask<Boolean, Void, LiveData<List<Coin>>>() {

    // Data access object used to access coin data.
    private var coinDao = dao

    /**
     * Get list of collected/uncollected coins.
     *
     * @param isCollecteds List of values representing whether collected or uncollected coins are
     *                     requested. 'true' means collected coins are requested.
     *
     * @return LivaData object of List of requested coins.
     */
    override fun doInBackground(vararg isCollecteds: Boolean?): LiveData<List<Coin>>? =
            when (isCollecteds[0]) {
                true -> coinDao.getAllCollected()
                false -> coinDao.getAllNotCollected()
                else -> null
            }

}