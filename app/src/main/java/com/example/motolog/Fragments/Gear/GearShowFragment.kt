package com.example.motolog.Fragments.Gear

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.motolog.R
import com.example.motolog.ViewModel.GearViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GearShowFragment : Fragment() {
    private val args by navArgs<GearShowFragmentArgs>()
    private lateinit var mGearViewModel: GearViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.gear_show, container, false)
        setHasOptionsMenu(true)

        mGearViewModel = ViewModelProvider(this)[GearViewModel::class.java]
        val gearData = mGearViewModel.getGear(args.currentGear.id)
        gearData.observe(viewLifecycleOwner, Observer{
            gears ->
                run {
                    if(gears.isNotEmpty())
                    {
                        val gear = gears.first()
                        view.findViewById<TextView>(R.id.tw_gear_model).text = gear.model
                        view.findViewById<TextView>(R.id.tw_gear_manufacturer).text = gear.manufacturer
                        view.findViewById<TextView>(R.id.tw_gear_price).text = String.format("%.2f€", gear.price)

                        val simpleDateFormat by lazy { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
                        val dateFormatted = simpleDateFormat.format(Date(gear.date))
                        view.findViewById<TextView>(R.id.tw_gear_date).text = dateFormatted
                    }
                    else findNavController().navigateUp()
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
            val action = GearShowFragmentDirections.gearshowToGearadd(args.currentGear)
            findNavController().navigate(action)
        }
        else if(item.itemId == R.id.delete_show_menu) deleteGear()

        return super.onContextItemSelected(item)
    }

    private fun deleteGear()
    {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _,_ ->
            mGearViewModel.deleteGear(args.currentGear)
            Toast.makeText(requireContext(), "Gear deleted!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.gearshow_to_gearlist)
        }
        builder.setNegativeButton("No"){ _,_ -> }
        builder.setTitle("Delete ${args.currentGear.manufacturer} ${args.currentGear.model}?")
        builder.setMessage("Are you sure you want to delete this gear?")
        builder.create().show()
    }
}