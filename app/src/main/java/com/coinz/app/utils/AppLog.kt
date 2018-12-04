package com.coinz.app.utils

import android.util.Log

/**
 * Convenience wrapper for Log.d to make standard log messages quicker to write.
 *
 * Usage:
 *     AppLog("MainActivity", "onStart", "log message")
 *
 * Output (abbreviated):
 *     MainActivity: [onStart] log message
 */
object AppLog {

    /**
     * Write to the log output.
     *
     * @param tag Main tag identifying the class writing to the log output.
     * @param funName Name of function invoking this call to write to the log output.
     * @param message Output message.
     */
    operator fun invoke(tag: String, funName: String, message: String) {
        Log.d(tag, "[$funName] $message")
    }

}