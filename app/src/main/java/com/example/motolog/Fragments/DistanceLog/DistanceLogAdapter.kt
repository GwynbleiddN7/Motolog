package com.example.motolog.Fragments.DistanceLog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.motolog.Models.DistanceLog
import com.example.motolog.Models.Motorcycle
import com.example.motolog.R
import com.example.motolog.formatThousand
import com.example.motolog.longToDateString

class DistanceLogAdapter : RecyclerView.Adapter<DistanceLogAdapter.MyViewHolder>() {
    private var distanceLogList = emptyList<DistanceLog>()
    private lateinit var currentBike: Motorcycle
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.distancelog_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return distanceLogList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = distanceLogList[position]

        holder.itemView.findViewById<TextView>(R.id.tw_distance_log).text = String.format("%S km", formatThousand(currentItem.distance))
        holder.itemView.findViewById<TextView>(R.id.tw_distance_date).text = longToDateString(currentItem.date)

        val deltaDistance = if(position < distanceLogList.size-1) currentItem.distance - distanceLogList[position+1].distance else currentItem.distance - currentBike.start_km
        holder.itemView.findViewById<TextView>(R.id.tw_distance_difference).text = String.format("+%S km", formatThousand(deltaDistance))

        holder.itemView.findViewById<ConstraintLayout>(R.id.distancelog_row).setOnClickListener {
            val action = DistanceLogFragmentDirections.distancelogToDistanceadd(currentBike, position)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun bindBike(bike: Motorcycle)
    {
        distanceLogList = bike.km_logs
        currentBike = bike
        notifyDataSetChanged()
    }
}