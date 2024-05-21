package com.gwynn7.motolog.Fragments.Garage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gwynn7.motolog.R
import com.gwynn7.motolog.ViewModel.MotorcycleViewModel
import com.gwynn7.motolog.formatThousand
import com.gwynn7.motolog.showToast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gwynn7.motolog.UnitHelper
import com.gwynn7.motolog.showToastAfterDelay

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
            var totalDistance = 0
            val bikesList = mMotorcycleViewModel.readAllData.value!!
            for (bike in bikesList) totalDistance += bike.personal_km
            showToast(requireContext(), "${getString(R.string.total_distance)}: ${String.format("%s %s", formatThousand(totalDistance), UnitHelper.getDistance())}")
        }
        return super.onContextItemSelected(item)
    }
}