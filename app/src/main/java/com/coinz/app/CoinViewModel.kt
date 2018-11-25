package com.coinz.app

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.coinz.app.database.Coin
import com.coinz.app.database.CoinRepository

/*
class CoinViewModel(application: Application): AndroidViewModel(application) {

    private var coinRepository = CoinRepository(application)

    var collectedCoins: LiveData<List<Coin>>? = coinRepository.getAllCollected()
    var notCollectedCoins: LiveData<List<Coin>>? = coinRepository.getAllNotCollected()

    fun insert(coin: Coin) = coinRepository.insert(coin)

}*/