package com.coinz.app.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.Button
import android.widget.TextView
import com.coinz.app.R
import com.coinz.app.interfaces.OnCollectCoinListener
import com.coinz.app.utils.AppLog
import com.coinz.app.utils.AppConsts
import java.lang.IllegalStateException

/**
 * Dialog handling coin collection.
 *
 * This dialog is invoked whenever the user clicks on a marker on the map. It displays relevant
 * information about the coin and gives the user a choice of whether to collect the coin, given
 * the user is in range, or not.
 *
 * Adapted from:
 * https://developer.android.com/guide/topics/ui/dialogs
 */
class CollectCoinDialogFragment: DialogFragment() {

    companion object {
        // Tag identifying log output from this fragment.
        const val logTag = "CollectCoinDialogFragment"

        /**
         * Initialize new instance of collection dialog.
         *
         * Convenience function to populate a CollectCoinDialogFragment with parameters without
         * having to go through Fragment's put* and get* routines.
         *
         * @param coinCurrency Coin currency
         * @param coinId Coin ID
         * @param coinValue Coin value
         * @param markerDist Distance from user to marker in meters
         *
         * @return New CollectCoinDialogFragment initialized with given parameters.
         */
        fun newInstance(coinCurrency: String, coinId: String, coinValue: Double,
                        markerDist: Double): CollectCoinDialogFragment {
            val collectDialog = CollectCoinDialogFragment()

            collectDialog.coinCurrency = coinCurrency
            collectDialog.coinId = coinId
            collectDialog.coinValue = coinValue.toString()
            collectDialog.markerDist = markerDist

            return collectDialog
        }
    }

    // Callback object notified when a coin is collected.
    private lateinit var callback: OnCollectCoinListener

    // Coin related data displayed to the user when he clicks on a marker.
    private var coinCurrency = ""
    private var coinId = ""
    private var coinValue = ""

    // Distance between user and marker in meters.
    private var markerDist = 0.0

    // Reference to the button that collects the coin when pressed, used to disable this button
    // in case the user is not in range of the coin.
    private val collectButton: Button by lazy {
        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the coin collect callback.
        try {
            callback = parentFragment as OnCollectCoinListener
        } catch (e: java.lang.ClassCastException) {
            throw ClassCastException("Calling activity must implement OnCollectCoinListener")
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
                val view = it.layoutInflater.inflate(R.layout.fragment_collect_coin_dialog, null)

                // Populate the collection dialog.
                view.findViewById<TextView>(R.id.coin_collect_currency).text = coinCurrency
                view.findViewById<TextView>(R.id.coin_collect_id).text = coinId
                view.findViewById<TextView>(R.id.coin_collect_value).text = coinValue

                val markerDistStr = "%.2f".format(markerDist)
                view.findViewById<TextView>(R.id.coin_collect_dist).text = markerDistStr

                setView(view)

                // Set collect button action.
                setPositiveButton(getString(R.string.confirm_coin_collection)) { _, _ ->
                    AppLog(logTag, "onClickPositive", "Collect pressed")

                    AppLog(logTag, "onClickPositive", "Setting Coin with id=$coinId to collected")
                    callback.onCollectCoin(coinId)
                }
                // Set cancel button action.
                setNegativeButton(getString(R.string.cancel_coin_collection)) { _, _ ->
                    AppLog(logTag, "onClickNegative", "Cancel pressed")
                    dialog.cancel()
                }

            }

            // Create the AlertDialog object and return it
            val dialog = builder.create()

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onStart() {
        super.onStart()

        AppLog(logTag, "onStart", "markerDist=$markerDist")

        // Check whether user is in range to collect the coin they clicked on.
        if (markerDist > AppConsts.maxCollectDist) {
            // User is too far away from coin, disable the button to collect the coin.
            collectButton.isEnabled = false
        }
    }

}