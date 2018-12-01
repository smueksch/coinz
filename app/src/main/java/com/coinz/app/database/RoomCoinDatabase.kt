package com.coinz.app.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

import com.coinz.app.utils.AppConsts

@Database(entities = arrayOf(RoomCoin::class), version = 1)
abstract class RoomCoinDatabase : RoomDatabase() {

    companion object {
        private var INSTANCE: RoomCoinDatabase? = null

        fun getInstance(context: Context): RoomCoinDatabase? {
            if (INSTANCE == null) {
                synchronized(RoomCoinDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                                                    RoomCoinDatabase::class.java,
                                                    AppConsts.coinDbName).build()
                }
            }

            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

    abstract fun coinDao(): RoomCoinDAO

}