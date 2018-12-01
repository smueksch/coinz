package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.database.RoomCoinDAO

/**
 * Task to delete all coins that do not have the given validity date.
 */
class DeleteInvalidsTask(daoRoom: RoomCoinDAO) : AsyncTask<String, Void, Void>() {

    private var coinDAO = daoRoom

    /**
     * Delete coins that do not have given validity date.
     *
     * @param validDates First entry is validity date, deletes all coins with different date.
     */
    override fun doInBackground(vararg validDates: String): Void? {
        coinDAO.deleteInvalids(validDates[0])
        // TODO: is there better way of returning nothing but have return type Void?
        return null
    }
}
