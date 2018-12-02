package com.coinz.app.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.TextView
import com.coinz.app.R
import com.coinz.app.interfaces.OnStoreCoinListener
import com.coinz.app.utils.AppLog
import java.lang.IllegalStateException

class StoreCoinDialogFragment : DialogFragment() {

    companion object {
        const val logTag = "StoreCoinDialogFragment"

        /**
         * Initialize a new store coin dialog.
         *
         * Convenience function to populate a StoreCoinDialog with parameters without having
         * to go through Fragment's put* and get* routines.
         *
         * @param coinId ID of coin to be stored.
         * @param currGold Current amount of GOLD in central bank.
         * @param newGold New amount of GOLD in central bank if coin is stored.
         */
        fun newInstance(coinId: String/*, currGold: Double, newGold: Double*/): StoreCoinDialogFragment {
            val dialog = StoreCoinDialogFragment()

            dialog.coinId = coinId
            // TODO: Enable showing the current and new amount of gold in dialog.
            /*
            dialog.currGold = currGold
            dialog.newGold = newGold
            */

            return dialog
        }
    }

    private var coinId = ""
    private var currGold = 0.0
    private var newGold = 0.0

    private lateinit var callback: OnStoreCoinListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            // TODO: Can we handle the case where the caller is an activity? Do we need to?
            callback = parentFragment as OnStoreCoinListener
        } catch (e: java.lang.ClassCastException) {
            throw ClassCastException("Calling activity must implement OnStoreCoinListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            builder.apply {
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                // NOTE: We must call 'layoutInflater' of 'activity', otherwise the app crashes.
                // See: https://stackoverflow.com/questions/7508185/problem-inflating-custom-view-for-alertdialog-in-dialogfragment
                val view = it.layoutInflater.inflate(R.layout.fragment_store_coin_dialog, null)

                /*
                // Populate the collection dialog.
                view.findViewById<TextView>(R.id.store_coin_curr_gold).text = currGold.toString()
                view.findViewById<TextView>(R.id.store_coin_new_gold).text = newGold.toString()
                */

                setView(view)

                setPositiveButton(getString(R.string.store_coin_store_button)) { _, _ ->
                    AppLog(logTag, "onClickPositive", "Collect pressed")

                    AppLog(logTag, "onClickPositive", "Storing Coin with id=$coinId in central bank")
                    callback.onStoreCoin(coinId)
                }
                setNegativeButton(getString(R.string.store_coin_cancel_button)) { _, _ ->
                    AppLog(logTag, "onClickNegative", "Cancel pressed")
                    dialog.cancel()
                }

            }

            // Create the AlertDialog object and return it
            val dialog = builder.create()

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}