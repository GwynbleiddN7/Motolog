package com.gwynn7.motolog.Fragments.ModsLog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gwynn7.motolog.showToastAfterDelay
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

                showToastAfterDelay(adapter, requireContext(), R.string.add_first_mod)
            }
            else stop(activity)
        }
        })

        view.findViewById<FloatingActionButton>(R.id.fab_addModsLog).setOnClickListener{
            val action = ModsLogFragmentDirections.modslogToModsadd(currentbike)
            findNavController().navigate(action)
        }
        return view
    }
}