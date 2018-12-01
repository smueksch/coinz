package com.coinz.app.database.asynctasks

import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.coinz.app.database.RoomCoin
import com.coinz.app.database.RoomCoinDAO

class GetByIsCollectedTask(daoRoom: RoomCoinDAO) : AsyncTask<Boolean, Void, LiveData<List<RoomCoin>>?>() {

    private var coinDao = daoRoom

    override fun doInBackground(vararg isCollecteds: Boolean?): LiveData<List<RoomCoin>>? =
            when (isCollecteds[0]) {
                true -> coinDao.getAllCollected()
                false -> coinDao.getAllNotCollected()
                else -> null
            }

}