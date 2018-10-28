package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.Coin
import com.coinz.app.database.CoinDAO

class InsertTask(dao: CoinDAO) : AsyncTask<Coin, Void, Void>() {

    private var coinDao = dao

    override fun doInBackground(vararg coins: Coin): Void? {
        coinDao.insert(*coins)
        // TODO: is there better way of returning nothing but have return type Void?
        return null
    }

}