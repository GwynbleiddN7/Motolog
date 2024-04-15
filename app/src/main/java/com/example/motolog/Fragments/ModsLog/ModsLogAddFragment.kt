package com.example.motolog.Fragments.ModsLog

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.motolog.Models.ActionLog
import com.example.motolog.Models.DistanceLog
import com.example.motolog.Path
import com.example.motolog.R
import com.example.motolog.ViewModel.MotorcycleViewModel
import com.example.motolog.showToast
import java.util.Calendar

class ModsLogAddFragment : Fragment() {
    private val args by navArgs<ModsLogAddFragmentArgs>()
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel
    private var savedDate: Long = 0
    private var currentPath: Path = Path.Add
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modslog_add, container, false)
        mMotorcycleViewModel = ViewModelProvider(this)[MotorcycleViewModel::class.java]
        if(args.logIndex != -1) currentPath = Path.Edit

        val buttonText = if(currentPath == Path.Add) "Add Log" else "Edit Log"
        val date = view.findViewById<CalendarView>(R.id.cv_mod_date)
        savedDate = Calendar.getInstance().timeInMillis
        date.maxDate = savedDate

        if(currentPath == Path.Edit)
        {
            val currentLog = args.currentBike.mods_logs[args.logIndex]
            savedDate = currentLog.date
            date.date = savedDate

            val title = view.findViewById<EditText>(R.id.et_mod_title)
            title.setText(currentLog.name)
            title.isSelected = true

            view.findViewById<EditText>(R.id.et_mod_description).setText(currentLog.description)
            view.findViewById<EditText>(R.id.et_mod_price).setText(currentLog.price.toString())
        }

        date.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val cal: Calendar = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            savedDate = cal.getTimeInMillis()
        }

        val button = view.findViewById<Button>(R.id.bt_addMod)
        button.text = buttonText
        button.setOnClickListener{
            insertDataToDatabase(view)
        }

        setHasOptionsMenu(currentPath == Path.Edit)
        return view
    }

    private fun insertDataToDatabase(view: View) {
        val title = view.findViewById<EditText>(R.id.et_mod_title).text.toString()
        val description = view.findViewById<EditText>(R.id.et_mod_description).text.toString()
        val price = view.findViewById<EditText>(R.id.et_mod_price).text.toString()

        if(inputCheck(title, description, price))
        {
            val bike = args.currentBike
            val modsLogList = bike.mods_logs.toMutableList()
            val newLog = ActionLog(title, description, savedDate, price.toDouble())

            if(currentPath == Path.Edit) modsLogList[args.logIndex] = newLog
            else modsLogList.add(0, newLog)

            bike.mods_logs = modsLogList
            mMotorcycleViewModel.updateMotorcycle(bike, null)

            showToast(requireContext(), "Log saved!")
            findNavController().navigateUp()
        }
        else showToast(requireContext(), "Please enter a distance")
    }

    private fun inputCheck(title: String, description: String, price: String): Boolean
    {
        return title.isNotEmpty() && description.isNotEmpty() && price.isNotEmpty()
    }
    private fun inputCheck(log: DistanceLog, distance: Int): Boolean{
        return (log.date < savedDate && log.distance > distance) || (log.date > savedDate && log.distance < distance)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete) deleteLog()
        return super.onContextItemSelected(item)
    }

    private fun deleteLog()
    {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _,_ ->
            val bike = args.currentBike
            val modsLogList = bike.mods_logs.toMutableList()
            modsLogList.removeAt(args.logIndex)
            bike.mods_logs = modsLogList
            mMotorcycleViewModel.updateMotorcycle(bike, null)
            showToast(requireContext(), "Log removed!")
            findNavController().navigateUp()
        }
        builder.setNegativeButton("No"){ _,_ -> }
        builder.setTitle("Delete the log?")
        builder.setMessage("Are you sure you want to delete this log?")
        builder.create().show()
    }
}