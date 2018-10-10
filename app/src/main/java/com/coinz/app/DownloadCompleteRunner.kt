package com.coinz.app

object DownloadCompleteRunner : DownloadCompleteListener {
    var result: String? = null

    override fun downloadComplete(result: String) {
        this.result = result
    }
}