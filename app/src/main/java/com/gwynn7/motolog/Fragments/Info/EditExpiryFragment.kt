package com.gwynn7.motolog.Fragments.Info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.DatePicker
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gwynn7.motolog.R
import com.gwynn7.motolog.ViewModel.MotorcycleViewModel
import com.gwynn7.motolog.dateFromLong
import com.gwynn7.motolog.longFromDate
import com.gwynn7.motolog.showToast
import java.util.Calendar
import java.util.Date

class EditExpiryFragment : Fragment() {
    private val args by navArgs<EditExpiryFragmentArgs>()
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel

    private var taxDate: Long = Date().time
    private var insuranceDate: Long = Date().time
    private var inspectionDate: Long = Date().time
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.info_expiryinfo_edit, container, false)
        mMotorcycleViewModel = ViewModelProvider(this)[MotorcycleViewModel::class.java]

        if(args.currentBike.expiry.tax >= 0) taxDate = args.currentBike.expiry.tax
        view.findViewById<DatePicker>(R.id.dp_tax).init(dateFromLong(taxDate, Calendar.YEAR), dateFromLong(taxDate, Calendar.MONTH),dateFromLong(taxDate, Calendar.DAY_OF_MONTH))
        { _, year, monthOfYear, dayOfMonth ->
            taxDate = longFromDate(year, monthOfYear, dayOfMonth)
        }

        if(args.currentBike.expiry.inspection >= 0) inspectionDate = args.currentBike.expiry.inspection
        view.findViewById<DatePicker>(R.id.dp_inspection).init(dateFromLong(inspectionDate, Calendar.YEAR), dateFromLong(inspectionDate, Calendar.MONTH),dateFromLong(inspectionDate, Calendar.DAY_OF_MONTH))
        { _, year, monthOfYear, dayOfMonth ->
            inspectionDate = longFromDate(year, monthOfYear, dayOfMonth)
        }

        if(args.currentBike.expiry.insurance >= 0) insuranceDate = args.currentBike.expiry.insurance
        view.findViewById<DatePicker>(R.id.dp_insurance).init(dateFromLong(insuranceDate, Calendar.YEAR), dateFromLong(insuranceDate, Calendar.MONTH),dateFromLong(insuranceDate, Calendar.DAY_OF_MONTH))
        { _, year, monthOfYear, dayOfMonth ->
            insuranceDate = longFromDate(year, monthOfYear, dayOfMonth)
        }

        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.save_menu)
        {
            val bike = args.currentBike
            bike.expiry.tax = taxDate
            bike.expiry.insurance = insuranceDate
            bike.expiry.inspection = inspectionDate
            mMotorcycleViewModel.updateMotorcycle(bike, null)
            showToast(requireContext(), getString(R.string.info_saved))
            findNavController().navigateUp()
        }
        return super.onContextItemSelected(item)
    }
}