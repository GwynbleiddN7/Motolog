package com.gwynn7.motolog.Fragments.Gear

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gwynn7.motolog.R
import com.gwynn7.motolog.UnitHelper
import com.gwynn7.motolog.ViewModel.GearViewModel
import com.gwynn7.motolog.longToDateString

class GearShowFragment : Fragment() {
    private val args by navArgs<GearShowFragmentArgs>()
    private lateinit var mGearViewModel: GearViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.gear_show, container, false)

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
                    view.findViewById<TextView>(R.id.tw_gear_price).text = String.format("%.2f%s", gear.price, UnitHelper.getCurrency())
                    view.findViewById<TextView>(R.id.tw_gear_date).text = longToDateString(gear.date)

                    val gearImage = view.findViewById<ImageView>(R.id.iv_gear_image_show)
                    if(gear.image != null) gearImage.setImageURI(gear.image)
                    else gearImage.setImageResource(R.drawable.helmet_show)

                    view.findViewById<ScrollView>(R.id.gear_show_view).visibility = View.VISIBLE
                }
                else findNavController().navigateUp()
            }
        })
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
                val action = GearShowFragmentDirections.gearshowToGearadd(args.currentGear)
                findNavController().navigate(action)
            }
            R.id.delete_show_menu -> deleteGear()
        }

        return super.onContextItemSelected(item)
    }

    private fun deleteGear()
    {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)){ _,_ ->
            mGearViewModel.deleteGear(args.currentGear)
            Toast.makeText(requireContext(), getString(R.string.gear_delete), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.gearshow_to_gearlist)
        }
        builder.setNegativeButton(getString(R.string.no)){ _,_ -> }
        builder.setTitle("${getString(R.string.delete)} ${args.currentGear.manufacturer} ${args.currentGear.model}?")
        builder.setMessage(getString(R.string.delete_gear_question))
        builder.create().show()
    }
}