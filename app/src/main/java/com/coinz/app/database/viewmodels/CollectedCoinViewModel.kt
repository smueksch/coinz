package com.coinz.app.database.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.coinz.app.database.entities.Coin
import com.coinz.app.database.CoinDatabase
import com.coinz.app.database.repositories.CoinRepository


/**
 * View model to separate coins data from UI.
 *
 * Separates the data of all the coins displayed in local wallet from the actual UI. Takes care of
 * keeping the data when UI is destroyed, for instance through phone rotation.
 */
class CollectedCoinViewModel(application: Application): AndroidViewModel(application) {

    private var coinRepository: CoinRepository

    var coins: LiveData<List<Coin>>

    init {
        val db = CoinDatabase.getInstance(application)
        val coinDao = db.coinDao()
        val rateDao = db.rateDao()
        coinRepository = CoinRepository(application, coinDao, rateDao)

        coins = coinRepository.getAllCollected()
    }

    /**
     * Return coin with given ID.
     *
     * @param coinId ID of requested coin.
     */
    fun getCoinById(coinId: String) = coinRepository.getCoinById(coinId)

    /**
     * Delete coin with given id.
     *
     * @param id ID of coin to be deleted.
     */
    fun deleteCoinById(id: String) = coinRepository.deleteById(id)

}