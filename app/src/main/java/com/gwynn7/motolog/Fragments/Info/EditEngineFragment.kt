package com.gwynn7.motolog.Fragments.Info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gwynn7.motolog.R
import com.gwynn7.motolog.ViewModel.MotorcycleViewModel
import com.gwynn7.motolog.showToast

class EditEngineFragment : Fragment() {
    private val args by navArgs<EditEngineFragmentArgs>()
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel

    private lateinit var bike_cc: EditText
    private lateinit var cylinders: EditText
    private lateinit var torque: EditText
    private lateinit var power: EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.info_engineinfo_edit, container, false)
        mMotorcycleViewModel = ViewModelProvider(this)[MotorcycleViewModel::class.java]

        val bike = args.currentBike

        bike_cc = view.findViewById(R.id.et_bike_cc)
        cylinders = view.findViewById(R.id.et_bike_cylinders)
        torque = view.findViewById(R.id.et_bike_torque)
        power = view.findViewById(R.id.et_bike_hp)

        bike_cc.setText(bike.info.engine_cc.toString())
        power.setText(bike.info.horse_power.toString())
        cylinders.setText(bike.info.cylinders.toString())
        torque.setText(bike.info.torque.toString())

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
            bike.info.cylinders = if(cylinders.text.isNotEmpty()) cylinders.text.toString().toInt() else 0
            bike.info.engine_cc = if(bike_cc.text.isNotEmpty())  bike_cc.text.toString().toDouble() else 0.0
            bike.info.torque = if(torque.text.isNotEmpty())  torque.text.toString().toDouble() else 0.0
            bike.info.horse_power = if(power.text.isNotEmpty()) power.text.toString().toDouble() else 0.0
            mMotorcycleViewModel.updateMotorcycle(bike, null)
            showToast(requireContext(), getString(R.string.info_saved))
            findNavController().navigateUp()
        }
        return super.onContextItemSelected(item)
    }
}