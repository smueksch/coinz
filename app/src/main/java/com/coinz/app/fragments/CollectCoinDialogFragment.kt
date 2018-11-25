package com.coinz.app.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.Button
import com.coinz.app.R // TODO: Do we really need to import this here?
import com.coinz.app.interfaces.OnCollectCoinListener
import com.coinz.app.utils.AppLog
import com.coinz.app.utils.AppConsts
import com.coinz.app.utils.CollectCoinDialogArgs
import java.lang.IllegalStateException

// Source: https://developer.android.com/guide/topics/ui/dialogs
class CollectCoinDialogFragment: DialogFragment() {

    companion object {
        const val logTag = "CollectCoinDialogFragment"
    }

    private lateinit var callback: OnCollectCoinListener

    // Distance between user and marker in meters.
    private var markerDist = 0.0

    private val collectButton: Button by lazy {
        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            callback = activity as OnCollectCoinListener
        } catch (e: java.lang.ClassCastException) {
            throw ClassCastException("Calling activity must implement OnCollectCoinListener")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            val coinId = arguments?.getCharSequence(CollectCoinDialogArgs.coinId) as String

            // Note: We can't initialize it sooner, as RHS would be null before onCreateDialog.
            markerDist = arguments?.getDouble(CollectCoinDialogArgs.markerDist) as Double

            builder.apply {
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                // NOTE: We must call 'layoutInflater' of 'activity', otherwise the app crashes.
                // See: https://stackoverflow.com/questions/7508185/problem-inflating-custom-view-for-alertdialog-in-dialogfragment
                val view = it.layoutInflater.inflate(R.layout.fragment_collect_coin_dialog, null)

                // TODO: Check if we actually can't use view binding, would be much nicer.
                // TODO: Get values for view as arguments
                // Can't use Kotlin's view binding here as we inflated the view ourselves.
                //view.findViewById<TextView>(R.id.coin_currency_desc).text = coin?.currency ?: ""
                //view.findViewById<TextView>(R.id.coin_value_desc).text = "${coin?.storedValue}"

                //setView(view)

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

        if (markerDist > AppConsts.maxCollectDist) {
            // User is too far away from coin, disable the button to collect the coin.
            collectButton.isEnabled = false
        }
    }

}