package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.entities.Rate
import com.coinz.app.database.daos.RateDAO
import com.coinz.app.utils.AppLog

/**
 * Insert GOLD exchange rates asynchronously.
 */
class InsertRatesTask(private val rateDao: RateDAO) : AsyncTask<Rate, Void, Void>() {

    companion object {
        const val tag = "InsertRatesTask"
    }

    override fun doInBackground(vararg rates: Rate): Void? {
        AppLog(tag, "doInBackground", "rates[0]=${rates[0]}")
        rateDao.insert(*rates)
        return null
    }

}