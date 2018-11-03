package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.Coin
import com.coinz.app.database.CoinDAO

class GetByIsCollectedTask(dao: CoinDAO) : AsyncTask<Boolean, Void, List<Coin>?>() {

    private var coinDao = dao

    override fun doInBackground(vararg isCollecteds: Boolean?): List<Coin>? =
            when (isCollecteds[0]) {
                true -> coinDao.getAllCollected()
                false -> coinDao.getAllNotCollected()
                else -> null
            }

}