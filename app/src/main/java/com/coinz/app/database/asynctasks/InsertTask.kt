package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.Coin
import com.coinz.app.database.CoinDAO
import com.coinz.app.utils.AppLog

class InsertTask(dao: CoinDAO) : AsyncTask<Coin, Void, Void>() {

    companion object {
        const val tag = "InsertTask"
    }

    private var coinDao = dao

    override fun doInBackground(vararg coins: Coin): Void? {
        AppLog(tag, "doInBackground", "coins[0]=")
        coinDao.insert(*coins)
        // TODO: is there better way of returning nothing but have return type Void?
        return null
    }

}