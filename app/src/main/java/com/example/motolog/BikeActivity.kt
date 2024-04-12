package com.example.motolog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.motolog.ViewModel.MotorcycleViewModel

class BikeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bike)
        val bike_id = intent.extras!!.getInt("bike_id")
        MotorcycleViewModel.currentBikeId = bike_id

        val navController = getNavController()
        setupActionBarWithNavController(navController)
    }

    private fun getNavController(): NavController
    {
        val navHost = supportFragmentManager.findFragmentById(R.id.bike_home_nav) as NavHostFragment
        return navHost.navController
    }
}