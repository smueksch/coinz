package com.coinz.app.fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.coinz.app.R
import com.coinz.app.database.GoldDatabase
import com.coinz.app.database.viewmodels.RateViewModel
import com.google.firebase.auth.FirebaseAuth

class CentralBankFragment : Fragment() {

    companion object {
        const val logTag = "CentralBankFragment"

        fun newInstance(): CentralBankFragment {
            return CentralBankFragment()
        }
    }

    private lateinit var auth: FirebaseAuth
    //private lateinit var goldDatabase: GoldDatabase

    // Have to wait with initialization until fragment is attached to an activity.
    private lateinit var associatedContext: Context

    private lateinit var rateViewModel: RateViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // Throw exceptions if we don't have associated context as we need it for proper
        // operation.
        associatedContext = requireContext()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Note: If we're in the central bank then we will have required log in earlier, so we
        // should always have a user at this stage.
        /*
        val user = auth.currentUser?.email ?: ""
        goldDatabase = GoldDatabase(user)*/

        rateViewModel = ViewModelProviders.of(this).get(RateViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_central_bank, container, false)

        /*
        val currentGold = goldDatabase.getGold()
        fragmentView.findViewById<TextView>(R.id.central_bank_gold).text = currentGold.toString()
        */

        // Add observer to the coin data.
        /* TODO: Change this to work with rateViewModel
        coinViewModel = ViewModelProviders.of(this).get(CollectedCoinViewModel::class.java)
        coinViewModel.coins.observe(this, Observer { coins ->
            // Update the cached copy of the coins in adapter.
            coins?.let { adapter.setCoins(it) }
        })*/

        return fragmentView
    }

}