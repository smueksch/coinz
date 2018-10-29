package com.coinz.app.deprecated

object DownloadCompleteRunner : DownloadCompleteListener {
    var result: String? = null

    override fun downloadComplete(result: String) {
        DownloadCompleteRunner.result = result
    }
}