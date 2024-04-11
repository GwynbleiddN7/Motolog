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
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.motolog.Models.Motorcycle
import com.example.motolog.R
import com.example.motolog.ViewModel.MotorcycleViewModel

class MotorcycleUpdateFragment : Fragment() {
    private val args by navArgs<MotorcycleUpdateFragmentArgs>()
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.motorcycle_edit, container, false)

        mMotorcycleViewModel = ViewModelProvider(this)[MotorcycleViewModel::class.java]

        view.findViewById<EditText>(R.id.et_bike_manufacturer_edit).setText(args.currentMotorcycle.manufacturer)
        view.findViewById<EditText>(R.id.et_bike_model_edit).setText(args.currentMotorcycle.model)
        view.findViewById<EditText>(R.id.et_bike_alias_edit).setText(args.currentMotorcycle.alias)
        view.findViewById<EditText>(R.id.et_bike_year_edit).setText(args.currentMotorcycle.year.toString())
        view.findViewById<EditText>(R.id.et_bike_price_edit).setText(args.currentMotorcycle.price.toString())

        view.findViewById<Button>(R.id.bt_editMotorcycle).setOnClickListener{
            updateItem(view)
        }
        setHasOptionsMenu(true)
        return view
    }

    private fun updateItem(view: View){
        val manufacturer = view.findViewById<EditText>(R.id.et_bike_manufacturer_edit).text.toString()
        val model = view.findViewById<EditText>(R.id.et_bike_model_edit).text.toString()
        val name = view.findViewById<EditText>(R.id.et_bike_alias_edit).text.toString()
        val year = view.findViewById<EditText>(R.id.et_bike_year_edit).text.toString()
        val price = view.findViewById<EditText>(R.id.et_bike_price_edit).text.toString()

        if(inputCheck(manufacturer, model, year))
        {
            val motorcycle = Motorcycle(args.currentMotorcycle.id, manufacturer, model, name, year.toInt(), price.toDouble())
            mMotorcycleViewModel.updateMotorcycle(motorcycle);
            Toast.makeText(requireContext(), "Motorcycle updated!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.bikeupdate_to_bikelist)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete) deleteMotorcycle()
        return super.onContextItemSelected(item)
    }

    private fun deleteMotorcycle()
    {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _,_ ->
            mMotorcycleViewModel.deleteMotorcycle(args.currentMotorcycle)
            Toast.makeText(requireContext(), "Motorcycle deleted!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.bikeupdate_to_bikelist)
        }
        builder.setNegativeButton("No"){ _,_ -> }
        builder.setTitle("Delete ${args.currentMotorcycle.manufacturer} ${args.currentMotorcycle.model}?")
        builder.setMessage("Are you sure you want to delete this motorcycle?")
        builder.create().show()
    }
}