package com.coinz.app.database.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.coinz.app.database.entities.Rate

/**
 * Data access object to GOLD exchange rates database table. Required by Room to access actual GOLD
 * exchange rates.
 */
@Dao
interface RateDAO {

    /**
     * Insert exchange rates into database.
     *
     * NOTE: If rate exists it will be replaced.
     *
     * @param rates Exchange rates to be inserted.
     */
    @Insert(onConflict = REPLACE)
    fun insert(vararg rates: Rate)

    /**
     * Get exchange rate for given currency.
     *
     * @param currency Currency for which to get the exchange rate into GOLD.
     *
     * @return Requested GOLD exchange rate.
     */
    @Query("SELECT * FROM rates WHERE currency = :currency")
    fun getByCurrency(currency: String): Rate

    /**
     * Delete all exchange rates that do not have given validity date.
     *
     * @param validDate Date for which to keep exchange rates, all others deleted.
     */
    @Query("DELETE FROM rates WHERE valid_date != :validDate")
    fun deleteInvalids(validDate: String)

}