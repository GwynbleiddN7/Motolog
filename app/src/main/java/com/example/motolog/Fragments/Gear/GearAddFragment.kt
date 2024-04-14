package com.example.motolog.Fragments.Gear

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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.motolog.Models.Gear
import com.example.motolog.Path
import com.example.motolog.R
import com.example.motolog.ViewModel.GearViewModel
import com.example.motolog.showToast
import java.util.Calendar

class GearAddFragment : Fragment() {
    private lateinit var mGearViewModel: GearViewModel
    private val args by navArgs<GearAddFragmentArgs>()
    private var savedDate: Long = 0
    private var currentPath: Path = Path.Add

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.gear_add, container, false)
        mGearViewModel = ViewModelProvider(this)[GearViewModel::class.java]

        if(args.currentGear != null) currentPath = Path.Edit

        val buttonText = if(currentPath == Path.Add) "Add Gear" else "Edit Gear"
        val date = view.findViewById<CalendarView>(R.id.cv_gear_date)
        savedDate = Calendar.getInstance().timeInMillis
        date.maxDate = savedDate

        if(currentPath == Path.Edit)
        {
            val currentGear = args.currentGear!!
            savedDate = currentGear.date
            date.date = savedDate

            view.findViewById<EditText>(R.id.et_gear_manufacturer).setText(currentGear.manufacturer)
            view.findViewById<EditText>(R.id.et_gear_model).setText(currentGear.model)
            view.findViewById<EditText>(R.id.et_gear_price).setText(currentGear.price.toString())
        }

        date.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val cal: Calendar = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            savedDate = cal.getTimeInMillis()
        }

        val button = view.findViewById<Button>(R.id.bt_addGear)
        button.text = buttonText
        button.setOnClickListener{
            insertDataToDatabase(view)
        }

        setHasOptionsMenu(currentPath == Path.Edit)
        return view
    }

    private fun insertDataToDatabase(view: View) {
        val manufacturer = view.findViewById<EditText>(R.id.et_gear_manufacturer).text.toString()
        val model = view.findViewById<EditText>(R.id.et_gear_model).text.toString()
        val price = view.findViewById<EditText>(R.id.et_gear_price).text.toString()

        if(inputCheck(manufacturer, model, price))
        {
            val id = if(currentPath == Path.Edit) args.currentGear!!.id else 0

            val gear = Gear(id, manufacturer, model, price.toDouble(), savedDate)
            if(currentPath == Path.Add) mGearViewModel.addGear(gear)
            else mGearViewModel.updateGear(gear)

            showToast(requireContext(), "Gear saved!")
            findNavController().navigateUp()
        }
        else showToast(requireContext(), "Please fill every field")
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
        val currentGear = args.currentGear!!
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _,_ ->
            mGearViewModel.deleteGear(currentGear)
            showToast(requireContext(), "Gear deleted!")
            findNavController().navigateUp()
        }
        builder.setNegativeButton("No"){ _,_ -> }
        builder.setTitle("Delete ${currentGear.manufacturer} ${currentGear.model}?")
        builder.setMessage("Are you sure you want to delete this gear?")
        builder.create().show()
    }
}