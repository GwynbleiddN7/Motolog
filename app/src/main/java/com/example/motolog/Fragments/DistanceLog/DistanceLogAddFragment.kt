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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.motolog.Models.DistanceLog
import com.example.motolog.Models.getUpdatedBikeDistance
import com.example.motolog.Path
import com.example.motolog.R
import com.example.motolog.ViewModel.MotorcycleViewModel
import com.example.motolog.showToast
import com.example.motolog.yearFromLong
import java.util.Calendar

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

        val buttonText = if(currentPath == Path.Add) getString(R.string.add_log) else getString(R.string.edit_log)
        val date = view.findViewById<CalendarView>(R.id.cv_distancelog_date)
        savedDate = Calendar.getInstance().timeInMillis
        date.maxDate = savedDate

        val distanceLog = view.findViewById<EditText>(R.id.et_distancelog)
        if(currentPath == Path.Edit)
        {
            val currentLog = args.currentBike.km_logs[args.logIndex]
            savedDate = currentLog.date
            date.date = savedDate

            distanceLog.setText(currentLog.distance.toString())
        }
        else distanceLog.setText(String.format("%d", args.currentBike.start_km + args.currentBike.personal_km))

        date.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val cal: Calendar = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            savedDate = cal.getTimeInMillis()
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
            val bike = args.currentBike
            val distanceLogList = bike.km_logs.toMutableList()

            if(yearFromLong(savedDate) < bike.year || distanceInt < bike.start_km) {
                showToast(requireContext(), getString(R.string.log_nomatch_1))
                return
            }

            for(log in distanceLogList){
                if(inputCheck(log, distanceInt)) {
                    showToast(requireContext(), getString(R.string.log_nomatch_2))
                    return
                }
            }

            if(currentPath == Path.Edit) distanceLogList.removeAt(args.logIndex)
            distanceLogList.add(DistanceLog(distanceInt, savedDate))

            bike.km_logs = distanceLogList.sortedBy { log -> log.date }.reversed()
            bike.personal_km = getUpdatedBikeDistance(bike)
            mMotorcycleViewModel.updateMotorcycle(bike, null)

            showToast(requireContext(), getString(R.string.log_saved))
            findNavController().navigateUp()
        }
        else showToast(requireContext(), getString(R.string.fill_fields))
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
        builder.setPositiveButton(getString(R.string.yes)){ _,_ ->
            val bike = args.currentBike
            val distanceLogList = bike.km_logs.toMutableList()
            distanceLogList.removeAt(args.logIndex)
            bike.km_logs = distanceLogList.sortedBy { log -> log.date }.reversed()
            bike.personal_km = getUpdatedBikeDistance(bike)
            mMotorcycleViewModel.updateMotorcycle(bike, null)
            showToast(requireContext(), getString(R.string.log_removed))
            findNavController().navigateUp()
        }
        builder.setNegativeButton(getString(R.string.no)){ _,_ -> }
        builder.setTitle(getString(R.string.title_question_remove_log))
        builder.setMessage(getString(R.string.description_question_remove_log))
        builder.create().show()
    }
}