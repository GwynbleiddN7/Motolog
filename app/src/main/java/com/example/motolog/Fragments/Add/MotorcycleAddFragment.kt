package com.example.motolog.Fragments.Add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.motolog.Models.Motorcycle
import com.example.motolog.R
import com.example.motolog.ViewModel.MotorcycleViewModel

class MotorcycleAddFragment : Fragment() {
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.motorcycle_add, container, false)
        mMotorcycleViewModel = ViewModelProvider(this)[MotorcycleViewModel::class.java]

        view.findViewById<Button>(R.id.bt_addMotorcycle).setOnClickListener {
            insertDataToDatabase(view)
        }

        return view
    }

    private fun insertDataToDatabase(view: View) {
        val manufacturer = view.findViewById<EditText>(R.id.et_bike_manufacturer).text.toString()
        val model = view.findViewById<EditText>(R.id.et_bike_model).text.toString()
        val name = view.findViewById<EditText>(R.id.et_bike_alias).text.toString()
        val year = view.findViewById<EditText>(R.id.et_bike_year).text.toString()
        val price = view.findViewById<EditText>(R.id.et_bike_price).text.toString()

        if(inputCheck(manufacturer, model, year))
        {
            val motorcycle = Motorcycle(0, manufacturer, model, name, year.toInt(), price.toDouble())
            mMotorcycleViewModel.addMotorcycle(motorcycle);
            Toast.makeText(requireContext(), "Motorcycle added!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.bikeadd_to_bikelist)
        }
        else
        {
            Toast.makeText(requireContext(), "Everything expect 'Name' is mandatory", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(manufacturer: String, model: String, year: String): Boolean
    {
        return manufacturer.isNotEmpty() && model.isNotEmpty() && year.isNotEmpty()
    }
}