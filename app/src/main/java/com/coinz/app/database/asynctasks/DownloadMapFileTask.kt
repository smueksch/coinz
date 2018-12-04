package com.coinz.app.database.asynctasks

import android.os.AsyncTask
import com.coinz.app.utils.AppLog
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Task to download data located online asynchronously.
 */
class DownloadMapFileTask : AsyncTask<String, Void, String>() {

    /**
     * Get data at given url as string.
     *
     * @param urls List of URLs, first one will be used to locate file on network.
     *
     * @exception IOException Thrown when the resource at the given URL cannot be accessed.
     *
     * @return Content of file at given URL.
     */
    override fun doInBackground(vararg urls: String): String = try {
        AppLog("DownloadMapFileTask", "doInBackground", "downloading from url ${urls[0]}")
        // Load the content of the resource at the URL and return it.
        loadFileFromNetwork(urls[0])
    } catch (e: IOException) {
        // Resource cannot be accessed, throw exception with appropriate message.
        "Unable to load content: ${e.message}"
    }

    /**
     * Read file from network located at given URL.
     *
     * @param urlString URL of file to read from network.
     *
     * @return String holding the data from the file at the given location.
     */
    private fun loadFileFromNetwork(urlString: String): String {
        // Create readable input stream and return file content as string.
        val stream: InputStream = downloadUrl(urlString)
        return stream.bufferedReader().use { it.readText() }
    }

    /**
     * Create input stream for given URL
     *
     * @param urlStr URL for which to create a readable input stream.
     *
     * @return Readable input stream for server at given URL.
     */
    @Throws(IOException::class)
    private fun downloadUrl(urlStr: String): InputStream {
        // Create an HTTP connection to server at given URL
        val conn = URL(urlStr).openConnection() as HttpURLConnection

        // Set connection options.
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