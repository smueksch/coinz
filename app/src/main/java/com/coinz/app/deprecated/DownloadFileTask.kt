package com.coinz.app.deprecated

import android.os.AsyncTask
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.HttpURLConnection

class DownloadFileTask(private val caller: DownloadCompleteRunner) :
        AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg urls: String): String = try {
        loadFileFromNetwork(urls[0])
    } catch (e: IOException) {
        "Unable to load content: ${e.message}"
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)

        DownloadCompleteRunner.downloadComplete(result)
    }

    private fun loadFileFromNetwork(urlString: String): String {
        val stream: InputStream = downloadUrl(urlString)
        return stream.bufferedReader().use { it.readText() }
    }

    @Throws(IOException::class)
    private fun downloadUrl(urlStr: String): InputStream {
        val url = URL(urlStr)
        val conn = url.openConnection() as HttpURLConnection
        conn.readTimeout = 10000 // milliseconds
        conn.connectTimeout = 15000 // milliseconds
        conn.requestMethod = "GET"
        conn.doInput = true
        conn.connect()
        return conn.inputStream
    }

}