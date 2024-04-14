package com.example.motolog.Fragments.Garage

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.motolog.BikeActivity
import com.example.motolog.Models.Motorcycle
import com.example.motolog.R

class MotorcycleListAdapter: RecyclerView.Adapter<MotorcycleListAdapter.MyViewHolder>() {
    private var motorcycleList = emptyList<Motorcycle>()
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.motorcycle_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = motorcycleList[position];
        holder.itemView.findViewById<TextView>(R.id.tw_bike_manufacturer).text = currentItem.manufacturer

        val bikeModel = holder.itemView.findViewById<TextView>(R.id.tw_bike_model)
        bikeModel.isSelected = true
        bikeModel.text = currentItem.model

        holder.itemView.findViewById<TextView>(R.id.tw_bike_alias).text = currentItem.alias
        holder.itemView.findViewById<TextView>(R.id.tw_bike_year).text = String.format("Year: %d", currentItem.year)

        val distance = String.format("Distance: %d km", currentItem.personal_km)
        holder.itemView.findViewById<TextView>(R.id.tw_bike_distance).text = distance

        val motorcycleRow = holder.itemView.findViewById<ConstraintLayout>(R.id.motorcycle_row)

        motorcycleRow.setOnClickListener {
            val bikeActivity = Intent(holder.itemView.context, BikeActivity::class.java)
            bikeActivity.putExtra("bike_id", currentItem.id)
            holder.itemView.context.startActivity(bikeActivity)
        }

        motorcycleRow.setOnLongClickListener {
            val action = MotorcycleListFragmentDirections.bikelistToBikeadd(currentItem)
            holder.itemView.findNavController().navigate(action)
            true
        }
    }

    override fun getItemCount(): Int {
        return motorcycleList.size;
    }

    fun setData(motorcycles: List<Motorcycle>)
    {
        this.motorcycleList = motorcycles;
        notifyDataSetChanged()
    }
}