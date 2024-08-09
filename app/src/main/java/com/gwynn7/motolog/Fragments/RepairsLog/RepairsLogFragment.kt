package com.gwynn7.motolog.Fragments.RepairsLog

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gwynn7.motolog.Models.Motorcycle
import com.gwynn7.motolog.R
import com.gwynn7.motolog.ViewModel.MotorcycleViewModel
import com.gwynn7.motolog.showToastAfterDelay
import com.gwynn7.motolog.stop


class RepairsLogFragment : Fragment() {
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel
    private lateinit var currentbike: Motorcycle
    private lateinit var adapter: RepairsLogAdapter
    var filterIndex = 0;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.repairslog_list, container, false)

        adapter = RepairsLogAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.rw_repairs)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mMotorcycleViewModel = ViewModelProvider(this)[MotorcycleViewModel::class.java]

        val bikeId = MotorcycleViewModel.currentBikeId
        if(bikeId == null)
        {
            stop(activity)
            return view
        }

        val bikeData = mMotorcycleViewModel.getMotorcycle(bikeId)
        bikeData.observe(viewLifecycleOwner, Observer {
            bikes -> run {
            if(bikes.isNotEmpty()) {
                currentbike = bikes.first()
                adapter.bindBike(currentbike)

                showToastAfterDelay(adapter, requireContext(), R.string.add_first_repair)
            }
            else stop(activity)
        }
        })
        var arrayType = 0;
        view.findViewById<FloatingActionButton>(R.id.fab_addRepairsLog).setOnClickListener{
            val alert = MaterialAlertDialogBuilder(requireContext())
                .setNegativeButton(R.string.back, null)
                .setSingleChoiceItems(resources.getStringArray(R.array.repair_types), 0) { _, which ->
                    arrayType = which;
                }
                .setTitle(R.string.choose_repair_type)
                .setPositiveButton(R.string.add_log){ _, _ ->
                    val action = RepairsLogFragmentDirections.repairslistToRepairsadd(currentbike, arrayType)
                    findNavController().navigate(action)
                }
                .show()
        }
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.filter_menu){
            val filterList = resources.getStringArray(R.array.repair_types).toMutableList()
            filterList.add(0, resources.getString(R.string.all))
            val alert = MaterialAlertDialogBuilder(requireContext())
                .setSingleChoiceItems(filterList.toTypedArray(), filterIndex) { _, which ->
                    filterIndex = which;
                }
                .setTitle(R.string.filter_type)
                .setNegativeButton(R.string.from_old){ _, _ ->
                    adapter.filter(filterIndex-1, true)
                }
                .setPositiveButton(R.string.from_new){ _, _ ->
                    adapter.filter(filterIndex-1, false)
                }
                .show()
        }
        return super.onContextItemSelected(item)
    }
}