package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.CoinDAO

class SetCollectedTask(dao: CoinDAO) : AsyncTask<String, Void, Void>() {

    private var coinDAO = dao

    override fun doInBackground(vararg strs: String): Void? {
        coinDAO.setCollected(strs[0])
        // TODO: is there better way of returning nothing but have return type Void?
        return null
    }

}