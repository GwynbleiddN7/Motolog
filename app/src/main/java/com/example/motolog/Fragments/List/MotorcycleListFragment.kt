package com.example.motolog.Fragments.List

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motolog.R
import com.example.motolog.ViewModel.MotorcycleViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
            motorcycles -> adapter.setData(motorcycles)
        })

        view.findViewById<FloatingActionButton>(R.id.fab_addMotorcycle).setOnClickListener {
            findNavController().navigate(R.id.bikelist_to_bikeadd)
        }
        return view
    }
}