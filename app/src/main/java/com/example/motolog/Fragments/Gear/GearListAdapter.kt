package com.example.motolog.Fragments.Gear

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.motolog.Models.Gear
import com.example.motolog.R
import com.example.motolog.longToDateString

class GearListAdapter : RecyclerView.Adapter<GearListAdapter.MyViewHolder>() {
    private var gearList = emptyList<Gear>()
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.gear_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = gearList[position];

        val gearModel = holder.itemView.findViewById<TextView>(R.id.tw_gear_model)
        gearModel.isSelected = true
        gearModel.text = currentItem.model

        holder.itemView.findViewById<TextView>(R.id.tw_gear_manufacturer).text = currentItem.manufacturer
        holder.itemView.findViewById<TextView>(R.id.tw_gear_date).text = String.format("Date: %S", longToDateString(currentItem.date))

        val price = String.format("Price: %.2f€", currentItem.price)
        holder.itemView.findViewById<TextView>(R.id.tw_gear_price).text = price

        val item = holder.itemView.findViewById<ConstraintLayout>(R.id.gear_row)

        item.setOnClickListener {
            val action = GearListFragmentDirections.gearlistToGearshow(currentItem)
            holder.itemView.findNavController().navigate(action)
        }

        item.setOnLongClickListener {
            val action = GearListFragmentDirections.gearlistToGearadd(currentItem)
            holder.itemView.findNavController().navigate(action)
            true
        }
    }

    override fun getItemCount(): Int {
        return gearList.size
    }

    fun bindGearList(gears: List<Gear>)
    {
        gearList = gears;
        notifyDataSetChanged()
    }

}