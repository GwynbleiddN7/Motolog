package com.example.motolog.Fragments.RepairsLog

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.motolog.Models.RepairsLog
import com.example.motolog.Path
import com.example.motolog.R
import com.example.motolog.ViewModel.MotorcycleViewModel
import com.example.motolog.repairColors
import com.example.motolog.showToast
import java.util.Calendar

class RepairsLogAddFragment : Fragment() {
    private val args by navArgs<RepairsLogAddFragmentArgs>()
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel
    private var savedDate: Long = 0
    private var currentPath: Path = Path.Add
    private var repairTypes: List<String> = listOf()
    private var repairsDefaultNotes: List<String> = listOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.repairslog_add, container, false)

        repairTypes = resources.getStringArray(R.array.repair_types).toList()
        repairsDefaultNotes = resources.getStringArray(R.array.repair_notes).toList()

        mMotorcycleViewModel = ViewModelProvider(this)[MotorcycleViewModel::class.java]
        if(args.logIndex != -1) currentPath = Path.Edit

        val buttonText = if(currentPath == Path.Add) getString(R.string.add_log) else getString(R.string.edit_log)
        val date = view.findViewById<CalendarView>(R.id.cv_repair_date)
        savedDate = Calendar.getInstance().timeInMillis
        date.maxDate = savedDate

        val notes = view.findViewById<EditText>(R.id.et_repair_notes)
        val type = view.findViewById<EditText>(R.id.et_repair_type)
        type.isSelected = true

        val spinner = view.findViewById<Spinner>(R.id.spinner_repair)
        var bEnableCallback = false;

        if(currentPath == Path.Edit)
        {
            val currentLog = args.currentBike.maintenance_logs[args.logIndex]
            savedDate = currentLog.date
            date.date = savedDate

            if(currentLog.typeIndex == -1)
            {
                spinner.setSelection(repairTypes.size-1)
                type.setText(currentLog.typeText)
            }
            else {
                type.setText(repairTypes[currentLog.typeIndex])
                spinner.setSelection(currentLog.typeIndex)
            }

            notes.setText(currentLog.notes)
            view.findViewById<EditText>(R.id.et_repair_price).setText(currentLog.price.toString())
        }

        val repairImage = view.findViewById<ImageView>(R.id.iv_repair_image)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                repairImage.setColorFilter(resources.getColor(repairColors[position], null));
                repairImage.visibility = VISIBLE
                if(position == repairTypes.size-1) {
                    if(bEnableCallback)
                    {
                        type.setText("")
                        type.isEnabled = true
                        notes.setText("")
                    }
                }
                else{
                    type.setText(repairTypes[position])
                    type.isEnabled = false
                    notes.setText(repairsDefaultNotes[position])
                }
                bEnableCallback= true
            }
        }

        date.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val cal: Calendar = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            savedDate = cal.getTimeInMillis()
        }

        val button = view.findViewById<Button>(R.id.bt_addRepair)
        button.text = buttonText
        button.setOnClickListener{
            insertDataToDatabase(view)
        }

        setHasOptionsMenu(currentPath == Path.Edit)
        return view
    }

    private fun insertDataToDatabase(view: View) {
        val type = view.findViewById<EditText>(R.id.et_repair_type).text.toString()
        val notes = view.findViewById<EditText>(R.id.et_repair_notes).text.toString()
        val price = view.findViewById<EditText>(R.id.et_repair_price).text.toString()

        if(inputCheck(type, notes, price))
        {
            var id = -1
            if(repairTypes.contains(type)) id = repairTypes.indexOf(type)

            val bike = args.currentBike
            val repairsLogList = bike.maintenance_logs.toMutableList()

            if(currentPath == Path.Edit) repairsLogList.removeAt(args.logIndex)
            repairsLogList.add(RepairsLog(id, type, notes, savedDate, price.toDouble()))

            bike.maintenance_logs = repairsLogList.sortedBy { log -> log.date }.reversed()
            mMotorcycleViewModel.updateMotorcycle(bike, null)

            showToast(requireContext(), getString(R.string.log_saved))
            findNavController().navigateUp()
        }
        else showToast(requireContext(), getString(R.string.fill_fields))
    }

    private fun inputCheck(type: String, notes: String, price: String): Boolean
    {
        return type.isNotEmpty() && notes.isNotEmpty() && price.isNotEmpty()
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
            val repairsLogList = bike.maintenance_logs.toMutableList()
            repairsLogList.removeAt(args.logIndex)
            bike.maintenance_logs = repairsLogList.sortedBy { log -> log.date }.reversed()
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