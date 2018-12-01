package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.RoomCoinDAO

class SetCollectedTask(daoRoom: RoomCoinDAO) : AsyncTask<String, Void, Void>() {

    private var coinDAO = daoRoom

    override fun doInBackground(vararg strs: String): Void? {
        coinDAO.setCollected(strs[0])
        // TODO: is there better way of returning nothing but have return type Void?
        return null
    }

}