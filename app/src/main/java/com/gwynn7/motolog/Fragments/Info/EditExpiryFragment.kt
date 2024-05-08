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
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gwynn7.motolog.Path
import com.gwynn7.motolog.R
import com.gwynn7.motolog.ViewModel.MotorcycleViewModel
import com.gwynn7.motolog.longFromDate
import com.gwynn7.motolog.showToast
import java.util.Calendar

class EditExpiryFragment : Fragment() {
    private val args by navArgs<EditExpiryFragmentArgs>()
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel

    private var taxDate: Long = -1
    private var insuranceDate: Long = -1
    private var inspectionDate: Long = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.info_expiryinfo_edit, container, false)
        mMotorcycleViewModel = ViewModelProvider(this)[MotorcycleViewModel::class.java]

        val cvTaxDate = view.findViewById<CalendarView>(R.id.cv_tax)
        if(args.currentBike.expiry.tax >= 0)
        {
            taxDate = args.currentBike.expiry.tax
            cvTaxDate.date = taxDate
        }
        cvTaxDate.setOnDateChangeListener { _, year, month, dayOfMonth ->
            taxDate = longFromDate(year, month, dayOfMonth)
        }

        val cvInspectionDate = view.findViewById<CalendarView>(R.id.cv_inspection)
        if(args.currentBike.expiry.inspection >= 0)
        {
            inspectionDate = args.currentBike.expiry.inspection
            cvInspectionDate.date = inspectionDate
        }
        cvInspectionDate.setOnDateChangeListener { _, year, month, dayOfMonth ->
            inspectionDate = longFromDate(year, month, dayOfMonth)
        }

        val cvInsuranceDate = view.findViewById<CalendarView>(R.id.cv_insurance)
        if(args.currentBike.expiry.insurance >= 0)
        {
            insuranceDate = args.currentBike.expiry.insurance
            cvInsuranceDate.date = insuranceDate
        }
        cvInsuranceDate.setOnDateChangeListener { _, year, month, dayOfMonth ->
            insuranceDate = longFromDate(year, month, dayOfMonth)
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