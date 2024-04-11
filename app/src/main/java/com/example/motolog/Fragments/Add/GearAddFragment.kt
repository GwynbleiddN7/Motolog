package com.example.motolog.Fragments.Add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.motolog.Models.Gear
import com.example.motolog.R
import com.example.motolog.ViewModel.GearViewModel
import java.util.Calendar

class GearAddFragment : Fragment() {
    private lateinit var mGearViewModel: GearViewModel
    private var savedDate: Long = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.gear_add, container, false)
        mGearViewModel = ViewModelProvider(this)[GearViewModel::class.java]

        savedDate = Calendar.getInstance().timeInMillis
        val date = view.findViewById<CalendarView>(R.id.cv_gear_date)
        date.maxDate = savedDate
        date.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val c: Calendar = Calendar.getInstance()
            c.set(year, month, dayOfMonth)
            savedDate = c.getTimeInMillis()
        }

        view.findViewById<Button>(R.id.bt_addGear).setOnClickListener{
            insertDataToDatabase(view)
        }

        return view
    }

    private fun insertDataToDatabase(view: View) {
        val manufacturer = view.findViewById<EditText>(R.id.et_gear_manufacturer).text.toString()
        val model = view.findViewById<EditText>(R.id.et_gear_model).text.toString()
        val price = view.findViewById<EditText>(R.id.et_gear_price).text.toString()

        if(inputCheck(manufacturer, model, price))
        {
            val gear = Gear(0, manufacturer, model, price.toDouble(), savedDate)
            mGearViewModel.addGear(gear);
            Toast.makeText(requireContext(), "Gear added!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.gearadd_to_gearlist)
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
}