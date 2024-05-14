package com.gwynn7.motolog.Fragments.Gear

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gwynn7.motolog.Models.Gear
import com.gwynn7.motolog.R
import com.gwynn7.motolog.UnitHelper
import com.gwynn7.motolog.longToDateString

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
        holder.itemView.findViewById<TextView>(R.id.tw_gear_date).text = String.format("${holder.itemView.resources.getString(R.string.date)}: %s", longToDateString(currentItem.date))

        val price = String.format("${holder.itemView.resources.getString(R.string.price)}: %.2f%s", currentItem.price, UnitHelper.getCurrency())
        holder.itemView.findViewById<TextView>(R.id.tw_gear_price).text = price

        val gearImage = holder.itemView.findViewById<ImageView>(R.id.gear_image)
        if(currentItem.image != null) gearImage.setImageURI(currentItem.image)
        else gearImage.setImageResource(R.drawable.helmet_list)

        val item = holder.itemView.findViewById<CardView>(R.id.cv_gear_row)

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