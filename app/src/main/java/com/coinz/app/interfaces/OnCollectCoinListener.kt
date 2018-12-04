package com.coinz.app.interfaces

/**
 * Interface enabling reactive behavior to a coin being collected.
 */
interface OnCollectCoinListener {
    /**
     * Function called when a coin is collected.
     *
     * @param id ID of coin that has been collected.
     */
    fun onCollectCoin(id: String)
}