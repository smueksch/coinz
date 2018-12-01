package com.coinz.app.database.asynctasks

import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.coinz.app.database.RoomCoin
import com.coinz.app.database.RoomCoinDAO

class GetCoinTask(daoRoom: RoomCoinDAO) : AsyncTask<String, Void, LiveData<RoomCoin>?>() {

    private var coinDao = daoRoom

    override fun doInBackground(vararg ids: String): LiveData<RoomCoin>? = coinDao.get(ids[0])

}