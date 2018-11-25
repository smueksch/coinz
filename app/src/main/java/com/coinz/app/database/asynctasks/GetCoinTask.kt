package com.coinz.app.database.asynctasks

import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.coinz.app.database.Coin
import com.coinz.app.database.CoinDAO

class GetCoinTask(dao: CoinDAO) : AsyncTask<String, Void, LiveData<Coin>?>() {

    private var coinDao = dao

    override fun doInBackground(vararg ids: String): LiveData<Coin>? = coinDao.get(ids[0])

}