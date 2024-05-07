package com.gwynn7.motolog.Fragments.ModsLog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gwynn7.motolog.Models.ModsLog
import com.gwynn7.motolog.Models.Motorcycle
import com.gwynn7.motolog.R
import com.gwynn7.motolog.longToDateString
import com.gwynn7.motolog.repairColors

class ModsLogAdapter: RecyclerView.Adapter<ModsLogAdapter.MyViewHolder>(){
    private var modsLogList = emptyList<ModsLog>()
    private lateinit var currentBike: Motorcycle
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.modslog_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return modsLogList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = modsLogList[position]

        holder.itemView.findViewById<TextView>(R.id.tw_mod_title).text = currentItem.title
        holder.itemView.findViewById<TextView>(R.id.tw_mod_description).text = currentItem.description
        holder.itemView.findViewById<TextView>(R.id.tw_mod_date).text = longToDateString(currentItem.date)

        val price = String.format("${holder.itemView.resources.getString(R.string.price)}: %.2f€", currentItem.price)
        holder.itemView.findViewById<TextView>(R.id.tw_mod_price).text = price

        holder.itemView.findViewById<ImageView>(R.id.mod_image).setColorFilter(holder.itemView.resources.getColor(
            repairColors[position % repairColors.size], null));

        holder.itemView.findViewById<CardView>(R.id.cv_mods_row).setOnClickListener {
            val action = ModsLogFragmentDirections.modslogToModsadd(currentBike, position)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun bindBike(bike: Motorcycle)
    {
        modsLogList = bike.logs.mods
        currentBike = bike
        notifyDataSetChanged()
    }
}