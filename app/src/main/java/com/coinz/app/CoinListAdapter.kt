package com.coinz.app

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.coinz.app.database.Coin

class CoinListAdapter(context: Context): RecyclerView.Adapter<CoinListAdapter.CoinViewHolder>() {

    // TODO: Should this be a dataclass?
    class CoinViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val coinItemView: TextView = itemView.findViewById(R.id.textView)

    }

    private val layoutInflater = LayoutInflater.from(context)
    private var coins: List<Coin>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder =
        CoinViewHolder(layoutInflater.inflate(R.layout.recyclerview_item, parent, false))

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) =
        coins?.let {
            // NOTE: need 'let' syntax here instead of 'when' as we would have to cast 'coins' to
            // 'List<Coin>' explicitly.
            holder.coinItemView.text = it[position].id
        } ?: holder.coinItemView.setText("No Coin") // Covers data not ready case.

    override fun getItemCount(): Int = coins?.size ?: 0 // Initial value is 0.

}