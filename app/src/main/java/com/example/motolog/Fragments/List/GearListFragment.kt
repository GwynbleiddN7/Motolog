package com.example.motolog.Fragments.List

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motolog.R
import com.example.motolog.ViewModel.GearViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
            gears -> adapter.setData(gears)
        })

        view.findViewById<FloatingActionButton>(R.id.fab_addGear).setOnClickListener{
            findNavController().navigate(R.id.gearlist_to_gearadd)
        }
        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.money_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.money_menu){
            var totalMoney: Double = 0.0
            for (gear in mGearViewModel.readAllData.value!!)
            {
                totalMoney += gear.price
            }
            Toast.makeText(requireContext(), "Total spent on gear: ${String.format("%.2f€", totalMoney)}", Toast.LENGTH_LONG).show()
        }
        return super.onContextItemSelected(item)
    }
}