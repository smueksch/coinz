package com.coinz.app

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.coinz.app.database.Coin
import com.coinz.app.database.CoinDatabase
import com.coinz.app.database.CoinRepository

class MapCoinsViewModel(application: Application): AndroidViewModel(application) {

    private var coinRepository: CoinRepository

    var coins: LiveData<List<Coin>>?

    init {
        // TODO: Should this really be a ?. safe call? Can we make it like in tutorial?
        val coinDAO = CoinDatabase.getInstance(application)?.coinDao()
        coinRepository = CoinRepository(application, coinDAO)

        coins = coinRepository.getAllNotCollected()
    }

    fun insert(coin: Coin) = coinRepository.insert(coin)

    fun setCollected(id: String) = coinRepository.setCollected(id)

}