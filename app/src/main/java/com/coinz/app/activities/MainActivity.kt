package com.coinz.app.activities

import android.content.Context
import android.location.Location
import android.os.Binder
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.coinz.app.R
import com.coinz.app.database.Coin
import com.coinz.app.database.CoinRepository
import com.coinz.app.fragments.CollectCoinDialogFragment
import com.coinz.app.utils.AppLog
import com.coinz.app.utils.AppStrings
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.android.core.location.LocationEnginePriority
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, LocationEngineListener,
                     PermissionsListener {

    companion object {
        const val tag = "MainActivity"
        const val mapboxToken = "pk.eyJ1Ijoic2VibXVlayIsImEiOiJjam12MWE0a3kwNW92M3Bxdmxxcnk1ZmYwIn0.1tI9T6CLf7Qq0ZvGtCK9QQ"
    }

    private lateinit var coinRepository: CoinRepository

    /** TODO: Deprecated
    private var mapUrl = MapURL() // Map URL for current date.
    private var mapDownloadDate: String? = "" // Shared preference could be null.
    */

    private var map: MapboxMap? = null

    private lateinit var origin: Location
    private var permissionsManager = PermissionsManager(this)
    private lateinit var locationEngine: LocationEngine
    private lateinit var locationLayerPlugin: LocationLayerPlugin


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        nav_view.setNavigationItemSelectedListener { menuItem ->
            // Set item as selected to persist highlight.
            menuItem.isChecked = true

            // Close drawer when item is tapped.
            drawer_layout.closeDrawers()

            // TODO: Add code to update UI based on item selected.
            // Add code here to update the UI based on the item selected
            // For example, swap UI fragments here

            true
        }
        // On start-up, the map is always the active menu item. Since the map is the first item in
        // the menu it has index 0.
        nav_view.menu.getItem(0).isChecked = true

        Mapbox.getInstance(this, mapboxToken)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()

        restorePreferences()

        // TODO: Should this be in onCreate instead?
        coinRepository = CoinRepository(this)

        /** TODO: Deprecated
        downloadMap()
        */
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
            // NOTE: need the 'android.' preceding the 'R.id.home', otherwise the menu button won't
            // work.
            android.R.id.home -> {
                // TODO: have a proper naming convention for IDs
                drawer_layout.openDrawer(GravityCompat.START)
                true
            }
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap?) {
        val funTag = "[onMapReady]"

        if (mapboxMap == null) {
            Log.d(tag, "$funTag mapboxMap is null")
        } else {
            map = mapboxMap

            // Set UI options.
            map?.uiSettings?.isCompassEnabled = true
            map?.uiSettings?.isZoomControlsEnabled = true

            enableLocation()

            // Custom action when pressing a marker.
            map?.setOnMarkerClickListener { marker ->
                AppLog(tag, "onMarkerClick", "marker=$marker")

                val ft = supportFragmentManager.beginTransaction()
                val previous = supportFragmentManager.findFragmentByTag("collectDialog")
                previous?.let {
                    ft.remove(it)
                }
                ft.addToBackStack(null)

                val collectCoinDialog = CollectCoinDialogFragment()

                collectCoinDialog.arguments = Bundle().apply {
                    // TODO: These string values should be in AppStrings.
                    /*
                    putCharSequence("currency", marker.title)
                    putCharSequence("value", marker.snippet)
                    putDouble("latitude", marker.position.latitude)
                    putDouble("longitude", marker.position.longitude)
                    */
                    putCharSequence("coin_id", marker.title)
                }
                collectCoinDialog.show(ft, "collectDialog")

                true // Consume event.
                /*
                 * NB: if we put "true" in here the onClick event is consumed, so there is no small
                 * Mapbox popup with title and snipped over the marker. If we put "false, we still
                 * call this listener, but also get a Mapbox popup with title and marker.
                 */
            }

            /** TODO: Deprecated
            // TODO: Is this the appropriate function to call this?
            val map = loadMap()

            // Add all makers.
            map.features()?.forEach { addMarker(mapboxMap, it) }
            */

            // TODO: Clean-up
            AppLog(tag, "onMapReady", "getAllNotCollected()=${coinRepository.getAllNotCollected()}")
            //AppLog(tag, "onMapReady", "getAllNotCollected().value=${coinRepository.getAllNotCollected()}")
            coinRepository.getAllNotCollected()?.forEach { addMarker(mapboxMap, it) }
        }
    }

    override fun onLocationChanged(location: Location?) {
        val funTag = "[onLocationChanged]"

        if (location == null) {
            Log.d(tag, "$funTag location is null")
        } else {
            origin = location
            setCameraPosition(origin)
        }
    }

    @SuppressWarnings("MissingPermission")
    override fun onConnected() {
        val funTag = "[onConnected]"

        Log.d(tag, "$funTag requesting location updates")
        locationEngine.requestLocationUpdates()
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        val funTag = "[onExplanationNeeded]"
        Log.d(tag, "$funTag Permissions: $permissionsToExplain")
        // TODO: Present pop-up message or dialogue
    }

    override fun onPermissionResult(granted: Boolean) {
        val funTag = "[onPermissionResult]"

        Log.d(tag, "$funTag granted == $granted")
        if (granted) {
            enableLocation()
        } else {
            // TODO: open dialogue with user.
            AppLog(tag, "onPermissionResult", "Location permissions still not granted")
        }
    }

    /**
     * Save preferences to SharedPreferences file.
     */
    private fun savePreferences() {
        val funTag = "[savePreferences]"

        val settings = getSharedPreferences(AppStrings.preferencesFilename,
                                            Context.MODE_PRIVATE)
        val editor = settings.edit()

        /** TODO: Deprecated
        Log.d(tag, "$funTag Saving mapDownloadDate=$mapDownloadDate")
        editor.putString(AppStrings.mapDownloadDate, mapDownloadDate)
        */

        editor.apply()
    }

    /**
     * Restore preferences from SharedPreferences file.
     */
    private fun restorePreferences() {
        val funTag = "[restorePreferences]"

        val settings = getSharedPreferences(AppStrings.preferencesFilename,
                                            Context.MODE_PRIVATE)

        /** TODO: Deprecated
        mapDownloadDate = settings.getString(AppStrings.mapDownloadDate, "")
        Log.d(tag, "$funTag Restored mapDownloadDate=$mapDownloadDate")
        */
    }

    /** TODO: Deprecated
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

    // TODO: saveMapData maybe?
    private fun saveMap(data: String?) {
        val funTag = "[saveMap]"

        val mapFile = File(filesDir, AppStrings.mapFilename)

        FileOutputStream(mapFile).use { it.write(data?.toByteArray()) }
        Log.d(tag, "$funTag Saved map to $filesDir/${AppStrings.mapFilename}")
    }

    // TODO: document
    private fun loadMapData(): String {
        val funTag = "[loadMapData]"

        val mapFile = File(filesDir, AppStrings.mapFilename)

        val rawMapData: String = FileInputStream(mapFile).bufferedReader().use { it.readText() }
        Log.d(tag, "$funTag Loaded map from $filesDir/${AppStrings.mapFilename}")

        return rawMapData
    }

    // TODO: document
    private fun loadMap() = FeatureCollection.fromJson(loadMapData())
    */

    private fun enableLocation() {
        val funTag = "[enableLocation]"

        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            Log.d(tag, "$funTag Location permissions granted")
            initializeLocationEngine()
            initializeLocationLayer()
        } else {
            Log.d(tag, "$funTag Location permissions not granted")
            permissionsManager.requestLocationPermissions(this)
        }
    }

    @SuppressWarnings("MissingPermission")
    private fun initializeLocationEngine() {
        locationEngine = LocationEngineProvider(this).obtainBestLocationEngineAvailable()

        locationEngine.apply {
            interval = 5000        // Every 5 seconds.
            fastestInterval = 1000 // At most every second.
            priority = LocationEnginePriority.HIGH_ACCURACY
            activate()
        }

        val lastLocation = locationEngine.lastLocation

        if (lastLocation != null) {
            origin = lastLocation
            setCameraPosition(origin)
        } else {
            locationEngine.addLocationEngineListener(this)
        }
    }

    @SuppressWarnings("MissingPermission")
    private fun initializeLocationLayer() {
        val funTag = "[initializeLocationLayer]"

        if (map == null) {
            Log.d(tag, "$funTag map is null")
        } else {
            locationLayerPlugin = LocationLayerPlugin(mapView, map!!, locationEngine)

            locationLayerPlugin.apply {
                setLocationLayerEnabled(true)
                cameraMode = CameraMode.TRACKING
                renderMode = RenderMode.NORMAL
            }
        }
    }

    private fun setCameraPosition(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        map?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    /** TODO: Deprecated
    private fun addMarker(mapboxMap: MapboxMap?, mapFeature: Feature) {
        val funTag = "[addMarker]"

        val markerOpt = MarkerOptions().apply {
            val geometry = mapFeature.geometry()
            if (geometry is Point) {
                position = LatLng(geometry.latitude(), geometry.longitude())
            }

            val properties = mapFeature.properties()
            val currency = properties?.get("currency")?.asString ?: ""
            val value = properties?.get("value")?.asString ?: ""

            val markerTitle = getString(R.string.coin_marker_title)
            val markerSnippet = getString(R.string.coin_marker_snippet)

            title = "$markerTitle: $currency"
            snippet = "$markerSnippet: $value"

            // TODO: customize the marker depending on marker-color property.
            icon = IconFactory.getInstance(this@MainActivity).fromResource(R.drawable.map_marker_blue)
        }

        mapboxMap?.addMarker(markerOpt)
    }
    */

    // TODO: Move this into a class where it's more appropriate
    // TODO: this could be made into a Kotlin like extension of MapboxMap.
    private fun addMarker(mapboxMap: MapboxMap?, coin: Coin) {
        val markerOpt = MarkerOptions().apply {
            position = LatLng(coin.latitude, coin.longitude)

            val currency = coin.currency
            val value = coin.storedValue

            val markerTitle = getString(R.string.coin_marker_title)
            val markerSnippet = getString(R.string.coin_marker_snippet)

            // Temporarily change title and snipped to work with dialog
            /*title = "$markerTitle: $currency"
            snippet = "$markerSnippet: $value"*/

            // Set marker title to coin ID so we can retrieve all data associated with Coin simply
            // by getting the title of the marker later on. Important for onMarkerClickListener.
            title = coin.id

            // TODO: customize the marker depending on marker-color property.
            //icon = IconFactory.getInstance(this@MainActivity).fromResource(R.drawable.map_marker_blue)
        }

        mapboxMap?.addMarker(markerOpt)
    }
}
