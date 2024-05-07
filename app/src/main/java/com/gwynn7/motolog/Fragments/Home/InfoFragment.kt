package com.gwynn7.motolog.Fragments.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gwynn7.motolog.R

class InfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bikeinfo, container, false)

        view.findViewById<TextView>(R.id.bike_cc).text = "650"
        view.findViewById<TextView>(R.id.bike_hp).text = "73.4"
        view.findViewById<TextView>(R.id.bike_torque).text = "64"
        view.findViewById<TextView>(R.id.bike_cylinders).text = "2"

        view.findViewById<TextView>(R.id.bike_price).text = "3500$"
        view.findViewById<TextView>(R.id.bike_license_plate).text = "EC640FE"
        view.findViewById<TextView>(R.id.bike_front_tire).text = "Metzler Sportec R7 170/60"
        view.findViewById<TextView>(R.id.bike_rear_tire).text = "Metzler Sportec R7 160/70"

        view.findViewById<TextView>(R.id.bike_insurance).text = "28/05/2027"
        view.findViewById<TextView>(R.id.bike_tax).text = "18/08/2025"
        view.findViewById<TextView>(R.id.bike_inspection).text = "16/04/2023"

        return view
    }
}