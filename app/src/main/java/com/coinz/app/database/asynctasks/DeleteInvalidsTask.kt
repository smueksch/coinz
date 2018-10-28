package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.CoinDAO

class DeleteInvalidsTask(dao: CoinDAO) : AsyncTask<String, Void, Void>() {

    private var coinDAO = dao

    override fun doInBackground(vararg validDates: String): Void? {
        coinDAO.deleteInvalids(validDates[0])
        // TODO: is there better way of returning nothing but have return type Void?
        return null
    }
}