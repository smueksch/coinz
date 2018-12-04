package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.daos.CoinDAO

/**
 * Task to asynchronously set given coin to be collected in local database.
 *
 * @param dao Data access object for coins.
 */
class SetCoinCollectedTask(dao: CoinDAO) : AsyncTask<String, Void, Void>() {

    // Data access object used to access coin data.
    private var coinDAO = dao

    /**
     * Set coin for given ID as collected.
     *
     * @param ids List of coin IDs, only the first one will be taken into account.
     */
    override fun doInBackground(vararg ids: String): Void? {
        coinDAO.setCollected(ids[0])
        return null
    }

}