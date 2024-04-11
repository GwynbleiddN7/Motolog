package com.example.motolog.Fragments.Update

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.motolog.Models.Gear
import com.example.motolog.R
import com.example.motolog.ViewModel.GearViewModel
import java.util.Calendar
import java.util.Date

class GearUpdateFragment : Fragment() {
    private val args by navArgs<GearUpdateFragmentArgs>()
    private lateinit var mGearViewModel: GearViewModel
    private var savedDate: Long = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.gear_edit, container, false)
        mGearViewModel = ViewModelProvider(this)[GearViewModel::class.java]

        savedDate = args.currentGear.date
        val date = view.findViewById<CalendarView>(R.id.cv_gear_date_edit)
        date.maxDate = Date().time
        date.date = savedDate
        date.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val c: Calendar = Calendar.getInstance()
            c.set(year, month, dayOfMonth)
            savedDate = c.getTimeInMillis()
        }

        view.findViewById<EditText>(R.id.et_gear_manufacturer_edit).setText(args.currentGear.manufacturer)
        view.findViewById<EditText>(R.id.et_gear_model_edit).setText(args.currentGear.model)
        view.findViewById<EditText>(R.id.et_gear_price_edit).setText(args.currentGear.price.toString())

        view.findViewById<Button>(R.id.bt_editGear).setOnClickListener{
            updateItem(view)
        }
        setHasOptionsMenu(true)

        return view
    }

    private fun updateItem(view: View){
        val manufacturer = view.findViewById<EditText>(R.id.et_gear_manufacturer_edit).text.toString()
        val model = view.findViewById<EditText>(R.id.et_gear_model_edit).text.toString()
        val price = view.findViewById<EditText>(R.id.et_gear_price_edit).text.toString()

        if(inputCheck(manufacturer, model, price))
        {
            val gear = Gear(args.currentGear.id, manufacturer, model, price.toDouble(), savedDate)
            mGearViewModel.updateGear(gear);
            Toast.makeText(requireContext(), "Gear updated!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.gearupdate_to_gearlist)
        }
        else
        {
            Toast.makeText(requireContext(), "Please fill every field", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(manufacturer: String, model: String, price: String): Boolean
    {
        return manufacturer.isNotEmpty() && model.isNotEmpty() && price.isNotEmpty()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete) deleteGear()
        return super.onContextItemSelected(item)
    }

    private fun deleteGear()
    {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _,_ ->
            mGearViewModel.deleteGear(args.currentGear)
            Toast.makeText(requireContext(), "Gear deleted!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.gearupdate_to_gearlist)
        }
        builder.setNegativeButton("No"){ _,_ -> }
        builder.setTitle("Delete ${args.currentGear.manufacturer} ${args.currentGear.model}?")
        builder.setMessage("Are you sure you want to delete this gear?")
        builder.create().show()
    }
}