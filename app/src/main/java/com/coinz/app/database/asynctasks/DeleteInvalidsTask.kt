package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.daos.CoinDAO
import com.coinz.app.database.daos.RateDAO

/**
 * Task to delete all coins that do not have the given validity date.
 */
class DeleteInvalidsTask(private val coinDao: CoinDAO,
                         private val rateDao: RateDAO) : AsyncTask<String, Void, Void>() {

    /**
     * Delete all data in local database that is invalid.
     *
     * @param validDates First entry is validity date, deletes all data with different date.
     */
    override fun doInBackground(vararg validDates: String): Void? {
        coinDao.deleteInvalids(validDates[0])
        rateDao.deleteInvalids(validDates[0])
        return null
    }
}
