package com.coinz.app.database

import com.coinz.app.utils.AppLog
import com.google.firebase.firestore.*

/**
 * Database giving access to remotely stored gold values.
 *
 * @param user User for which to access the database.
 */
class GoldDatabase(private val user: String) {

    /**
     * Define the Firestore structure of the database related to storing gold values for users.
     */
    companion object {
        const val tag = "GoldDatabase"

        // Collection key.
        const val COLLECTION = "gold"
        // Key of field storing user's coins
        const val GOLD = "gold"
        // Key of field storing how many coins user has banked.
        const val NUM_STORED_COINS = "num-stored-coins"
        // Valid data for the number of stored coins.
        const val VALID_DATE = "valid-date"
    }

    // Database access object.
    private val db: CollectionReference
    private val document: DocumentReference

    // User's GOLD amount in the central bank.
    private var gold = 0.0

    // Amount of coins user has stored today.
    private var numStoredCoins: Long = 0

    // Date for which numStoredCoins is valid.
    private var validDate = ""

    init {
        db = FirebaseFirestore.getInstance().collection(COLLECTION)
        document = db.document(user)

        // Initial request of user data from central bank.
        document.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val gold = document[GOLD] as Double
                        val numStoredCoins = document[NUM_STORED_COINS] as Long
                        val validDate = document[VALID_DATE] as String


                        AppLog(tag, "init", "Retrieved amount of gold for $user: $gold")
                        AppLog(tag, "init", "Retrieved number of stored coins for $user: $numStoredCoins")
                        AppLog(tag, "init", "numStoredCoins valid date for $user: $validDate")

                        this.gold = gold
                        this.numStoredCoins = numStoredCoins
                        this.validDate = validDate
                    } else {
                        AppLog(tag, "init", "No document $user found!")
                    }
                }
                .addOnFailureListener { exception ->
                    AppLog(tag, "init", exception.message ?: "Failed to read GOLD for $user")
                }

        // Add a listener for updates in the data.
        document.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                AppLog(tag, "snapshotListener", exception.message ?: "Exception occurred listening for snapshots")
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                // There's new data, store it locally.
                AppLog(tag, "snapshotListener", "New data retrieved.")

                this.gold = snapshot[GOLD] as Double
                this.numStoredCoins = snapshot[NUM_STORED_COINS] as Long
                this.validDate = snapshot[VALID_DATE] as String
            }
        }
    }

    fun getGold() = gold

    /**
     * Set GOLD value locally and in central bank.
     *
     * Note: We define this separately, i.e. not as 'set()' for gold, because we want to be able to
     * internally set the gold value without causing a database update.
     *
     * @param gold New GOLD value.
     */
    fun setGold(gold: Double) {
        AppLog(tag, "setGold", "Setting GOLD for $user to $gold")
        db.document(user).update(GOLD, gold)

        this.gold = gold
    }

    fun getNumStoredCoins() = numStoredCoins

    /**
     * Set number of stored coins locally and in central bank.
     *
     * Note: We define this separately, i.e. not as 'set()' for numStoredCoins, because we want to
     * be able to internally set the gold value without causing a database update.
     *
     * @param numStoredCoins New number of stored coins in central bank.
     */
    fun setNumStoredCoins(numStoredCoins: Long) {
        AppLog(tag, "setNumStoredCoins", "Setting number of stored coins for $user to $numStoredCoins")
        db.document(user).update(NUM_STORED_COINS, numStoredCoins)

        this.numStoredCoins = numStoredCoins
    }

    fun getValidDate() = validDate

    /**
     * Set valid date for number of stored coins locally and in central bank.
     *
     * Note: We define this separately, i.e. not as 'set()' for validDate, because we want to
     * be able to internally set the gold value without causing a database update.
     *
     * @param validDate New valid date for number of stored coins in central bank.
     */
    fun setValidDate(validDate: String) {
        AppLog(tag, "setValidDate", "Setting valid date for $user to $validDate")
        db.document(user).update(VALID_DATE, validDate)

        this.validDate = validDate
    }

}