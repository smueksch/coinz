package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadMapFileTask : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg urls: String): String = try {
        loadFileFromNetwork(urls[0])
    } catch (e: IOException) {
        "Unable to load content: ${e.message}"
    }

    private fun loadFileFromNetwork(urlString: String): String {
        val stream: InputStream = downloadUrl(urlString)
        return stream.bufferedReader().use { it.readText() }
    }

    @Throws(IOException::class)
    private fun downloadUrl(urlStr: String): InputStream {
        val conn = URL(urlStr).openConnection() as HttpURLConnection
        conn.apply {
            readTimeout = 10000    // milliseconds
            connectTimeout = 15000 // milliseconds
            requestMethod = "GET"
            doInput = true
            connect()
        }
        return conn.inputStream
    }

}