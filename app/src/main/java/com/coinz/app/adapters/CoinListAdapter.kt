package com.coinz.app.adapters

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.coinz.app.R
import com.coinz.app.database.GoldDatabase
import com.coinz.app.database.entities.Coin
import com.coinz.app.database.viewmodels.RateViewModel
import com.coinz.app.fragments.StoreCoinDialogFragment

/**
 * Adapter handling the display of a each coin in the local wallet.
 *
 * This adapter essentially encapsulates the view of a each coin in the list of coins displayed in
 * the local wallet. It will manage the reassignment of coins to views should it be required by
 * the RecyclerView. This may happen if a coin gets removed and a different one added.
 *
 * @param context Application context
 * @param childFragmentManager Fragment Manager to create child fragments, used to display dialogs.
 * @param rateViewModel View model encapsulating GOLD exchange rates.
 * @param goldDatabase Remote GOLD database access object.
 */
class CoinListAdapter(context: Context,
                      private val childFragmentManager: FragmentManager,
                      private val rateViewModel: RateViewModel,
                      private val goldDatabase: GoldDatabase): RecyclerView.Adapter<CoinListAdapter.CoinViewHolder>() {

    /**
     * Class holding the view of a single coin.
     *
     * This class essentially manages how each coin is displayed within the list of coins. Each
     * coin in the local wallet will get its own view within the list.
     */
    inner class CoinViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val coinCurrency: TextView = itemView.findViewById(R.id.coin_card_currency)
        val coinId: TextView = itemView.findViewById(R.id.coin_card_id)
        val coinValue: TextView = itemView.findViewById(R.id.coin_card_value)
        val coinStoreButton: Button = itemView.findViewById(R.id.coin_store_button)

    }

    // Layout Inflater, used to inflate layout when view holder is created.
    private val inflater = LayoutInflater.from(context)

    // Cached copy of coins.
    private var coins = emptyList<Coin>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    /**
     * Inflate coin view and create view holder for it.
     *
     * @return View holder for the coin's view.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return CoinViewHolder(itemView)
    }

    /**
     * Bind values to the coin view.
     *
     * Fill the view holder with relevant data from the coin indexed by position.
     *
     * @param holder Container for the view
     * @param position Index of the coin to be assigned to the view.
     */
    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        // Get the coin which will be assigned to the view.
        val currentCoin = coins[position]

        // Fill the view with relevant data from the current coin.
        holder.coinCurrency.text = currentCoin.currency
        holder.coinId.text = currentCoin.id
        holder.coinValue.text = currentCoin.storedValue.toString()

        // Enable the coin storing dialog for the store coin button.
        holder.coinStoreButton.setOnClickListener { _ ->
            showStoreCoinDialog(currentCoin.id, currentCoin.currency, currentCoin.storedValue)
        }
    }

    /**
     * Set the cached coins.
     *
     * @param coins New list of cached coins.
     */
    internal fun setCoins(coins: List<Coin>) {
        this.coins = coins
        notifyDataSetChanged()
    }

    /**
     * Get number of cached coins.
     *
     * @return Number of cached coins.
     */
    override fun getItemCount(): Int = coins.size

    /**
     * Show dialog to store coin in central bank.
     *
     * @param coinId ID of coin to be stored.
     * @param coinCurrency Currency of coin to be stored.
     * @param coinValue Value of coin to be stored.
     */
    private fun showStoreCoinDialog(coinId: String, coinCurrency: String, coinValue: Double) {
        val ft = childFragmentManager.beginTransaction()
        val previous = childFragmentManager.findFragmentByTag("storeCoinDialog")
        previous?.let {
            ft.remove(it)
        }
        ft.addToBackStack(null)

        // Get current GOLD amount.
        val currentGold = goldDatabase.getGold()

        // Compute how much GOLD will be in user's central bank once the coin is banked.
        val exchangeRate = rateViewModel.getRateByCurrency(coinCurrency)
        val updatedGold = currentGold + coinValue * exchangeRate.rate

        // Create appropriate dialog to store the coin.
        val collectCoinDialog = StoreCoinDialogFragment.newInstance(coinId, currentGold, updatedGold)

        // Show the store coin dialog.
        // NOTE: Could be that putting null here is a problem!
        collectCoinDialog.setTargetFragment(null, 0)
        collectCoinDialog.show(ft, "storeCoinDialog")
    }

}