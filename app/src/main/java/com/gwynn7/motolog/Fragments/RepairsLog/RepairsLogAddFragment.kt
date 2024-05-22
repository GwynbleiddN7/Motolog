package com.gwynn7.motolog.Fragments.RepairsLog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gwynn7.motolog.Models.RepairsLog
import com.gwynn7.motolog.Path
import com.gwynn7.motolog.R
import com.gwynn7.motolog.UnitHelper
import com.gwynn7.motolog.ViewModel.MotorcycleViewModel
import com.gwynn7.motolog.capitalize
import com.gwynn7.motolog.dateFromLong
import com.gwynn7.motolog.longFromDate
import com.gwynn7.motolog.repairColors
import com.gwynn7.motolog.showToast
import java.util.Calendar
import java.util.Date

class RepairsLogAddFragment : Fragment() {
    private val args by navArgs<RepairsLogAddFragmentArgs>()
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel
    private var savedDate: Long = Date().time
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

        view.findViewById<TextView>(R.id.textView_repair_distance).text = capitalize(getString(R.string.bike_repair_distance, UnitHelper.getDistanceText(requireContext())))

        val notes = view.findViewById<EditText>(R.id.et_repair_notes)
        val distance = view.findViewById<EditText>(R.id.et_repair_distance)
        distance.setText(String.format("%d", args.currentBike.personal_km + args.currentBike.start_km))
        val type = view.findViewById<EditText>(R.id.et_repair_type)
        type.isSelected = true

        val spinner = view.findViewById<Spinner>(R.id.spinner_repair)
        var bEnableCallback = currentPath == Path.Add;

        if(currentPath == Path.Edit)
        {
            val currentLog = args.currentBike.logs.maintenance[args.logIndex]
            savedDate = currentLog.date

            if(currentLog.typeIndex == -1) {
                spinner.setSelection(repairTypes.size-1)
                type.setText(currentLog.typeText)
            }
            else {
                type.setText(repairTypes[currentLog.typeIndex])
                spinner.setSelection(currentLog.typeIndex)
            }

            notes.setText(currentLog.notes)
            view.findViewById<EditText>(R.id.et_repair_price).setText(currentLog.price.toString())
            distance.setText(currentLog.repair_km.toString())
        }

        val repairImage = view.findViewById<ImageView>(R.id.iv_repair_image)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                repairImage.setColorFilter(resources.getColor(repairColors[position], null));
                repairImage.visibility = View.VISIBLE
                if(bEnableCallback) {
                    if(position == repairTypes.size-1) {
                        type.setText("")
                        type.isEnabled = true
                        notes.setText("")
                    }
                    else{
                        type.setText(repairTypes[position])
                        type.isEnabled = false
                        notes.setText(repairsDefaultNotes[position])
                    }
                }

                bEnableCallback=true
            }
        }

        val date = view.findViewById<DatePicker>(R.id.dp_repair_date)
        date.maxDate = Date().time
        date.init(dateFromLong(savedDate, Calendar.YEAR), dateFromLong(savedDate, Calendar.MONTH), dateFromLong(savedDate, Calendar.DAY_OF_MONTH))
        { _, year, month, dayOfMonth ->
            savedDate = longFromDate(year, month, dayOfMonth)
        }


        val button = view.findViewById<Button>(R.id.bt_deleteRepair)
        button.visibility = if(currentPath == Path.Edit) View.VISIBLE else View.GONE
        button.setOnClickListener{
            deleteLog()
        }

        setHasOptionsMenu(true)
        return view
    }

    private fun insertDataToDatabase(view: View) {
        val type = view.findViewById<EditText>(R.id.et_repair_type).text.toString().trim()
        val notes = view.findViewById<EditText>(R.id.et_repair_notes).text.toString().trim()
        val price = view.findViewById<EditText>(R.id.et_repair_price).text.toString()
        val distance = view.findViewById<EditText>(R.id.et_repair_distance).text.toString()

        if(inputCheck(type, notes, price, distance))
        {
            var id = -1
            if(repairTypes.contains(type)) id = repairTypes.indexOf(type)

            val bike = args.currentBike
            val repairsLogList = bike.logs.maintenance.toMutableList()
            val distanceInt = distance.toInt()

            val alert = MaterialAlertDialogBuilder(requireContext())
                .setPositiveButton(getString(R.string.ok), null)
                .setTitle(getString(R.string.repairlog_nomatch))
            if(dateFromLong(savedDate, Calendar.YEAR) < bike.year) {
                alert
                    .setMessage(getString(R.string.log_nomatch_date))
                    .show()
                return
            }

            if(distanceInt < bike.start_km){
                alert
                    .setMessage(getString(R.string.log_nomatch_distance))
                    .show()
                return
            }

            if(currentPath == Path.Edit) repairsLogList.removeAt(args.logIndex)
            repairsLogList.add(RepairsLog(id, type, notes, savedDate, distanceInt, price.toDouble()))

            bike.logs.maintenance = repairsLogList.sortedBy { log -> log.date }.reversed()
            mMotorcycleViewModel.updateMotorcycle(bike, null)

            showToast(requireContext(), getString(R.string.log_saved))
            findNavController().navigateUp()
        }
        else showToast(requireContext(), getString(R.string.fill_fields))
    }

    private fun inputCheck(type: String, notes: String, price: String, distance: String): Boolean
    {
        return type.isNotEmpty() && notes.isNotEmpty() && price.isNotEmpty() && distance.isNotEmpty()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.save_menu) insertDataToDatabase(requireView())
        return super.onContextItemSelected(item)
    }

    private fun deleteLog()
    {
        MaterialAlertDialogBuilder(requireContext())
            .setPositiveButton(getString(R.string.yes)){ _,_ ->
                val bike = args.currentBike
                val repairsLogList = bike.logs.maintenance.toMutableList()
                repairsLogList.removeAt(args.logIndex)
                bike.logs.maintenance = repairsLogList.sortedBy { log -> log.date }.reversed()
                mMotorcycleViewModel.updateMotorcycle(bike, null)
                showToast(requireContext(), getString(R.string.log_removed))
                findNavController().navigateUp()
            }
            .setNegativeButton(getString(R.string.no), null)
            .setTitle(getString(R.string.title_question_remove_log))
            .setMessage(getString(R.string.description_question_remove_log))
            .show()
    }
}