package com.coinz.app

import android.arch.lifecycle.LiveData
import android.content.Context
import android.os.AsyncTask

// TODO: Fetching the GeoJSON should happen here IF NEEDED.
// TODO: Should this be a singleton?
class CoinRepository(context: Context) {

    companion object {
        const val COLLECTED = true
        const val NOT_COLLECTED = false

        class GetByIsCollectedTask(dao: CoinDAO) : AsyncTask<Boolean, Void, LiveData<List<Coin>>?>() {

            private var coinDao = dao

            override fun doInBackground(vararg isCollecteds: Boolean?): LiveData<List<Coin>>? =
                when (isCollecteds[0]) {
                    true -> coinDao.getAllCollected()
                    false -> coinDao.getAllNotCollected()
                    else -> null
                }

        }

        class InsertTask(dao: CoinDAO) : AsyncTask<Coin, Void, Void>() {

            private var coinDao = dao

            override fun doInBackground(vararg coins: Coin): Void? {
                coinDao.insert(*coins)
                // TODO: is there better way of returning nothing but have return type Void?
                return null
            }

        }

        class SetCollectedTask(dao: CoinDAO) : AsyncTask<String, Void, Void>() {

            private var coinDAO = dao

            override fun doInBackground(vararg strs: String): Void? {
                coinDAO.setCollected(strs[0])
                // TODO: is there better way of returning nothing but have return type Void?
                return null
            }

        }

        class DeleteInvalidsTask(dao: CoinDAO) : AsyncTask<String, Void, Void>() {

            private var coinDAO = dao

            override fun doInBackground(vararg validDates: String): Void? {
                coinDAO.deleteInvalids(validDates[0])
                // TODO: is there better way of returning nothing but have return type Void?
                return null
            }
        }
    }

    private var coinDao: CoinDAO?
    private var coins: LiveData<List<Coin>>?

    init {
        val db = CoinDatabase.getInstance(context)

        coinDao = db?.coinDao()
        // TODO: This is the crucial bit here. When we initalize the repository we need to think
        // about whether we want to download the new coins or use the old ones. That decision is
        // based on whether the coins are still valid or not.
        // In general, when the repository is created the validity of all coins should be checked.
        coins = coinDao?.getAll()
    }

    fun getAll(): LiveData<List<Coin>>? = coins

    fun getAllCollected(): LiveData<List<Coin>>? = coinDao?.let {
        GetByIsCollectedTask(it).execute(COLLECTED).get()
    }

    fun getAllNotCollected(): LiveData<List<Coin>>? = coinDao?.let {
        GetByIsCollectedTask(it).execute(NOT_COLLECTED).get()
    }

    fun insert(coin: Coin) = coinDao?.let { InsertTask(it).execute(coin) }

    fun setCollected(id: String) = coinDao?.let { SetCollectedTask(it).execute(id) }

}