package com.coinz.app

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.coinz.app.database.Coin

class CoinListAdapter(context: Context): RecyclerView.Adapter<CoinListAdapter.CoinViewHolder>() {

    inner class CoinViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val coinItemView: TextView = itemView.findViewById(R.id.textView)

    }

    private val layoutInflater = LayoutInflater.from(context)

    // Cached copy of coins.
    private var coins = emptyList<Coin>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder =
        CoinViewHolder(layoutInflater.inflate(R.layout.recyclerview_item, parent, false))

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.coinItemView.text = coins[position].id
    }

    override fun getItemCount(): Int = coins.size

}