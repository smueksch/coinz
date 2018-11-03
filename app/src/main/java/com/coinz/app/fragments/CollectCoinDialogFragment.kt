package com.coinz.app.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.coinz.app.utils.AppLog
import java.lang.IllegalStateException

// Source: https://developer.android.com/guide/topics/ui/dialogs
class CollectCoinDialogFragment: DialogFragment() {

    companion object {
        const val tag = "CollectCoinDialogFragment"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            builder.apply {
                setMessage("Coin Collection Dialog")
                setPositiveButton("Collect") { dialog, id ->
                    AppLog(CollectCoinDialogFragment.tag, "setPositiveButton", "Collect pressed")
                }
                setNegativeButton("Cancel") { dialog, id ->
                    AppLog(CollectCoinDialogFragment.tag, "setNegativeButton", "Cancel pressed")
                }
            }

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}