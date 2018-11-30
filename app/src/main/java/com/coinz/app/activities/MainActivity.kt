package com.coinz.app.activities

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.coinz.app.R
import com.coinz.app.fragments.LocalWalletFragment
import com.coinz.app.fragments.MapFragment
import com.coinz.app.utils.*
import com.mapbox.mapboxsdk.Mapbox

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val tag = "MainActivity"
    }

    private lateinit var mapFragment: MapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        Mapbox.getInstance(this, AppConsts.mapboxToken)

        if (savedInstanceState == null) {
            val fragmentManager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()

            mapFragment = MapFragment.newInstance()
            // TODO: give this a proper ID.
            transaction.add(R.id.fragment_container, mapFragment, "com.mapbox.map")

            transaction.commit()
        } else {
            // TODO: give this a proper ID.
            mapFragment = supportFragmentManager.findFragmentByTag("com.mapbox.map") as MapFragment
        }

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

            when(menuItem.itemId) {
                R.id.nav_map -> {
                    AppLog(tag, "navigationItemSelectedListener", "clicked Map menu item")

                    val fragmentManager: FragmentManager = supportFragmentManager
                    val transaction: FragmentTransaction = fragmentManager.beginTransaction()

                    transaction.apply {
                        replace(R.id.fragment_container, mapFragment)
                        //addToBackStack("mapbox.map")
                    }

                    transaction.commit()
                }
                R.id.nav_local_wallet -> {
                    AppLog(tag, "navigationItemSelectedListener", "clicked LocalWallet menu item")

                    // TODO: Could we do this whole transaction business more compactly?
                    val fragmentManager: FragmentManager = supportFragmentManager
                    val transaction: FragmentTransaction = fragmentManager.beginTransaction()

                    transaction.apply {
                        add(R.id.fragment_container, LocalWalletFragment.newInstance())
                        //addToBackStack("local_wallet")
                    }

                    transaction.commit()

                }
            }

            true
        }
        // Set map item in menu to be active on start-up.
        nav_view.menu.getItem(NavDrawerMenu.Map.index).isChecked = true

    }

    override fun onStart() {
        super.onStart()

        restorePreferences()
    }

    override fun onStop() {
        super.onStop()

        savePreferences()
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
    }

}
