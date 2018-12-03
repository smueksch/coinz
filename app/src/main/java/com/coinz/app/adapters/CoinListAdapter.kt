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
import com.coinz.app.database.entities.Coin
import com.coinz.app.fragments.StoreCoinDialogFragment

class CoinListAdapter(context: Context,
                      private val childFragmentManager: FragmentManager): RecyclerView.Adapter<CoinListAdapter.CoinViewHolder>() {

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

        holder.coinStoreButton.setOnClickListener { _ -> showStoreCoinDialog(currentCoin.id) }
    }

    internal fun setCoins(coins: List<Coin>) {
        this.coins = coins
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = coins.size

    private fun showStoreCoinDialog(coinId: String) {
        val ft = childFragmentManager.beginTransaction()
        val previous = childFragmentManager.findFragmentByTag("storeCoinDialog")
        previous?.let {
            ft.remove(it)
        }
        ft.addToBackStack(null)

        val collectCoinDialog = StoreCoinDialogFragment.newInstance(coinId)

        // NOTE: Could be that putting null here is a problem!
        collectCoinDialog.setTargetFragment(null, 0)
        collectCoinDialog.show(ft, "storeCoinDialog")
    }

}