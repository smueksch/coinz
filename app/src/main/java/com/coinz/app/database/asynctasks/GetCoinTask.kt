package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.entities.Coin
import com.coinz.app.database.daos.CoinDAO

class GetCoinTask(dao: CoinDAO) : AsyncTask<String, Void, Coin>() {

    private var coinDao = dao

    override fun doInBackground(vararg ids: String): Coin = coinDao.get(ids[0])

}