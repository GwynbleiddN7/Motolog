package com.example.motolog.Fragments.DistanceLog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.motolog.Fragments.Gear.GearListFragmentDirections
import com.example.motolog.Models.DistanceLog
import com.example.motolog.Models.Motorcycle
import com.example.motolog.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

        holder.itemView.findViewById<TextView>(R.id.tw_distance_log).text = String.format("%d km", currentItem.distance)

        val simpleDateFormat by lazy { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
        val dateFormatted = simpleDateFormat.format(Date(currentItem.date))
        holder.itemView.findViewById<TextView>(R.id.tw_distance_date).text = dateFormatted

        val deltaDistance = if(position+1 < distanceLogList.size) currentItem.distance - distanceLogList[position+1].distance else currentItem.distance - currentBike.start_km
        holder.itemView.findViewById<TextView>(R.id.tw_distance_difference).text = String.format("+%d km", deltaDistance)

        val item = holder.itemView.findViewById<ConstraintLayout>(R.id.distancelog_row)

        item.setOnClickListener {
            val action = DistanceLogFragmentDirections.distancelogToDistanceadd(position, currentBike)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(bike: Motorcycle)
    {
        this.distanceLogList = bike.km_logs
        this.currentBike = bike
        notifyDataSetChanged()
    }
}