package com.example.motolog

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BikeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bike)
        val bike_id = intent.extras!!.getInt("bike_id")
        findViewById<TextView>(R.id.idView).text = bike_id.toString()
    }
}