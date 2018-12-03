package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.daos.CoinDAO

/**
 * Task to delete coin with a given id.
 */
class DeleteCoinByIdTask(dao: CoinDAO) : AsyncTask<String, Void, Void>() {

    private var coinDAO = dao

    /**
     * Delete coin if it has given id.
     *
     * @param id First entry is id, deletes coin with that id.
     */
    override fun doInBackground(vararg ids: String): Void? {
        coinDAO.deleteById(ids[0])
        // TODO: is there better way of returning nothing but have return type Void?
        return null
    }
}
