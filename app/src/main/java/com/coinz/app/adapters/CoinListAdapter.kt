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

class CoinListAdapter(context: Context,
                      private val childFragmentManager: FragmentManager,
                      private val rateViewModel: RateViewModel,
                      private val goldDatabase: GoldDatabase): RecyclerView.Adapter<CoinListAdapter.CoinViewHolder>() {

    inner class CoinViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val coinCurrency: TextView = itemView.findViewById(R.id.coin_card_currency)
        val coinId: TextView = itemView.findViewById(R.id.coin_card_id)
        val coinValue: TextView = itemView.findViewById(R.id.coin_card_value)
        val coinStoreButton: Button = itemView.findViewById(R.id.coin_store_button)

    }

    private val inflater = LayoutInflater.from(context)

    // Cached copy of coins.
    private var coins = emptyList<Coin>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return CoinViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val currentCoin = coins[position]

        holder.coinCurrency.text = currentCoin.currency
        holder.coinId.text = currentCoin.id
        holder.coinValue.text = currentCoin.storedValue.toString()

        holder.coinStoreButton.setOnClickListener { _ ->
            showStoreCoinDialog(currentCoin.id, currentCoin.currency, currentCoin.storedValue)
        }
    }

    internal fun setCoins(coins: List<Coin>) {
        this.coins = coins
        notifyDataSetChanged()
    }

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

        val collectCoinDialog = StoreCoinDialogFragment.newInstance(coinId, currentGold, updatedGold)

        // NOTE: Could be that putting null here is a problem!
        collectCoinDialog.setTargetFragment(null, 0)
        collectCoinDialog.show(ft, "storeCoinDialog")
    }

}