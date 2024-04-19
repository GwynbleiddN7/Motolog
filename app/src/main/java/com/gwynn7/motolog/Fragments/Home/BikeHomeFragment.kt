package com.gwynn7.motolog.Fragments.Home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gwynn7.motolog.Models.Motorcycle
import com.gwynn7.motolog.R
import com.gwynn7.motolog.ViewModel.MotorcycleViewModel
import com.gwynn7.motolog.formatThousand
import com.gwynn7.motolog.showToast

class BikeHomeFragment : Fragment() {
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel
    private lateinit var currentBike: Motorcycle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bike_home, container, false)


        mMotorcycleViewModel = ViewModelProvider(this)[MotorcycleViewModel::class.java]
        val bikeId = MotorcycleViewModel.currentBikeId
        if(bikeId == null)
        {
            returnToList()
            return view
        }

        val bikeData = mMotorcycleViewModel.getMotorcycle(bikeId)
        bikeData.observe(viewLifecycleOwner, Observer {
            bikes -> run {
                if(bikes.isNotEmpty())
                {
                    currentBike = bikes.first()
                    val aliasTextView = view.findViewById<TextView>(R.id.bike_alias)
                    aliasTextView.text = currentBike.alias.ifEmpty { currentBike.model }
                    aliasTextView.isSelected = true

                    view.findViewById<TextView>(R.id.bike_manufacturer).text = currentBike.manufacturer
                    view.findViewById<TextView>(R.id.bike_model).text = currentBike.model
                    view.findViewById<TextView>(R.id.bike_year).text = String.format("%d", currentBike.year)

                    view.findViewById<TextView>(R.id.bike_personalkm).text = formatThousand(currentBike.personal_km)
                    view.findViewById<TextView>(R.id.bike_totalkm).text = formatThousand(currentBike.personal_km + currentBike.start_km)

                    val bikeImage = view.findViewById<ImageView>(R.id.bike_image)
                    if(currentBike.image != null) bikeImage.setImageURI(currentBike.image)
                    else {
                        bikeImage.setImageResource(R.drawable.bike_home)
                        val bikeCard = view.findViewById<CardView>(R.id.cv_bike_image)
                        bikeCard.radius = 0F
                        bikeCard.scaleX = 1.3F
                        bikeCard.scaleY = 1.3F
                        bikeImage.scaleX = 1.1F
                        bikeImage.scaleY = 1.1F
                        bikeImage.setPadding(0,60,0,0)
                    }

                    view.findViewById<ScrollView>(R.id.bike_home).visibility = VISIBLE
                }
                else returnToList()
            }
        })

        view.findViewById<CardView>(R.id.distanceButton).setOnClickListener{
            findNavController().navigate(R.id.bikehome_to_bikedistance)
        }
        view.findViewById<CardView>(R.id.infoButton).setOnClickListener{
            findNavController().navigate(R.id.bikehome_to_bikeinfo)
        }
        view.findViewById<CardView>(R.id.modsButton).setOnClickListener{
            findNavController().navigate(R.id.bikehome_to_bikemods)
        }
        view.findViewById<CardView>(R.id.repairButton).setOnClickListener{
            findNavController().navigate(R.id.bikehome_to_bikerepairs)
        }

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.show_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.edit_show_menu -> {
                val action = BikeHomeFragmentDirections.bikehomeToBikeedit(currentBike)
                findNavController().navigate(action)
            }
            R.id.delete_show_menu -> deleteMotorcycle()
        }

        return super.onContextItemSelected(item)
    }

    private fun deleteMotorcycle()
    {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)){ _, _ ->
            mMotorcycleViewModel.deleteMotorcycle(currentBike)
            showToast(requireContext(), getString(R.string.bike_delete))
            returnToList()
        }
        builder.setNegativeButton(getString(R.string.no)){ _,_ -> }
        builder.setTitle("${getString(R.string.delete)} ${currentBike.manufacturer} ${currentBike.model}?")
        builder.setMessage(getString(R.string.delete_bike_question))
        builder.create().show()
    }

    private fun returnToList()
    {
        activity?.finish()
    }
}