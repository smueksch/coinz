package com.coinz.app

import android.app.Application
import android.arch.lifecycle.LiveData

// TODO: Fetching the GeoJSON should happen here IF NEEDED.
class CoinRepository(application: Application) {

    private var coinDao: CoinDAO?
    private var coins: LiveData<List<Coin>>?

    init {
        val db = CoinDatabase.getInstance(application)

        coinDao = db?.coinDao()
        coins = coinDao?.getAll()
    }

    // TODO: Async tasks for inserting, changing, deleting
    // See: https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#7

}