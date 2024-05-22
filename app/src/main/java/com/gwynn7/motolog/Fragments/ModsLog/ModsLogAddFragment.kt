package com.gwynn7.motolog.Fragments.ModsLog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gwynn7.motolog.Models.ModsLog
import com.gwynn7.motolog.Path
import com.gwynn7.motolog.R
import com.gwynn7.motolog.ViewModel.MotorcycleViewModel
import com.gwynn7.motolog.dateFromLong
import com.gwynn7.motolog.longFromDate
import com.gwynn7.motolog.showToast
import java.util.Calendar
import java.util.Date

class ModsLogAddFragment : Fragment() {
    private val args by navArgs<ModsLogAddFragmentArgs>()
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel
    private var savedDate: Long = Date().time
    private var currentPath: Path = Path.Add
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modslog_add, container, false)
        mMotorcycleViewModel = ViewModelProvider(this)[MotorcycleViewModel::class.java]
        if(args.logIndex != -1) currentPath = Path.Edit

        if(currentPath == Path.Edit)
        {
            val currentLog = args.currentBike.logs.mods[args.logIndex]
            savedDate = currentLog.date

            val title = view.findViewById<EditText>(R.id.et_mod_title)
            title.setText(currentLog.title)
            title.isSelected = true

            view.findViewById<EditText>(R.id.et_mod_description).setText(currentLog.description)
            view.findViewById<EditText>(R.id.et_mod_price).setText(currentLog.price.toString())
        }

        val date = view.findViewById<DatePicker>(R.id.dp_mod_date)
        date.maxDate = Date().time
        date.init(dateFromLong(savedDate, Calendar.YEAR), dateFromLong(savedDate, Calendar.MONTH), dateFromLong(savedDate, Calendar.DAY_OF_MONTH))
        { _, year, month, dayOfMonth ->
            savedDate = longFromDate(year, month, dayOfMonth)
        }

        val button = view.findViewById<Button>(R.id.bt_deleteMod)
        button.visibility = if(currentPath == Path.Edit) View.VISIBLE else View.GONE
        button.setOnClickListener{
            deleteLog()
        }

        setHasOptionsMenu(true)
        return view
    }

    private fun insertDataToDatabase(view: View) {
        val title = view.findViewById<EditText>(R.id.et_mod_title).text.toString().trim()
        val description = view.findViewById<EditText>(R.id.et_mod_description).text.toString().trim()
        val price = view.findViewById<EditText>(R.id.et_mod_price).text.toString()

        if(inputCheck(title, description, price))
        {
            val bike = args.currentBike
            val modsLogList = bike.logs.mods.toMutableList()
            val newLog = ModsLog(title, description, savedDate, price.toDouble())

            if(currentPath == Path.Edit) modsLogList[args.logIndex] = newLog
            else modsLogList.add(0, newLog)

            bike.logs.mods = modsLogList.sortedBy { log -> log.date }.reversed()
            mMotorcycleViewModel.updateMotorcycle(bike, null)

            showToast(requireContext(), getString(R.string.log_saved))
            findNavController().navigateUp()
        }
        else showToast(requireContext(), getString(R.string.fill_fields))
    }

    private fun inputCheck(title: String, description: String, price: String): Boolean
    {
        return title.isNotEmpty() && description.isNotEmpty() && price.isNotEmpty()
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
            val modsLogList = bike.logs.mods.toMutableList()
            modsLogList.removeAt(args.logIndex)
            bike.logs.mods = modsLogList
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