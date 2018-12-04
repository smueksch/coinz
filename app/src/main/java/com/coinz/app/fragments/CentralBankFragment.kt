package com.coinz.app.fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.coinz.app.R
import com.coinz.app.database.viewmodels.RateViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * Fragment giving a view into the central bank.
 *
 * This fragment displays data taken from the central bank, e.g. the current GOLD amount for the
 * given user.
 */
class CentralBankFragment : Fragment() {

    companion object {
        /**
         * Create new instance of fragment.
         *
         * @return Instance of central bank fragment.
         */
        fun newInstance(): CentralBankFragment {
            return CentralBankFragment()
        }
    }

    // Firebase authenticator object, used to access Firbase's account management.
    private lateinit var auth: FirebaseAuth
    // Instance of GOLD database, used to access remote user data related to their GOLD amount.
    //private lateinit var goldDatabase: GoldDatabase

    // Context of associated activity, used to launch functions that require a context.
    // Have to wait with initialization until fragment is attached to an activity.
    private lateinit var associatedContext: Context

    // View model holding the GOLD exchange rates and giving access to them.
    private lateinit var rateViewModel: RateViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // Throw exceptions if we don't have associated context as we need it for proper
        // operation.
        associatedContext = requireContext()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize access to Firebase's account management.
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
        //val fragmentView = inflater.inflate(R.layout.fragment_central_bank, container, false)

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

        return inflater.inflate(R.layout.fragment_central_bank, container, false)
    }

}