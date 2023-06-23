package com.example.dbtry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class PlantActivity: AppCompatActivity() {

    private lateinit var plantListLoadingIcon: ProgressBar
    private lateinit var plantRecyclerView: RecyclerView
    private lateinit var plantArrayList: ArrayList<Plant>
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_list)

        plantListLoadingIcon = findViewById(R.id.plantListLoading)
        plantRecyclerView = findViewById(R.id.plantList)
        plantRecyclerView.layoutManager = LinearLayoutManager(this)
        plantRecyclerView.setHasFixedSize(true)

        plantArrayList = arrayListOf<Plant>()

        getPlantData()


    }
    private fun getPlantData() {

        db = FirebaseDatabase.getInstance().getReference("10032311")
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    plantArrayList.clear()
                    for (plantSnapshot in snapshot.children){
                        val plant = plantSnapshot.getValue(Plant::class.java)
                        plantArrayList.add(plant!!)
                    }

                    var adapter = PlantAdapter(plantArrayList)

                    plantRecyclerView.adapter = adapter
                    plantListLoadingIcon.visibility = View.GONE
                    adapter.setOnItemClickListener(object : PlantAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
//                            Toast.makeText(this@PlantActivity, "Plant no. ${position+1}", Toast.LENGTH_SHORT).show()
                            singlePlantActivity(position)
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    fun singlePlantActivity(position: Int){

        val plantNumber = (position + 1).toString()

        val intent = Intent(this@PlantActivity, SinglePlantActivity::class.java).also {
            it.putExtra("PLANT_NUMBER", plantNumber)
        }
        startActivity(intent)
    }
}