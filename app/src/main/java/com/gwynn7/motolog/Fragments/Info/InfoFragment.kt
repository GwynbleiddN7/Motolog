package com.gwynn7.motolog.Fragments.Info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gwynn7.motolog.Models.Motorcycle
import com.gwynn7.motolog.R
import com.gwynn7.motolog.UnitHelper
import com.gwynn7.motolog.ViewModel.MotorcycleViewModel
import com.gwynn7.motolog.longToDateString
import com.gwynn7.motolog.stop
import java.util.Calendar

class InfoFragment : Fragment() {
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel
    private lateinit var currentbike: Motorcycle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.info, container, false)

        mMotorcycleViewModel = ViewModelProvider(this)[MotorcycleViewModel::class.java]

        val bikeId = MotorcycleViewModel.currentBikeId
        if(bikeId == null)
        {
            stop(activity)
            return view
        }

        val bikeData = mMotorcycleViewModel.getMotorcycle(bikeId)
        bikeData.observe(viewLifecycleOwner, Observer {
            bikes -> run {
            if(bikes.isNotEmpty()) {
                currentbike = bikes.first()

                view.findViewById<TextView>(R.id.bike_cc).text = currentbike.info.engine_cc.toString()
                view.findViewById<TextView>(R.id.bike_hp).text = currentbike.info.horse_power.toString()
                view.findViewById<TextView>(R.id.bike_torque).text = currentbike.info.torque.toString()
                view.findViewById<TextView>(R.id.bike_cylinders).text = currentbike.info.cylinders.toString()

                view.findViewById<TextView>(R.id.bike_price).text = String.format("%.2f%s", currentbike.info.price, UnitHelper.getCurrency())
                view.findViewById<TextView>(R.id.bike_license_plate).text = currentbike.info.plate_number.ifEmpty { getString(R.string.not_set) }
                view.findViewById<TextView>(R.id.bike_front_tire).text = currentbike.info.front_tire.ifEmpty { getString(R.string.not_set)  }
                view.findViewById<TextView>(R.id.bike_rear_tire).text = currentbike.info.rear_tire.ifEmpty { getString(R.string.not_set)  }

                val currentTime = Calendar.getInstance().timeInMillis
                view.findViewById<TextView>(R.id.bike_insurance).text = if(currentbike.expiry.insurance > currentTime) longToDateString(currentbike.expiry.insurance) else getString(R.string.not_updated)
                view.findViewById<TextView>(R.id.bike_tax).text = if(currentbike.expiry.tax > currentTime) longToDateString(currentbike.expiry.tax) else getString(R.string.not_updated)
                view.findViewById<TextView>(R.id.bike_inspection).text = if(currentbike.expiry.inspection > currentTime) longToDateString(currentbike.expiry.inspection) else getString(R.string.not_updated)
            }
            else stop(activity)
        }
        })

        view.findViewById<ImageButton>(R.id.bt_edit_engine).setOnClickListener {
            val action = InfoFragmentDirections.infoToEditengine(currentbike)
            findNavController().navigate(action)
        }
        view.findViewById<ImageButton>(R.id.bt_edit_expiry).setOnClickListener {
            val action = InfoFragmentDirections.infoToEditexpiry(currentbike)
            findNavController().navigate(action)
        }
        view.findViewById<ImageButton>(R.id.bt_edit_info).setOnClickListener {
            val action = InfoFragmentDirections.infoToEditinfo(currentbike)
            findNavController().navigate(action)
        }

        return view
    }
}