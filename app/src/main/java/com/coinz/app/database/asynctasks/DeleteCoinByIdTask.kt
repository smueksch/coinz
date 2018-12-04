package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.daos.CoinDAO

/**
 * Task to delete coin with a given id from local database.
 *
 * @param dao Data access object for coin.
 */
class DeleteCoinByIdTask(dao: CoinDAO) : AsyncTask<String, Void, Void>() {

    // Data access object used to access coin data.
    private var coinDAO = dao

    /**
     * Delete coin if it has given id.
     *
     * @param ids List of coin IDs, only the first will be taken into account.
     */
    override fun doInBackground(vararg ids: String): Void? {
        coinDAO.deleteById(ids[0])
        return null
    }
}
