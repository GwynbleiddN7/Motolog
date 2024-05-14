package com.gwynn7.motolog.Fragments.ModsLog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gwynn7.motolog.Models.Motorcycle
import com.gwynn7.motolog.R
import com.gwynn7.motolog.ViewModel.MotorcycleViewModel
import com.gwynn7.motolog.showToast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gwynn7.motolog.UnitHelper
import com.gwynn7.motolog.stop

class ModsLogFragment : Fragment() {
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel
    private lateinit var currentbike: Motorcycle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modslog_list, container, false)

        val adapter = ModsLogAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.rw_modslogs)
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
            }
            else stop(activity)
        }
        })

        view.findViewById<FloatingActionButton>(R.id.fab_addModsLog).setOnClickListener{
            val action = ModsLogFragmentDirections.modslogToModsadd(currentbike)
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
            val modsList = currentbike.logs.mods
            for (mod in modsList) totalMoney += mod.price
            showToast(requireContext(), "${getString(R.string.total_spent)}: ${String.format("%.2f%s", totalMoney, UnitHelper.getCurrency())}")
        }
        return super.onContextItemSelected(item)
    }
}