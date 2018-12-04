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
import com.coinz.app.database.viewmodels.RateViewModel
import com.coinz.app.interfaces.OnStoreCoinListener
import com.coinz.app.utils.AppLog
import com.google.firebase.auth.FirebaseAuth

/**
 * Fragment displaying all coins stored in the local wallet.
 *
 * This fragment displays all currently collected coins in a list of card views, one card per coin.
 */
class LocalWalletFragment : Fragment(), OnStoreCoinListener {

    companion object {
        // Tag identifying log output from this fragment.
        const val logTag = "LocalWalletFragment"

        /**
         * Create new instance of fragment.
         *
         * @return Instance of local wallet fragment.
         */
        fun newInstance(): LocalWalletFragment {
            return LocalWalletFragment()
        }
    }

    // Firebase authenticator object, used to access Firbase's account management.
    private lateinit var auth: FirebaseAuth
    //private lateinit var goldDatabase: GoldDatabase

    // Context of activity this fragment is associated to, use whenever a function requires a
    // context.
    // Have to wait with initialization until fragment is attached to an activity.
    private lateinit var associatedContext: Context

    // View models holding and giving access to coin and GOLD exchange rate data.
    private lateinit var coinViewModel: CollectedCoinViewModel
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

        // Note: If we're in the Local Wallet then we will have required log in earlier, so we
        // should always have a user at this stage.
        //val user = auth.currentUser?.email ?: ""
        //goldDatabase = GoldDatabase(user) // DISABLED AS IT CAUSED CRASHES

        // Initialize GOLD exchange rate view model.
        rateViewModel = ViewModelProviders.of(this).get(RateViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_local_wallet, container, false)

        // Initialize the recycler view used to display the coins in a list.
        val recyclerView = fragmentView.findViewById<RecyclerView>(R.id.coin_recyclerview)
        val adapter = CoinListAdapter(associatedContext, childFragmentManager, rateViewModel/*,
                                      goldDatabase*/) // goldDatabse disabled as it cause crashes.

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(associatedContext)

        // Add observer to the coin data.
        coinViewModel = ViewModelProviders.of(this).get(CollectedCoinViewModel::class.java)
        coinViewModel.coins.observe(this, Observer { coins ->
            // Update the cached copy of the coins in adapter.
            coins?.let { adapter.setCoins(it) }
        })

        return fragmentView
    }

    /**
     * Callback for case that is requested to be stored.
     *
     * This callback is invoked in case the user requests to store a coin through the appropriate
     * dialog. It will remove the coin from the local wallet and update the GOLD in the central
     * bank accordingly.
     *
     * @param coinId ID of coin to be stored in central bank.
     */
    override fun onStoreCoin(coinId: String) {
        AppLog(logTag, "onStoreCoin", "called with coinId=$coinId")

        // Retrieve coin to be able to update the central bank's GOLD amount appropriately.
        val coin = coinViewModel.getCoinById(coinId)
        AppLog(logTag, "onStoreCoin", "retrieve coin has ID=${coin.id}")

        // Retrieve GOLD exchange rate for the coin.
        val exchangeRate = rateViewModel.getRateByCurrency(coin.currency)
        AppLog(logTag, "onStoreCoin", "GOLD exchange rate for ${coin.currency} is ${exchangeRate.rate}")

        /* DISABLED AS IT CAUSED CRASHES. Original purpose was to update the remote GOLD value.
        val exchangeGold = coin.storedValue * exchangeRate.rate

        val currentGold = goldDatabase.getGold()
        val currentNumStoredCoins = goldDatabase.getNumStoredCoins()

        AppLog(logTag, "onStoreCoin", "user currently has $currentGold GOLD")

        // Update the amount of GOLD in central bank as well as how many coins the user has stored
        // today.
        goldDatabase.setGold(currentGold + exchangeGold)
        goldDatabase.setNumStoredCoins(currentNumStoredCoins + 1)
        */

        // Remove coin that we just stored in central bank from our local wallet
        coinViewModel.deleteCoinById(coinId)
    }

}