package com.gwynn7.motolog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.gwynn7.motolog.ViewModel.MotorcycleViewModel

class BikeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bike)
        val bikeId = intent.extras!!.getInt("bike_id")
        MotorcycleViewModel.currentBikeId = bikeId

        val navController = getNavController()
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return getNavController().navigateUp() || super.onSupportNavigateUp()
    }

    private fun getNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(R.id.bike_home_nav) as NavHostFragment
        return navHost.navController
    }
}