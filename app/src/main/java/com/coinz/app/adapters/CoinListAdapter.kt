package com.coinz.app.adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.coinz.app.R
import com.coinz.app.database.Coin

class CoinListAdapter(context: Context): RecyclerView.Adapter<CoinListAdapter.CoinViewHolder>() {

    inner class CoinViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val coinCurrency: TextView = itemView.findViewById(R.id.coin_card_currency)
        val coinId: TextView = itemView.findViewById(R.id.coin_card_id)
        val coinValue: TextView = itemView.findViewById(R.id.coin_card_value)

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
    }

    internal fun setCoins(coins: List<Coin>) {
        this.coins = coins
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = coins.size

}