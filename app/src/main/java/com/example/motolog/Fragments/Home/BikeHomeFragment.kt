package com.example.motolog.Fragments.Home

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.motolog.Fragments.Gear.GearShowFragmentDirections
import com.example.motolog.Models.Motorcycle
import com.example.motolog.R
import com.example.motolog.ViewModel.MotorcycleViewModel

class BikeHomeFragment : Fragment() {
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel
    private lateinit var currentBike: Motorcycle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bike_home, container, false)
        setHasOptionsMenu(true)

        mMotorcycleViewModel = ViewModelProvider(this)[MotorcycleViewModel::class.java]
        val bike_id = MotorcycleViewModel.currentBikeId

        if(bike_id == null)
        {
            returnToList()
            return view
        }

        val bikeData = mMotorcycleViewModel.getMotorcycle(bike_id)
        bikeData.observe(viewLifecycleOwner, Observer {
            bikes -> run {
                if(bikes.isNotEmpty())
                {
                    currentBike = bikes.first()
                    view.findViewById<TextView>(R.id.bike_alias).text = currentBike.alias
                    view.findViewById<TextView>(R.id.bike_manufacturer).text = currentBike.manufacturer
                    view.findViewById<TextView>(R.id.bike_model).text = currentBike.model
                    view.findViewById<TextView>(R.id.bike_km).text = String.format("%.2f km", currentBike.personal_km + currentBike.start_km)
                }
                else returnToList()
            }
        })

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.show_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.edit_show_menu)
        {
            val action = BikeHomeFragmentDirections.bikehomeToBikeedit(currentBike)
            findNavController().navigate(action)
        }
        else if(item.itemId == R.id.delete_show_menu) deleteMotorcycle()

        return super.onContextItemSelected(item)
    }

    private fun deleteMotorcycle()
    {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _,_ ->
            mMotorcycleViewModel.deleteMotorcycle(currentBike)
            Toast.makeText(requireContext(), "Motorcycle deleted!", Toast.LENGTH_SHORT).show()
            returnToList()
        }
        builder.setNegativeButton("No"){ _,_ -> }
        builder.setTitle("Delete ${currentBike.manufacturer} ${currentBike.model}?")
        builder.setMessage("Are you sure you want to delete this motorcycle?")
        builder.create().show()
    }

    private fun returnToList()
    {
        activity?.finish()
    }
}