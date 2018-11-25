package com.coinz.app.database.asynctasks

import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.coinz.app.database.Coin
import com.coinz.app.database.CoinDAO

class GetByIsCollectedTask(dao: CoinDAO) : AsyncTask<Boolean, Void, LiveData<List<Coin>>?>() {

    private var coinDao = dao

    override fun doInBackground(vararg isCollecteds: Boolean?): LiveData<List<Coin>>? =
            when (isCollecteds[0]) {
                true -> coinDao.getAllCollected()
                false -> coinDao.getAllNotCollected()
                else -> null
            }

}