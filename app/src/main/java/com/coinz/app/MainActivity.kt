package com.coinz.app

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView

import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

class MainActivity : AppCompatActivity() {

    companion object {
        val tag = "MainActivity"
        val mapboxToken = "pk.eyJ1Ijoic2VibXVlayIsImEiOiJjam12MWE0a3kwNW92M3Bxdmxxcnk1ZmYwIn0.1tI9T6CLf7Qq0ZvGtCK9QQ"
        val mapFilename = "coinzmap.geojson"
        val dateFormat = "yyyy/MM/dd" // For dates in SharedPreferences.
    }

    private var mapUrl = MapURL(Calendar.getInstance().time) // Map URL for current date.
    private var mapDownloadDate: String? = "" // Shared preference could be null.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        Mapbox.getInstance(this, mapboxToken)
        mapView.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()

        restorePreferences()

        downloadMap()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()

        savePreferences()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Save preferences to SharedPreferences file.
     */
    private fun savePreferences() {
        val funTag = "[savePreferences]"

        val settings = getSharedPreferences(getString(R.string.preferences_filename),
                Context.MODE_PRIVATE)
        val editor = settings.edit()

        Log.d(tag, "$funTag Saving mapDownloadDate=$mapDownloadDate")
        editor.putString(getString(R.string.map_download_date), mapDownloadDate)

        editor.apply()
    }

    /**
     * Restore preferences from SharedPreferences file.
     */
    private fun restorePreferences() {
        val funTag = "[restorePreferences]"

        val settings = getSharedPreferences(getString(R.string.preferences_filename),
                                            Context.MODE_PRIVATE)

        mapDownloadDate = settings.getString(getString(R.string.map_download_date), "")
        Log.d(tag, "$funTag Restored mapDownloadDate=$mapDownloadDate")
    }

    /**
     * Download today's map.
     *
     * Download map for the current date if and only if it has not been downloaded yet.
     */
    private fun downloadMap() {
        val funTag = "[downloadMap]"

        val currentDate = SimpleDateFormat(dateFormat).format(Calendar.getInstance().time)

        // Download map only if new one is available.
        if (currentDate == mapDownloadDate) {
            Log.d(tag, "$funTag Map already downloaded")
        } else {
            val downloadRunner = DownloadCompleteRunner
            val downloadTask = DownloadFileTask(downloadRunner)

            Log.d(tag, "$funTag Starting download for URL=${mapUrl.url}")
            val map = downloadTask.execute(mapUrl.url).get()

            Log.d(tag, "$funTag Download result=${map.take(100)}")
            saveMap(map)

            // Reset the download date for the map.
            mapDownloadDate = currentDate
        }
    }

    private fun saveMap(data: String?) {
        val funTag = "[saveMap]"

        val mapFile = File(filesDir, getString(R.string.map_filename))

        FileOutputStream(mapFile).use { it.write(data?.toByteArray()) }
        Log.d(tag, "$funTag Saved map to $filesDir/${getString(R.string.map_filename)}")
    }

    // TODO: Need map loading capabilities.
}
