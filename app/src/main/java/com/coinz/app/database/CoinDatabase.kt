package com.coinz.app.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.coinz.app.database.daos.CoinDAO
import com.coinz.app.database.daos.RateDAO
import com.coinz.app.database.entities.Coin
import com.coinz.app.database.entities.Rate

import com.coinz.app.utils.AppConsts

/**
 * Database object to access locally stored data.
 *
 * Note: This class will be properly handled by Room in the background and will give us access to
 * our local data.
 *
 * Implemented as singleton per Room requirement.
 */
@Database(entities = arrayOf(Coin::class, Rate::class), version = 2)
abstract class CoinDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: CoinDatabase? = null

        /**
         * Get instance of the database singleton.
         *
         * @param context Application context.
         *
         * @return Reference to database instance.
         */
        fun getInstance(context: Context): CoinDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                                                    CoinDatabase::class.java,
                                                    AppConsts.coinDbName).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    abstract fun coinDao(): CoinDAO

    abstract fun rateDao(): RateDAO

}