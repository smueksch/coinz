package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.daos.CoinDAO
import com.coinz.app.database.daos.RateDAO

/**
 * Task to delete all coins that do not have the given validity date from local database.
 *
 * @param coinDao Data access object for coins.
 * @param rateDao Data access object for GOLD exchange rates.
 */
class DeleteInvalidsTask(private val coinDao: CoinDAO,
                         private val rateDao: RateDAO) : AsyncTask<String, Void, Void>() {

    /**
     * Delete all data in local database that is invalid.
     *
     * @param validDates List of valid dates, deletes all data with different date than first entry.
     */
    override fun doInBackground(vararg validDates: String): Void? {
        coinDao.deleteInvalids(validDates[0])
        rateDao.deleteInvalids(validDates[0])
        return null
    }
}
