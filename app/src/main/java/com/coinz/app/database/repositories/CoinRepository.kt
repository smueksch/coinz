package com.coinz.app.database.repositories

import android.arch.lifecycle.LiveData
import android.content.Context
import com.coinz.app.database.entities.Coin
import com.coinz.app.database.daos.CoinDAO
import com.coinz.app.database.daos.RateDAO
import com.coinz.app.database.asynctasks.*

/**
 * Class handling access to local data.
 *
 * This class abstracts the access to the local data, i.e. it handles data access asynchronously
 * and requests new data from the network should this be required.
 *
 * @param context Application context.
 * @param coinDao Data access object for coin data.
 * @param rateDao Data access object for GOLD exchange rate data.
 */
class CoinRepository(context: Context,
                     private val coinDao: CoinDAO,
                     rateDao: RateDAO): DatabaseUpdater(context, coinDao, rateDao) {

    companion object {
        // Tag to identify log output for this class.
        const val tag = "CoinRepository"

        // Named constants to make code more readable.
        const val COLLECTED = true
        const val NOT_COLLECTED = false
    }

    init {
        // Make sure database is current right away.
        updateDatabase()
    }

    /**
     * Return coin corresponding to given id.
     *
     * Note: Function is executed asynchronously.
     *
     * @param id ID of requested coin.
     *
     * @return Coin corresponding to given ID.
     */
    fun getCoinById(id: String): Coin = coinDao.let {
        updateDatabase()
        GetCoinTask(it).execute(id).get()
    }

    /**
     * Get observable list of all collected coins.
     *
     * Note: Function is executed asynchronously.
     *
     * @return Observable list of all collected coins in local database.
     */
    fun getAllCollected(): LiveData<List<Coin>> = coinDao.let {
        updateDatabase()
        GetCoinsByIsCollectedTask(it).execute(COLLECTED).get()
    }

    /**
     * Get observable list of all uncollected coins.
     *
     * Note: Function is executed asynchronously.
     *
     * @return Observable list of all uncollected coins in local database.
     */
    fun getAllNotCollected(): LiveData<List<Coin>> = coinDao.let {
        updateDatabase()
        GetCoinsByIsCollectedTask(it).execute(NOT_COLLECTED).get()
    }

    /**
     * Set coin corresponding to given ID as collected in local database.
     *
     * Note: Function is executed asynchronously.
     *
     * @param id ID of coin to set as collected.
     */
    fun setCollected(id: String): Unit = coinDao.let { SetCoinCollectedTask(it).execute(id) }

    /**
     * Insert coin into local database.
     *
     * Note: Function is executed asynchronously, but waits for result to avoid race conditions on
     * coins after inserting in case a coin is replaced.
     *
     * Attention: If a coin with identival ID is already in the database it will be replaced!
     *
     * @param id ID of coin to set as collected.
     */
    fun insertCoin(coin: Coin): Unit = coinDao.let { InsertCoinsTask(it).execute(coin).get() }

    /**
     * Remove coin corresponding to given ID from local database.
     *
     * Note: Function is executed asynchronously.
     *
     * @param id ID of coin to remove.
     */
    fun deleteById(id: String): Unit = coinDao.let { DeleteCoinByIdTask(it).execute(id) }

}