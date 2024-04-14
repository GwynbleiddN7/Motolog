package com.example.motolog.Fragments.DistanceLog

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
import com.example.motolog.Models.DistanceLog
import com.example.motolog.Path
import com.example.motolog.R
import com.example.motolog.ViewModel.MotorcycleViewModel
import java.util.Calendar
import java.util.Date

class DistanceLogAddFragment : Fragment() {
    private val args by navArgs<DistanceLogAddFragmentArgs>()
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel
    private var savedDate: Long = 0
    private var currentPath: Path = Path.Add
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.distancelog_add, container, false)

        mMotorcycleViewModel = ViewModelProvider(this)[MotorcycleViewModel::class.java]

        if(args.logIndex != -1) currentPath = Path.Edit

        val buttonText = if(currentPath == Path.Add) "Add Log" else "Edit Log"

        if(currentPath == Path.Edit)
        {
            val currentLog = args.currentBike!!.km_logs[args.logIndex]
            savedDate = currentLog.date
            val date = view.findViewById<CalendarView>(R.id.cv_distancelog_date)
            date.maxDate = Date().time
            date.date = savedDate
            date.setOnDateChangeListener { _, year, month, dayOfMonth ->
                val c: Calendar = Calendar.getInstance()
                c.set(year, month, dayOfMonth)
                savedDate = c.getTimeInMillis()
            }

            view.findViewById<EditText>(R.id.et_distancelog).setText(currentLog.distance.toString())
        }
        else
        {
            savedDate = Calendar.getInstance().timeInMillis
            val date = view.findViewById<CalendarView>(R.id.cv_distancelog_date)
            date.maxDate = savedDate
            date.setOnDateChangeListener { _, year, month, dayOfMonth ->
                val c: Calendar = Calendar.getInstance()
                c.set(year, month, dayOfMonth)
                savedDate = c.getTimeInMillis()
            }
        }


        val button = view.findViewById<Button>(R.id.bt_addDistanceLog)
        button.text = buttonText
        button.setOnClickListener{
            insertDataToDatabase(view)
        }

        setHasOptionsMenu(currentPath == Path.Edit)

        return view
    }

    private fun insertDataToDatabase(view: View) {
        val distance = view.findViewById<EditText>(R.id.et_distancelog).text.toString()

        if(distance.isNotEmpty())
        {
            val distanceInt = distance.toInt()
            val bike = args.currentBike!!
            val list = bike.km_logs.toMutableList()

            val cal: Calendar = Calendar.getInstance()
            cal.setTime(Date(savedDate))
            if(cal.get(Calendar.YEAR) < bike.year || distanceInt < bike.start_km)
            {
                Toast.makeText(requireContext(), "Distance/Date mismatch", Toast.LENGTH_SHORT).show()
                return
            }

            for(log in list){
                if(inputCheck(log, distanceInt))
                {
                    Toast.makeText(requireContext(), "Distance/Date mismatch", Toast.LENGTH_SHORT).show()
                    return
                }
            }
            if(currentPath == Path.Edit) list.removeAt(args.logIndex)
            list.add(DistanceLog(distanceInt, savedDate))

            bike.km_logs = list.sortedBy { log -> log.date }.reversed()
            var personal_km = 0
            for(log in bike.km_logs) personal_km += log.distance
            bike.personal_km = personal_km
            mMotorcycleViewModel.updateMotorcycle(bike)

            Toast.makeText(requireContext(), "Log saved!", Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
        }
        else
        {
            Toast.makeText(requireContext(), "Please fill every field", Toast.LENGTH_LONG).show()
        }
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
            val bike = args.currentBike!!
            val list = bike.km_logs.toMutableList()
            list.removeAt(args.logIndex)
            bike.km_logs = list.sortedBy { log -> log.date }.reversed()
            var personal_km = 0
            for(log in bike.km_logs) personal_km += log.distance
            bike.personal_km = personal_km
            mMotorcycleViewModel.updateMotorcycle(bike)
            Toast.makeText(requireContext(), "Gear deleted!", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
        builder.setNegativeButton("No"){ _,_ -> }
        builder.setTitle("Delete the log?")
        builder.setMessage("Are you sure you want to delete this log?")
        builder.create().show()
    }
}