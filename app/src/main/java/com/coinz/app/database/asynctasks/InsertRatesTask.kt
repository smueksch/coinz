package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.entities.Rate
import com.coinz.app.database.daos.RateDAO
import com.coinz.app.utils.AppLog

/**
 * Insert GOLD exchange rates asynchronously into local database.
 *
 * @param rateDao Data access object for GOLD exchange rates.
 */
class InsertRatesTask(private val rateDao: RateDAO) : AsyncTask<Rate, Void, Void>() {

    companion object {
        // Tag used to identify log output of this class.
        const val tag = "InsertRatesTask"
    }

    /**
     * Insert given exchange rates into local database.
     *
     * @param rates List of exchange rates to insert.
     */
    override fun doInBackground(vararg rates: Rate): Void? {
        AppLog(tag, "doInBackground", "rates[0]=${rates[0]}")
        rateDao.insert(*rates)
        return null
    }

}