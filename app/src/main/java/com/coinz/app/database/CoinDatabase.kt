package com.coinz.app.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

import com.coinz.app.utils.AppConsts

@Database(entities = arrayOf(Coin::class), version = 1)
abstract class CoinDatabase : RoomDatabase() {

    companion object {
        private var INSTANCE: CoinDatabase? = null

        fun getInstance(context: Context): CoinDatabase? {
            if (INSTANCE == null) {
                synchronized(CoinDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                                                    CoinDatabase::class.java,
                                                    AppConsts.coinDbName).build()
                }
            }

            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

    abstract fun coinDao(): CoinDAO

}