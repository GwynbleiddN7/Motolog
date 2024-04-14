package com.example.motolog.Fragments.DistanceLog

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
import com.example.motolog.Models.Motorcycle
import com.example.motolog.R
import com.example.motolog.ViewModel.MotorcycleViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DistanceLogFragment : Fragment() {
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel
    private lateinit var currentbike: Motorcycle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.distancelog_list, container, false)

        val adapter = DistanceLogAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.rw_distancelogs)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mMotorcycleViewModel = ViewModelProvider(this)[MotorcycleViewModel::class.java]

        val bikeId = MotorcycleViewModel.currentBikeId
        if(bikeId == null)
        {
            returnToList()
            return view
        }

        val bikeData = mMotorcycleViewModel.getMotorcycle(bikeId)
        bikeData.observe(viewLifecycleOwner, Observer {
            bikes -> run {
            if(bikes.isNotEmpty()) {
                currentbike = bikes.first()
                adapter.bindBike(currentbike)
            }
            else returnToList()
        }
        })

        view.findViewById<FloatingActionButton>(R.id.fab_addDistanceLog).setOnClickListener{
            val action = DistanceLogFragmentDirections.distancelogToDistanceadd(-1, currentbike)
            findNavController().navigate(action)
        }

        return view
    }

    private fun returnToList()
    {
        activity?.finish()
    }
}