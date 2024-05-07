package com.gwynn7.motolog.Fragments.Garage

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getString
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gwynn7.motolog.BikeActivity
import com.gwynn7.motolog.Models.Motorcycle
import com.gwynn7.motolog.R
import com.gwynn7.motolog.formatThousand

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
        holder.itemView.findViewById<TextView>(R.id.tw_bike_year).text = String.format("${getString(holder.itemView.context, R.string.year)}: %d", currentItem.year)

        val distance = String.format("${holder.itemView.context.getString(R.string.distance)}: %S km", formatThousand(currentItem.personal_km))
        holder.itemView.findViewById<TextView>(R.id.tw_bike_distance).text = distance

        val motorcycleRow = holder.itemView.findViewById<CardView>(R.id.cv_bike_row)

        val bikeImage = holder.itemView.findViewById<ImageView>(R.id.motorcycle_image)
        if(currentItem.image != null) bikeImage.setImageURI(currentItem.image)
        else bikeImage.setImageResource(R.drawable.bike)

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

    fun bindBikeList(motorcycles: List<Motorcycle>)
    {
        motorcycleList = motorcycles;
        notifyDataSetChanged()
    }
}