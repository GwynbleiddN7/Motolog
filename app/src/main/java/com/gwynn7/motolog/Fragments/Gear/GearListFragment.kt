package com.gwynn7.motolog.Fragments.Gear

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
import com.gwynn7.motolog.ViewModel.GearViewModel
import com.gwynn7.motolog.showToast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gwynn7.motolog.UnitHelper

class GearListFragment : Fragment() {
    private lateinit var mGearViewModel: GearViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.gear_list, container, false)

        val adapter = GearListAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.rw_gears)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mGearViewModel = ViewModelProvider(this)[GearViewModel::class.java]
        mGearViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            gears -> run {
                if(gears.isEmpty()) showToast(requireContext(), getString(R.string.add_gear))
                adapter.bindGearList(gears)
            }
        })

        view.findViewById<FloatingActionButton>(R.id.fab_addGear).setOnClickListener{
            val action = GearListFragmentDirections.gearlistToGearadd(null)
            findNavController().navigate(action)
        }
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.money_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.money_menu){
            var totalMoney = 0.0
            val gearList = mGearViewModel.readAllData.value!!
            for (gear in gearList) totalMoney += gear.price
            showToast(requireContext(), "${getString(R.string.total_spent)}: ${String.format("%.2f%s", totalMoney, UnitHelper.getCurrency())}")
        }
        return super.onContextItemSelected(item)
    }
}