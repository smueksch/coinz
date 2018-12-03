package com.coinz.app.database.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.coinz.app.database.entities.Coin

/**
 * Data access object to coin table. Required by Room.
 */
@Dao
interface CoinDAO {

    /**
     * Get coin for given id.
     *
     * @param id ID of requested coin.
     *
     * @return Requested coin.
     */
    @Query("SELECT * FROM coins WHERE id = :id")
    fun get(id: String): Coin


    @Query("SELECT * FROM coins")
    fun getAll(): LiveData<List<Coin>>

    @Query("SELECT * FROM coins WHERE is_collected = 1")
    fun getAllCollected(): LiveData<List<Coin>>

    @Query("SELECT * FROM coins WHERE is_collected = 0")
    fun getAllNotCollected(): LiveData<List<Coin>>

    @Insert(onConflict = REPLACE)
    fun insert(vararg coins: Coin)

    /**
     * Mark given coin as collected.
     *
     * Note: Since Room uses SQLite under the hood we cannot use booleans directly, so TRUE is 1
     * and FALSE is 0.
     *
     * @param id ID of coin to be marked as collected.
     */
    @Query("UPDATE coins SET is_collected = 1 WHERE id = :id")
    fun setCollected(id: String)

    /**
     * Delete all coins that do not have given validity date.
     *
     * @param validDate Date for which to keep coins, all others deleted.
     */
    @Query("DELETE FROM coins WHERE valid_date != :validDate")
    fun deleteInvalids(validDate: String)

    /**
     * Delete coin with given id.
     *
     * @param id ID of coin to be deleted.
     */
    @Query("DELETE FROM coins WHERE id = :id")
    fun deleteById(id: String)
}