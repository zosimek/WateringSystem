package com.example.dbtry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlantAdapter(private val plantList : ArrayList<Plant>) : RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)

    }

    fun setOnItemClickListener(listener: onItemClickListener){

        mListener = listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.plant_list_item, parent, false)
        return PlantViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val currentitem = plantList[position]
        holder.name.text = currentitem.name
        when (currentitem.image){
            1 -> {
                holder.imagePlant.setImageResource(R.drawable.plant_benjaminek)
            }
            2 -> {
                holder.imagePlant.setImageResource(R.drawable.plant_bonsai)
            }
            3 -> {
                holder.imagePlant.setImageResource(R.drawable.plant_kaktus)
            }
            4 -> {
                holder.imagePlant.setImageResource(R.drawable.plant_kwiatek)
            }
            5 -> {
                holder.imagePlant.setImageResource(R.drawable.plant_monstera)
            }
            6 -> {
                holder.imagePlant.setImageResource(R.drawable.plant_sokulent)
            }
            7 -> {
                holder.imagePlant.setImageResource(R.drawable.plant_storczyk)
            }
            else -> {
                holder.imagePlant.setImageResource(R.drawable.plant_benjaminek)
            }
        }
    }

    override fun getItemCount(): Int {
        return plantList.size
    }

    class PlantViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val name : TextView = itemView.findViewById(R.id.txtPlantName)
        val imagePlant : ImageView = itemView.findViewById(R.id.imagePlant)

        init {

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

        }
    }
}
