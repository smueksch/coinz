package com.coinz.app.interfaces

/**
 * Interface enabling reactive behavior to a coin being requested to be stored in the central bank.
 */
interface OnStoreCoinListener {
    /**
     * Function called when a coin is requested to be stored in central bank.
     *
     * @param coinId ID of coin requested to be stored.
     */
    fun onStoreCoin(coinId: String)
}