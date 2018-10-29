package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.CoinDAO

/** TODO: Deprecated
/**
 * Task to delete all coins.
 */
class DeleteAllTask(dao: CoinDAO) : AsyncTask<Void, Void, Void>() {

    private var coinDAO = dao

    /**
     * Delete coins that do not have given validity date.
     *
     * @param validDates First entry is validity date, deletes all coins with different date.
     */
    override fun doInBackground(vararg p0: Void?): Void? {
        coinDAO.deleteAll()
        // TODO: is there better way of returning nothing but have return type Void?
        return null
    }
}
        */