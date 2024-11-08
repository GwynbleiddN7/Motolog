package com.gwynn7.motolog.Fragments.Garage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gwynn7.motolog.R
import com.gwynn7.motolog.ViewModel.MotorcycleViewModel
import com.gwynn7.motolog.formatThousand
import com.gwynn7.motolog.showToast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gwynn7.motolog.UnitHelper
import com.gwynn7.motolog.settings
import com.gwynn7.motolog.showToastAfterDelay
import kotlinx.coroutines.runBlocking

class MotorcycleListFragment : Fragment() {
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.motorcycle_list, container, false)

        val adapter = MotorcycleListAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.rw_motorcycles)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        /*
        val tempFixKey = intPreferencesKey("temp-fix")
        runBlocking {
            requireContext().settings.edit { s -> s.remove(tempFixKey) }
        }*/

        mMotorcycleViewModel = ViewModelProvider(this)[MotorcycleViewModel::class.java]
        mMotorcycleViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            motorcycles -> run {
                adapter.bindBikeList(motorcycles.reversed())
                showToastAfterDelay(adapter, requireContext(), R.string.add_first_bike)
            }
        })

        view.findViewById<FloatingActionButton>(R.id.fab_addMotorcycle).setOnClickListener {
            val action = MotorcycleListFragmentDirections.bikelistToBikeadd(null)
            findNavController().navigate(action)
        }
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.distance_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.distance_menu){
            val inflater = this.layoutInflater
            val dialogView: View = inflater.inflate(R.layout.distance_dialog, null)

            var totalDistance = 0
            val bikesList = mMotorcycleViewModel.readAllData.value!!
            for (bike in bikesList) totalDistance += bike.personal_km

            dialogView.findViewById<TextView>(R.id.distance).text = String.format("%s %s", formatThousand(totalDistance), UnitHelper.getDistance())

            MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .show()
        }
        return super.onContextItemSelected(item)
    }
}