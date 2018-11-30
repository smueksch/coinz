package com.coinz.app.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.res.TypedArray
import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.coinz.app.R
import com.coinz.app.database.Coin
import com.coinz.app.database.MapCoinsViewModel
import com.coinz.app.interfaces.OnCollectCoinListener
import com.coinz.app.utils.AppConsts
import com.coinz.app.utils.AppLog
import com.coinz.app.utils.IconIndex
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
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import kotlinx.android.synthetic.main.fragment_map.*

// Define our own map fragment, because mapbox.SupportMapFragment isn't sufficient, misses all the
// enable location business.
class MapFragment : Fragment(), OnMapReadyCallback, LocationEngineListener,
                    PermissionsListener, OnCollectCoinListener {

    companion object {
        const val logTag = "MapFragment"

        // TODO: could have something with options like SupportMapFragment.
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }

    // Throw exceptions if we don't have associated activity and context as we need both for proper
    // operation.
    // TODO: replace owner with associated.
    //private val ownerActivity = requireActivity()
    //private val ownerContext = requireContext()

    // Have to wait with initialization until fragment is attached to an activity.
    private lateinit var associatedContext: Context
    // TODO: Could do without if we changed the way dialog is called.
    private lateinit var associatedActivity: FragmentActivity

    private lateinit var coinViewModel: MapCoinsViewModel

    private lateinit var mapboxMap: MapboxMap
    private lateinit var mapView: MapView

    private lateinit var origin: Location
    private var permissionsManager = PermissionsManager(this)
    private lateinit var locationEngine: LocationEngine
    private lateinit var locationLayerPlugin: LocationLayerPlugin

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // Throw exceptions if we don't have associated activity and context as we need both for proper
        // operation.
        associatedActivity = requireActivity()
        associatedContext = requireContext()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(associatedContext, AppConsts.mapboxToken)

        /*
        map_view.onCreate(savedInstanceState)
        map_view.getMapAsync(this)
        */
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_map, container, false) // TODO: compare to other fragment.

        mapView = fragmentView.findViewById(R.id.map_view)

        // Call mapView's onCreate here rather than onCreate as we need the view to be inflated
        // first, otherwise we'd get a NullPointerException.
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return fragmentView
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
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
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    // ATTENTION: This function may turn out to be problematic.
    fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState)//, outPersistentState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onMapReady(mapboxMap: MapboxMap?) {
        if (mapboxMap == null) {
            AppLog(logTag, "onMapReady", "mapboxMap is null")
        } else {
            this.mapboxMap = mapboxMap

            // mapboxMap initialized, so we can now initialize the coin view model.
            initializeCoinViewModel()

            // Set UI options.
            mapboxMap.uiSettings.isCompassEnabled = true
            mapboxMap.uiSettings.isZoomControlsEnabled = true

            // Set zoom level.
            // Note: we do this here in code because setting the zoom initially in the XML file
            // seems to have no effect.
            with(CameraPosition.Builder()) {
                this.zoom(AppConsts.initialCameraZoom)
                mapboxMap.cameraPosition = this.build()
            }

            enableLocation()

            // Custom action when pressing a marker.
            mapboxMap.setOnMarkerClickListener { marker ->
                AppLog(logTag, "onMarkerClick", "marker=$marker")

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
            AppLog(logTag, "onLocationChanged", "location is null")
        } else {
            origin = location
            setCameraPosition(origin)
        }
    }

    @SuppressWarnings("MissingPermission")
    override fun onConnected() {
        AppLog(logTag, "onConnected", "requesting location updates")
        locationEngine.requestLocationUpdates()
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        AppLog(logTag, "onExplanationNeeded", "Permissions: $permissionsToExplain")
        // TODO: Present pop-up message or dialogue
    }

    override fun onPermissionResult(granted: Boolean) {
        AppLog(logTag, "onPermissionResult", "granted == $granted")
        if (granted) {
            enableLocation()
        } else {
            // TODO: open dialogue with user.
            AppLog(logTag, "onPermissionResult", "Location permissions still not granted")
        }
    }

    private fun enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(associatedContext)) {
            AppLog(logTag, "enableLocation", "Location permissions granted")
            initializeLocationEngine()
            initializeLocationLayer()
        } else {
            AppLog(logTag, "enableLocation", "Location permissions not granted")
            permissionsManager.requestLocationPermissions(associatedActivity)
        }
    }

    @SuppressWarnings("MissingPermission")
    private fun initializeLocationEngine() {
        locationEngine = LocationEngineProvider(associatedContext).obtainBestLocationEngineAvailable()

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
        // TODO: Remove this if if it's really not needed.
        if (mapboxMap == null) {
            AppLog(logTag, "initializeLocationLayer", "map is null")
        } else {
            locationLayerPlugin = LocationLayerPlugin(mapView, mapboxMap, locationEngine)

            locationLayerPlugin.apply {
                setLocationLayerEnabled(true)
                cameraMode = CameraMode.TRACKING
                renderMode = RenderMode.NORMAL
            }
        }
    }

    private fun setCameraPosition(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    // TODO: Document. Essentially puts an observer on the view model.
    // Needs to be called after mapboxMap is initialized, i.e. in onMapReady at earliest.
    private fun initializeCoinViewModel() {
        coinViewModel = ViewModelProviders.of(this).get(MapCoinsViewModel::class.java)
        //coinViewModel.collectedCoins?.observe(this, Observer { coins -> })
        coinViewModel.coins?.observe(this, Observer<List<Coin>> { coins ->
            // Remove all current markers.
            // Note: Seems a bit wasteful if only one coin is updated. However, we don't know
            // ahead of time how many coins have been updated.
            mapboxMap.removeAnnotations()

            // Add in the new markers for the new set of coins:
            coins?.forEach { addMarker(mapboxMap, it) }
        })
    }

    override fun onCollectCoin(id: String) {
        coinViewModel.setCollected(id)
    }

    private fun showMarkerDialog(marker: Marker) {
        // ATTENTION: This use of activity could cause major issue, may be better to find a way
        // to start one fragment from the other.
        // TODO: Try to follow the above about fragment starting.
        val ft = childFragmentManager.beginTransaction()
        val previous = childFragmentManager.findFragmentByTag("collectDialog")
        previous?.let {
            ft.remove(it)
        }
        ft.addToBackStack(null)

        val collectCoinDialog = CollectCoinDialogFragment()

        // TODO: could we not pass arguments by defining a newInstance function for CollectCoinDialogFragment?
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

    // TODO: Is there a better place to put this?
    private fun locationToLatLng(location: Location): LatLng {
        return LatLng(location.latitude, location.longitude)
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

            val iconFactory = IconFactory.getInstance(associatedContext)
            val iconIndex = IconIndex(coin.markerSymbol, coin.markerColor)

            icon = iconFactory.fromResource(icons.getResourceId(iconIndex, 0))
        }

        mapboxMap?.addMarker(markerOpt)
    }
}