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
import java.util.Locale

class EditInfoFragment : Fragment() {
    private val args by navArgs<EditInfoFragmentArgs>()
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel

    private lateinit var licenseplate: EditText
    private lateinit var price: EditText
    private lateinit var front_tire: EditText
    private lateinit var rear_tire: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.info_bikeinfo_edit, container, false)
        mMotorcycleViewModel = ViewModelProvider(this)[MotorcycleViewModel::class.java]

        val bike = args.currentBike

        licenseplate = view.findViewById(R.id.et_bike_license_plate)
        price = view.findViewById(R.id.et_bike_price)
        front_tire = view.findViewById(R.id.et_bike_front_tire)
        rear_tire = view.findViewById(R.id.et_bike_rear_tire)

        price.setText(bike.info.price.toString())
        licenseplate.setText(bike.info.plate_number)
        front_tire.setText(bike.info.front_tire)
        rear_tire.setText(bike.info.rear_tire)

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
            bike.info.front_tire = front_tire.text.toString()
            bike.info.rear_tire = rear_tire.text.toString()
            bike.info.plate_number = licenseplate.text.toString().uppercase(Locale.getDefault())
            bike.info.price = if(price.text.isNotEmpty()) price.text.toString().toDouble() else 0.0
            mMotorcycleViewModel.updateMotorcycle(bike, null)
            showToast(requireContext(), getString(R.string.info_saved))
            findNavController().navigateUp()
        }
        return super.onContextItemSelected(item)
    }
}