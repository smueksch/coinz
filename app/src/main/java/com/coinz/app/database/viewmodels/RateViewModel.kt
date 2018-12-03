package com.coinz.app.database.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.coinz.app.database.CoinDatabase
import com.coinz.app.database.repositories.RateRepository

/**
 * View model to separate exchange rates data from UI.
 *
 * Separates the data of all the exchange rates from the actual UI. Takes care of
 * keeping the data when UI is destroyed, for instance through phone rotation.
 */
class RateViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RateRepository

    init {
        val db = CoinDatabase.getInstance(application)
        val coinDao = db.coinDao()
        val rateDao = db.rateDao()
        repository = RateRepository(application, coinDao, rateDao)
    }

    /**
     * Get GOLD exchange rate for given currency.
     *
     * @param currency Currency for which to get GOLD exchange rate.
     *
     * @return Requested exchange rate object.
     */
    fun getRateByCurrency(currency: String) = repository.getRateByCurrency(currency)

}