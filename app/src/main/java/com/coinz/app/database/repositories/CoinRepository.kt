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
 */
class CoinRepository(context: Context,
                     private val coinDao: CoinDAO,
                     rateDao: RateDAO): DatabaseUpdater(context, coinDao, rateDao) {

    companion object {
        const val tag = "CoinRepository"
        const val COLLECTED = true
        const val NOT_COLLECTED = false
    }

    init {
        // Make sure database is current right away.
        updateDatabase()
    }

    fun getCoinById(id: String): Coin = coinDao.let {
        updateDatabase()
        GetCoinTask(it).execute(id).get()
    }

    fun getAllCollected(): LiveData<List<Coin>> = coinDao.let {
        updateDatabase()
        GetCoinsByIsCollectedTask(it).execute(COLLECTED).get()
    }

    fun getAllNotCollected(): LiveData<List<Coin>> = coinDao.let {
        updateDatabase()
        GetCoinsByIsCollectedTask(it).execute(NOT_COLLECTED).get()
    }

    fun setCollected(id: String): Unit = coinDao.let { SetCoinCollectedTask(it).execute(id) }

    fun deleteById(id: String): Unit = coinDao.let { DeleteCoinByIdTask(it).execute(id) }

}