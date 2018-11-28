package com.coinz.app.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.res.TypedArray
import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.coinz.app.database.MapCoinsViewModel
import com.coinz.app.R
import com.coinz.app.database.Coin
import com.coinz.app.fragments.CollectCoinDialogFragment
import com.coinz.app.interfaces.OnCollectCoinListener
import com.coinz.app.utils.*
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.android.core.location.LocationEnginePriority
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, LocationEngineListener,
                     PermissionsListener, OnCollectCoinListener {

    companion object {
        const val tag = "MainActivity"
    }

    private lateinit var coinViewModel: MapCoinsViewModel

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
        // Set map item in menu to be active on start-up.
        nav_view.menu.getItem(NavDrawerMenu.Map.index).isChecked = true

        Mapbox.getInstance(this, AppConsts.mapboxToken)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        coinViewModel = ViewModelProviders.of(this).get(MapCoinsViewModel::class.java)
        //coinViewModel.collectedCoins?.observe(this, Observer { coins -> })
        coinViewModel.coins?.observe(this, Observer<List<Coin>> { coins ->
            // Remove all current markers.
            // Note: Seems a bit wasteful if only one coin is updated. However, we don't know
            // ahead of time how many coins have been updated.
            map?.removeAnnotations()

            // Add in the new markers for the new set of coins:
            coins?.forEach { addMarker(map, it) }
        })
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()

        restorePreferences()
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
        if (mapboxMap == null) {
            AppLog(tag, "onMapReady", "mapboxMap is null")
        } else {
            map = mapboxMap

            // Set UI options.
            map?.uiSettings?.isCompassEnabled = true
            map?.uiSettings?.isZoomControlsEnabled = true

            // Set zoom level.
            // Note: we do this here in code because setting the zoom initially in the XML file
            // seems to have no effect.
            with(CameraPosition.Builder()) {
                this.zoom(AppConsts.initialCameraZoom)
                map?.cameraPosition = this.build()
            }

            enableLocation()

            // Custom action when pressing a marker.
            map?.setOnMarkerClickListener { marker ->
                AppLog(tag, "onMarkerClick", "marker=$marker")

                showMarkerDialog(marker)

                true // Consume event.
                /*
                 * NB: if we put "true" in here the onClick event is consumed, so there is no small
                 * Mapbox popup with title and snipped over the marker. If we put "false, we still
                 * call this listener, but also get a Mapbox popup with title and marker.
                 */
            }

            // Do initial rendering of all markers.
            coinViewModel.coins?.value?.forEach { addMarker(mapboxMap, it) }
        }
    }

    override fun onLocationChanged(location: Location?) {
        if (location == null) {
            AppLog(tag, "onLocationChanged", "location is null")
        } else {
            origin = location
            setCameraPosition(origin)
        }
    }

    @SuppressWarnings("MissingPermission")
    override fun onConnected() {
        AppLog(tag, "onConnected", "requesting location updates")
        locationEngine.requestLocationUpdates()
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        AppLog(tag, "onExplanationNeeded", "Permissions: $permissionsToExplain")
        // TODO: Present pop-up message or dialogue
    }

    override fun onPermissionResult(granted: Boolean) {
        AppLog(tag, "onPermissionResult", "granted == $granted")
        if (granted) {
            enableLocation()
        } else {
            // TODO: open dialogue with user.
            AppLog(tag, "onPermissionResult", "Location permissions still not granted")
        }
    }

    override fun onCollectCoin(id: String) {
        coinViewModel.setCollected(id)
    }

    private fun showMarkerDialog(marker: Marker) {
        val ft = supportFragmentManager.beginTransaction()
        val previous = supportFragmentManager.findFragmentByTag("collectDialog")
        previous?.let {
            ft.remove(it)
        }
        ft.addToBackStack(null)

        val collectCoinDialog = CollectCoinDialogFragment()

        collectCoinDialog.arguments = Bundle().apply {
            putCharSequence(CollectCoinDialogFragment.Args.coinId, marker.title)

            // Compute distance from user to marker:
            val markerDist = marker.position.distanceTo(locationToLatLng(origin))
            putDouble(CollectCoinDialogFragment.Args.markerDist, markerDist)
        }

        // NOTE: Could be that putting null here is a problem!
        collectCoinDialog.setTargetFragment(null, 0)
        collectCoinDialog.show(ft, "collectDialog")
    }

    /**
     * Save preferences to SharedPreferences file.
     */
    private fun savePreferences() {
        val settings = getSharedPreferences(AppConsts.preferencesFilename,
                                            Context.MODE_PRIVATE)
        val editor = settings.edit()

        editor.apply()
    }

    /**
     * Restore preferences from SharedPreferences file.
     */
    private fun restorePreferences() {
        val settings = getSharedPreferences(AppConsts.preferencesFilename,
                                            Context.MODE_PRIVATE)
    }

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

    // TODO: Move this into a class where it's more appropriate
    // TODO: this could be made into a Kotlin like extension of MapboxMap.
    private fun addMarker(mapboxMap: MapboxMap?, coin: Coin) {
        val markerOpt = MarkerOptions().apply {
            position = LatLng(coin.latitude, coin.longitude)

            // Set marker title to coin ID so we can retrieve all data associated with Coin simply
            // by getting the title of the marker later on. Important for onMarkerClickListener.
            title = coin.id

            // Set custom marker icon depending on coin data.
            val icons: TypedArray = resources.obtainTypedArray(R.array.marker_drawables)

            val iconFactory = IconFactory.getInstance(this@MainActivity)
            val iconIndex = IconIndex(coin.markerSymbol, coin.markerColor)

            icon = iconFactory.fromResource(icons.getResourceId(iconIndex, 0))
        }

        mapboxMap?.addMarker(markerOpt)
    }

    // TODO: Is there a better place to put this?
    private fun locationToLatLng(location: Location): LatLng {
        return LatLng(location.latitude, location.longitude)
    }
}
