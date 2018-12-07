package com.coinz.app

import com.coinz.app.database.asynctasks.DownloadMapFileTask
import org.junit.Test

import org.junit.Assert.*
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Test suite to check whether the asynchronous DownloadMapFileTaskTest behaves as required.
 */
class DownloadMapFileTaskTest {

    // Name of file containing a valid download result for a coin map.
    private val validCoinzMapFilename = "coinz-map-2018-12-06.geojson"
    // Content of a valid coin map which can be downloaded. Used as expected value to test
    // maps being downloaded.
    private val validCoinzMap: String

    // Start of the expected error message on download failure (will always be the same, rest will
    // be different depending on what went wrong).
    private val expectedErrorStart = "Unable to load content:"

    init {
        // Get path to valid coin map file.
        val coinMapFilePath = Paths.get(System.getProperty("user.dir"),
                                        "src", "test", "java", "com", "coinz", "app",
                                        validCoinzMapFilename)

        // Read the content of the valid coin map into a string.
        validCoinzMap = File(coinMapFilePath.toString()).readText()
    }

    /**
     * Test whether a map file we know exists is downloaded properly.
     */
    @Test
    fun assertDownloadSuccess() {
        // URL we know contains a valid coin map.
        val url = "http://homepages.inf.ed.ac.uk/stg/coinz/2018/12/06/coinzmap.geojson"

        // Download the map an get it as a string we can check.
        val result = DownloadMapFileTask().execute(url).get()

        // We're expecting the result to be the content of the coin map for that day.
        assertEquals(validCoinzMap, result)
    }

    /**
     * Test whether map gives the correct response to a map not being available.
     */
    @Test
    fun assertDownloadFailure() {
        // URL we know won't contain a valid coin map.
        val url = "http://homepages.inf.ed.ac.uk/stg/coinz/2020/12/06/coinzmap.geojson"

        // Attempt to download the coin map from the URL and get it as a string.
        val result = DownloadMapFileTask().execute(url).get()

        // We're expecting a failure message, so the first bit of the result should be the same as
        // the expected error start, so take that many characters from the result.
        assertEquals(expectedErrorStart, result.take(expectedErrorStart.length))
    }

    /**
     * Test whether multiple urls as input are handled correctly
     *
     * Check here that the first URL will be used for the download and that the download succeeds
     * given the first URL contains a valid coin map.
     */
    @Test
    fun assertMultipleUrlFirstValid() {
        // List of URLs to feed into the download task. First one is valid, second isn't.
        val urls = arrayOf("http://homepages.inf.ed.ac.uk/stg/coinz/2018/12/06/coinzmap.geojson",
                           "http://homepages.inf.ed.ac.uk/stg/coinz/2020/12/06/coinzmap.geojson")

        // Download the map an get it as a string we can check.
        val result = DownloadMapFileTask().execute(urls[0], urls[1]).get()

        // We're expecting the result to be the content of the coin map for that day.
        assertEquals(validCoinzMap, result)
    }

    /**
     * Test whether multiple urls as input are handled correctly.
     *
     * Check here that the first URL will be used for the download and that the download fails
     * given the first URL contains an invalid coin map address, but the second contains a valid one.
     */
    @Test
    fun assertMultipleUrlFirstInvalid() {
        // List of URLs to feed into the download task. First one is invalid, second is actually
        // valid.
        val urls = arrayOf("http://homepages.inf.ed.ac.uk/stg/coinz/2020/12/06/coinzmap.geojson",
                           "http://homepages.inf.ed.ac.uk/stg/coinz/2018/12/06/coinzmap.geojson")

        // Download the map an get it as a string we can check.
        val result = DownloadMapFileTask().execute(urls[0], urls[1]).get()

        assertEquals(expectedErrorStart, result.take(expectedErrorStart.length))
    }

}