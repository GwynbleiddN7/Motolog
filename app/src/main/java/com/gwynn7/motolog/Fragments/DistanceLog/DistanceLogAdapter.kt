package com.gwynn7.motolog.Fragments.DistanceLog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gwynn7.motolog.Models.DistanceLog
import com.gwynn7.motolog.Models.Motorcycle
import com.gwynn7.motolog.R
import com.gwynn7.motolog.UnitHelper
import com.gwynn7.motolog.formatThousand
import com.gwynn7.motolog.longToDateString

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

        val distanceTextView = holder.itemView.findViewById<TextView>(R.id.tw_distance_log)
        distanceTextView.isSelected = true
        distanceTextView.text = String.format("%s %s", formatThousand(currentItem.distance), UnitHelper.getDistance())

        holder.itemView.findViewById<TextView>(R.id.tw_distance_date).text = longToDateString(currentItem.date)

        val deltaDistance = if(position < distanceLogList.size-1) currentItem.distance - distanceLogList[position+1].distance else currentItem.distance - currentBike.start_km
        holder.itemView.findViewById<TextView>(R.id.tw_distance_difference).text = String.format("+%s %s", formatThousand(deltaDistance), UnitHelper.getDistance())

        holder.itemView.findViewById<CardView>(R.id.cv_distance_row).setOnClickListener {
            val action = DistanceLogFragmentDirections.distancelogToDistanceadd(currentBike, position)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun bindBike(bike: Motorcycle)
    {
        distanceLogList = bike.logs.distance
        currentBike = bike
        notifyDataSetChanged()
    }
}