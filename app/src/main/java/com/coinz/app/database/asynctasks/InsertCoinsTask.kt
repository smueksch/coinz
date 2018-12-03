package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.entities.Coin
import com.coinz.app.database.daos.CoinDAO
import com.coinz.app.utils.AppLog

class InsertCoinsTask(dao: CoinDAO) : AsyncTask<Coin, Void, Void>() {

    companion object {
        const val tag = "InsertCoinsTask"
    }

    private var coinDao = dao

    override fun doInBackground(vararg coins: Coin): Void? {
        AppLog(tag, "doInBackground", "coins[0]=")
        coinDao.insert(*coins)
        // TODO: is there better way of returning nothing but have return type Void?
        return null
    }

}