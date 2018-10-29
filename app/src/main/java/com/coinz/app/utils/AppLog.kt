package com.coinz.app.utils

import android.util.Log

/**
 * Convenience function to quickly log messages in a nice format.
 */
//fun log(tag: String, funName: String, message: String) = Log.d(tag, "[$funName] $message")

/**
 * Convenience wrapper for Log.d to make standard log messages quicker to write.
 *
 * Usage:
 *     AppLog("MainActivity", "onStart", "log message")
 *
 * Result:
 *     // TODO: write result of above call.
 */
object AppLog {

    operator fun invoke(tag: String, funName: String, message: String) {
        Log.d(tag, "[$funName] $message")
    }

}