package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.RoomCoin
import com.coinz.app.database.RoomCoinDAO
import com.coinz.app.utils.AppLog

class InsertTask(daoRoom: RoomCoinDAO) : AsyncTask<RoomCoin, Void, Void>() {

    companion object {
        const val tag = "InsertTask"
    }

    private var coinDao = daoRoom

    override fun doInBackground(vararg roomCoins: RoomCoin): Void? {
        AppLog(tag, "doInBackground", "roomCoins[0]=")
        coinDao.insert(*roomCoins)
        // TODO: is there better way of returning nothing but have return type Void?
        return null
    }

}