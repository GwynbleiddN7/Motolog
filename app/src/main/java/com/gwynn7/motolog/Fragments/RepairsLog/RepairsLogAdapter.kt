package com.gwynn7.motolog.Fragments.RepairsLog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gwynn7.motolog.Models.Motorcycle
import com.gwynn7.motolog.Models.RepairsLog
import com.gwynn7.motolog.R
import com.gwynn7.motolog.UnitHelper
import com.gwynn7.motolog.formatThousand
import com.gwynn7.motolog.longToDateString
import com.gwynn7.motolog.repairColors

class RepairsLogAdapter: RecyclerView.Adapter<RepairsLogAdapter.MyViewHolder>() {
    private var repairsLogList = emptyList<RepairsLog>()
    private lateinit var currentBike: Motorcycle
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.repairslog_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return repairsLogList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = repairsLogList[position]

        holder.itemView.findViewById<TextView>(R.id.tw_repair_type).text = currentItem.typeText
        val notes = holder.itemView.findViewById<TextView>(R.id.tw_repair_notes)
        notes.text = currentItem.notes
        notes.isSelected = true

        holder.itemView.findViewById<TextView>(R.id.tw_repair_date).text = longToDateString(currentItem.date)

        holder.itemView.findViewById<ImageView>(R.id.repair_image).setColorFilter(holder.itemView.resources.getColor(
            repairColors[currentItem.typeIndex], null));

        val price = String.format("${holder.itemView.resources.getString(R.string.price)}: %.2f%s", currentItem.price, UnitHelper.getCurrency())
        holder.itemView.findViewById<TextView>(R.id.tw_repair_price).text = price

        val distance = String.format("%s %s", formatThousand(currentItem.repair_km), UnitHelper.getDistance())
        holder.itemView.findViewById<TextView>(R.id.tw_repair_distance).text = distance

        holder.itemView.findViewById<CardView>(R.id.cv_repairs_row).setOnClickListener {
            var arrayType = currentItem.typeIndex;
            val alert = MaterialAlertDialogBuilder(holder.itemView.context)
                .setNegativeButton(R.string.back, null)
                .setSingleChoiceItems(holder.itemView.resources.getStringArray(R.array.repair_types), currentItem.typeIndex) { _, which ->
                    arrayType = which;
                }
                .setTitle(R.string.choose_repair_type)
                .setPositiveButton(R.string.edit){ _, _ ->
                    val action = RepairsLogFragmentDirections.repairslistToRepairsadd(currentBike, arrayType, position)
                    holder.itemView.findNavController().navigate(action)
                }
                .show()
        }
    }

    fun filter(repairTypeIndex: Int, reverse: Boolean) {
        repairsLogList = currentBike.logs.maintenance.filter { element -> element.typeIndex == repairTypeIndex || repairTypeIndex == -1 }
        repairsLogList.sortedByDescending { element -> element.date }
        if(reverse) repairsLogList = repairsLogList.reversed()
        notifyDataSetChanged()
    }

    fun bindBike(bike: Motorcycle)
    {
        repairsLogList = bike.logs.maintenance
        currentBike = bike
        notifyDataSetChanged()
    }
}