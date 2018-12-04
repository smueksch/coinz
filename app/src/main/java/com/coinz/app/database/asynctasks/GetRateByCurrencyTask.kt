package com.coinz.app.database.asynctasks


import android.os.AsyncTask
import com.coinz.app.database.entities.Rate
import com.coinz.app.database.daos.RateDAO

/**
 * Task to get GOLD exchange rate for given currency asynchronously from local database.
 *
 * @param dao Data access object for coins.
 */
class GetRateByCurrencyTask(dao: RateDAO) : AsyncTask<String, Void, Rate>() {

    // Data access object used to access coin data.
    private var rateDao = dao

    /**
     * Get GOLD exchange rate for given currency.
     *
     * @param currencies List of currencies, only the first will be taken into account.
     *
     * @return GOLD exchange rate for requested currency.
     */
    override fun doInBackground(vararg currencies: String): Rate = rateDao.getByCurrency(currencies[0])

}