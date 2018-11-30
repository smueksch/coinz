package com.coinz.app.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.coinz.app.R
import com.coinz.app.adapters.CoinListAdapter
import com.coinz.app.database.viewmodels.CollectedCoinViewModel

class LocalWalletFragment : Fragment() {

    companion object {
        fun newInstance(): LocalWalletFragment {
            return LocalWalletFragment()
        }
    }

    // Have to wait with initialization until fragment is attached to an activity.
    private lateinit var associatedContext: Context

    private lateinit var coinViewModel: CollectedCoinViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // Throw exceptions if we don't have associated context as we need it for proper
        // operation.
        associatedContext = requireContext()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_local_wallet, container, false)

        val recyclerView = fragmentView.findViewById<RecyclerView>(R.id.coin_recyclerview)
        val adapter = CoinListAdapter(associatedContext)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(associatedContext)

        // Add observer to the coin data.
        coinViewModel = ViewModelProviders.of(this).get(CollectedCoinViewModel::class.java)
        // TODO: coins should not be nullable -> adjust according to tutorial:
        // https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#8
        coinViewModel.coins?.observe(this, Observer { coins ->
            // Update the cached copy of the coins in adapter.
            coins?.let { adapter.setCoins(it) }
        })

        return fragmentView
    }

}