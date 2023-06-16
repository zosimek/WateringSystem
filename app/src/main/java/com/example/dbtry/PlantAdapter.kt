package com.example.dbtry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }

    override fun getItemCount(): Int {
        return plantList.size
    }

    class PlantViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val name : TextView = itemView.findViewById(R.id.txtPlantName)

        init {

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

        }
    }
}
