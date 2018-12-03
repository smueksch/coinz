package com.coinz.app.database.asynctasks


import android.os.AsyncTask
import com.coinz.app.database.entities.Rate
import com.coinz.app.database.daos.RateDAO

class GetRateByCurrencyTask(dao: RateDAO) : AsyncTask<String, Void, Rate>() {

    private var rateDao = dao

    override fun doInBackground(vararg currencies: String): Rate = rateDao.getByCurrency(currencies[0])

}