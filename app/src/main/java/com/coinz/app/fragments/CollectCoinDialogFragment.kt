package com.coinz.app.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.TextView
import com.coinz.app.R // TODO: Do we really need to import this here?
import com.coinz.app.database.CoinRepository
import com.coinz.app.utils.AppLog
import java.lang.IllegalStateException

// Source: https://developer.android.com/guide/topics/ui/dialogs
class CollectCoinDialogFragment: DialogFragment() {

    companion object {
        const val logTag = "CollectCoinDialogFragment"
    }

    private lateinit var coinRepository: CoinRepository

    override fun onAttach(context: Context) {
        super.onAttach(context)

        coinRepository = CoinRepository(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            /*
            val currency = arguments?.getCharSequence("currency")
            val value = arguments?.getCharSequence("value")
            val latitude = arguments?.getDouble("latitude")
            val longitude = arguments?.getDouble("longitude")
            */
            val coinId = arguments?.getCharSequence("coin_id") as String

            val coin = coinRepository.getCoin(coinId)

            builder.apply {
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                // NOTE: We must call 'layoutInflater' of 'activity', otherwise the app crashes.
                // See: https://stackoverflow.com/questions/7508185/problem-inflating-custom-view-for-alertdialog-in-dialogfragment
                val view = it.layoutInflater.inflate(R.layout.fragment_collect_coin_dialog, null)

                view.findViewById<TextView>(R.id.coin_currency_desc).text = coin?.currency ?: ""
                view.findViewById<TextView>(R.id.coin_value_desc).text = "${coin?.storedValue}"

                setView(view)

                setPositiveButton(getString(R.string.confirm_coin_collection)) { _, _ ->
                    AppLog(logTag, "onClickPositive", "Collect pressed")

                    // TODO: this should only be the case if user is within acceptable distance of coin.
                    AppLog(logTag, "onClickPositive", "Setting Coin with id=$coinId to collected")
                    coinRepository.setCollected(coinId)
                }
                setNegativeButton(getString(R.string.cancel_coin_collection)) { _, _ ->
                    AppLog(logTag, "onClickNegative", "Cancel pressed")
                }

            }

            // Create the AlertDialog object and return it
            val dialog = builder.create()
            //dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false)

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}