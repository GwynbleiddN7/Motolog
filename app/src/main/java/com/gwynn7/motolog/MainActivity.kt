package com.gwynn7.motolog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.gwynn7.motolog.R
import com.gwynn7.motolog.ViewModel.MotorcycleViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var lastHomeFragmentId: Int = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.motorcycle_nav,
            R.id.gear_nav,
            R.id.settings_nav
        ))
        val navController = getNavController()
        setupActionBarWithNavController(navController, appBarConfiguration)

        MotorcycleViewModel.currentBikeId = null
        lastHomeFragmentId = R.id.motorcycle_list

        findViewById<BottomNavigationView>(R.id.bottomNavView).setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.bike_menu -> replaceHomeFragment(R.id.motorcycle_nav)
                R.id.gear_menu -> replaceHomeFragment(R.id.gear_nav)
                R.id.settings_menu -> replaceHomeFragment(R.id.settings_nav)
            }
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return getNavController().navigateUp() || super.onSupportNavigateUp()
    }

    private fun getNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(R.id.homeFragmentView) as NavHostFragment
        return navHost.navController
    }

    private fun replaceHomeFragment(fragmentId: Int) {
        val navController = getNavController()
        navController.popBackStack(lastHomeFragmentId, true)
        navController.navigate(fragmentId)
        lastHomeFragmentId = fragmentId
    }
}