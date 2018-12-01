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

// Source: https://developer.android.com/guide/topics/ui/dialogs
class CollectCoinDialogFragment: DialogFragment() {

    companion object {
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

    private lateinit var callback: OnCollectCoinListener

    private var coinCurrency = ""
    private var coinId = ""
    private var coinValue = ""
    private var markerDist = 0.0 // Distance between user and marker in meters.

    private val collectButton: Button by lazy {
        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            // TODO: Can we handle the case where the caller is an activity? Do we need to?
            //callback = activity as OnCollectCoinListener
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

                setPositiveButton(getString(R.string.confirm_coin_collection)) { _, _ ->
                    AppLog(logTag, "onClickPositive", "Collect pressed")

                    AppLog(logTag, "onClickPositive", "Setting Coin with id=$coinId to collected")
                    callback.onCollectCoin(coinId)
                }
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

        if (markerDist > AppConsts.maxCollectDist) {
            // User is too far away from coin, disable the button to collect the coin.
            collectButton.isEnabled = false
        }
    }

}