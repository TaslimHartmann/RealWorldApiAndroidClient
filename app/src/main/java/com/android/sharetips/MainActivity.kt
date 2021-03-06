package com.android.sharetips

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val hideToolbar = hideToolbar(destination.id)
            when (hideToolbar) {
                hideToolbar -> toolbar?.visibility = View.GONE
                else -> toolbar?.visibility = View.VISIBLE
            }

            val showNav = showBottomNav(destination.id)
            when {
                showNav -> navView.visibility = View.VISIBLE
                else -> navView.visibility = View.GONE
            }
        }
    }

    private fun hideToolbar(id: Int): Boolean {
        return id == R.id.loginFragment
    }

    private fun showBottomNav(id: Int): Boolean {
        return id == R.id.navigation_home
                || id == R.id.navigation_dashboard
                || id == R.id.navigation_notifications
    }

    private fun disableHomeUp(id: Int): Boolean {
        return id == R.id.navigation_home
                || id == R.id.navigation_dashboard
                || id == R.id.navigation_notifications
    }

}