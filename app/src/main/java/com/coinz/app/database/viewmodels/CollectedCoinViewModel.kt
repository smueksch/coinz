package com.coinz.app.database.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.coinz.app.database.RoomCoin
import com.coinz.app.database.RoomCoinDatabase
import com.coinz.app.database.RoomCoinRepository

class CollectedCoinViewModel(application: Application): AndroidViewModel(application) {

    private var roomCoinRepository: RoomCoinRepository

    var coins: LiveData<List<RoomCoin>>?

    init {
        // TODO: Should this really be a ?. safe call? Can we make it like in tutorial?
        val coinDAO = RoomCoinDatabase.getInstance(application)?.coinDao()
        roomCoinRepository = RoomCoinRepository(application, coinDAO)

        coins = roomCoinRepository.getAllCollected()
    }

    //fun insert(coin: RoomCoin) = roomCoinRepository.insert(coin)

    //fun setCollected(id: String) = roomCoinRepository.setCollected(id)

}