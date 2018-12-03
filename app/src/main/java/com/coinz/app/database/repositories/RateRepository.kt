package com.coinz.app.database.repositories

import android.content.Context
import com.coinz.app.database.daos.CoinDAO
import com.coinz.app.database.entities.Rate
import com.coinz.app.database.daos.RateDAO
import com.coinz.app.database.asynctasks.GetRateByCurrencyTask

/**
 * Class handling access to local data.
 *
 * This class abstracts the access to the local data, i.e. it handles data access asynchronously
 * and requests new data from the network should this be required.
 */
class RateRepository(context: Context,
                     coinDao: CoinDAO,
                     private val rateDao: RateDAO) : DatabaseUpdater(context, coinDao, rateDao) {

    init {
        // Make sure database is current right away.
        updateDatabase()
    }

    /**
     * Get GOLD exchange rate for given currency.
     *
     * @param currency Currency for which to get GOLD exchange rate.
     *
     * @return Requested exchange rate.
     */
    fun getRateByCurrency(currency: String): Rate {
        updateDatabase()
        return GetRateByCurrencyTask(rateDao).execute(currency).get()
    }

}