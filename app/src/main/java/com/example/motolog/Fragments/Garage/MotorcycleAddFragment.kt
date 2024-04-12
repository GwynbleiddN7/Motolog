package com.example.motolog.Fragments.Garage

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
import com.example.motolog.Path
import com.example.motolog.R
import com.example.motolog.ViewModel.MotorcycleViewModel

class MotorcycleAddFragment : Fragment() {
    private val args by navArgs<MotorcycleAddFragmentArgs>()
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel
    private var currentPath: Path = Path.Add
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.motorcycle_add, container, false)
        mMotorcycleViewModel = ViewModelProvider(this)[MotorcycleViewModel::class.java]

        if(args.currentMotorcycle != null) currentPath = Path.Edit

        val buttonText = if(currentPath == Path.Add) "Add Motorcycle" else "Edit Motorcycle"

        if(currentPath == Path.Edit) {
            val bike = args.currentMotorcycle!!
            view.findViewById<EditText>(R.id.et_bike_manufacturer).setText(bike.manufacturer)
            view.findViewById<EditText>(R.id.et_bike_model).setText(bike.model)
            view.findViewById<EditText>(R.id.et_bike_alias).setText(bike.alias)
            view.findViewById<EditText>(R.id.et_bike_year).setText(bike.year.toString())
            view.findViewById<EditText>(R.id.et_bike_startkm).setText(bike.start_km.toString())
        }

        val button = view.findViewById<Button>(R.id.bt_addMotorcycle)
        button.text = buttonText
        button.setOnClickListener {
            insertDataToDatabase(view)
        }

        setHasOptionsMenu(currentPath == Path.Edit)
        return view
    }

    private fun insertDataToDatabase(view: View) {
        val manufacturer = view.findViewById<EditText>(R.id.et_bike_manufacturer).text.toString()
        val model = view.findViewById<EditText>(R.id.et_bike_model).text.toString()
        val name = view.findViewById<EditText>(R.id.et_bike_alias).text.toString()
        val year = view.findViewById<EditText>(R.id.et_bike_year).text.toString()
        val start_km = view.findViewById<EditText>(R.id.et_bike_startkm).text.toString()

        if(inputCheck(manufacturer, model, year))
        {
            val id = if(currentPath == Path.Edit) args.currentMotorcycle!!.id else 0

            val km = if(start_km.isEmpty()) 0.0 else start_km.toDouble()
            val motorcycle = Motorcycle(id, manufacturer, model, name, year.toInt(), km)
            if(currentPath == Path.Add) mMotorcycleViewModel.addMotorcycle(motorcycle)
            else mMotorcycleViewModel.updateMotorcycle(motorcycle)
            Toast.makeText(requireContext(), "Motorcycle saved!", Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
        }
        else
        {
            Toast.makeText(requireContext(), "Please fill every field (Alias is optional)", Toast.LENGTH_LONG).show()
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
            mMotorcycleViewModel.deleteMotorcycle(args.currentMotorcycle!!)
            Toast.makeText(requireContext(), "Motorcycle deleted!", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
        builder.setNegativeButton("No"){ _,_ -> }
        builder.setTitle("Delete ${args.currentMotorcycle!!.manufacturer} ${args.currentMotorcycle!!.model}?")
        builder.setMessage("Are you sure you want to delete this motorcycle?")
        builder.create().show()
    }
}