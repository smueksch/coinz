package com.coinz.app.activities

import android.content.Context
import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mapbox.mapboxsdk.Mapbox

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val tag = "MainActivity"
    }

    private lateinit var auth: FirebaseAuth

    private lateinit var mapFragment: MapFragment
    private lateinit var localWalletFragment: LocalWalletFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()

        Mapbox.getInstance(this, AppConsts.mapboxToken)

        // Initialize map fragment.
        if (savedInstanceState == null) {
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

            mapFragment = MapFragment.newInstance()
            // TODO: give this a proper ID.
            transaction.add(R.id.fragment_container, mapFragment, AppConsts.mapFragmentTag)

            transaction.commit()
        } else {
            // TODO: give this a proper ID.
            mapFragment = supportFragmentManager.findFragmentByTag(AppConsts.mapFragmentTag) as MapFragment
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
                // TODO: Turn these IDs into better named constanst
                R.id.nav_map -> {
                    AppLog(tag, "navigationItemSelectedListener", "clicked Map menu item")

                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

                    transaction.replace(R.id.fragment_container, mapFragment)

                    transaction.commit()
                }
                R.id.nav_local_wallet -> {
                    AppLog(tag, "navigationItemSelectedListener", "clicked LocalWallet menu item")

                    val fragmentManager: FragmentManager = supportFragmentManager
                    val transaction: FragmentTransaction = fragmentManager.beginTransaction()

                    // Try to load local wallet fragment if it's already been initialized and added.
                    val loadedFragment = fragmentManager.findFragmentByTag(AppConsts.localWalletFragmentTag)

                    if (loadedFragment == null) {
                        // Local wallet fragment not yet initialized, do it now.
                        localWalletFragment = LocalWalletFragment.newInstance()
                        transaction.add(R.id.fragment_container, localWalletFragment,
                                        AppConsts.localWalletFragmentTag)
                    } else {
                        // Local wallet fragment already initialized, use existing version.
                        localWalletFragment = loadedFragment as LocalWalletFragment
                        transaction.replace(R.id.fragment_container, localWalletFragment)
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

        ensureUserLoggedIn(auth.currentUser)

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

    /**
     * Ensure user is logged in.
     *
     * Check whether the user is logged in. If they aren't, then open the log in activity to have
     * them log in or create an account if they haven't done so yet.
     *
     * @param user User to check.
     */
    private fun ensureUserLoggedIn(user: FirebaseUser?) {
        if (user == null) {
            // No current user, need to require log in.
            AppLog(tag, "ensureUserLoggedIn", "No user logged in, starting LogInActivity")
            startActivity(Intent(this, LogInActivity::class.java))
        }
    }

}
