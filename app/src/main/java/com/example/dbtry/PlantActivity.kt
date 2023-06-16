package com.example.dbtry

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
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
                    for (plantSnapshot in snapshot.children){
                        val plant = plantSnapshot.getValue(Plant::class.java)
                        plantArrayList.add(plant!!)
                    }

                    plantRecyclerView.adapter = PlantAdapter(plantArrayList)
                    plantListLoadingIcon.visibility = View.GONE

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



//        val textView1 : TextView = findViewById(R.id.txttitle) as TextView
//
//        var database = FirebaseDatabase.getInstance().getReference("Users")
//        var getdata = object : ValueEventListener {
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                var sb = StringBuilder()
//                for(i in snapshot.children) {
//                        var name = i.child("firstName").getValue()
//                        sb.append("$name")
//                }
//                textView1.setText(sb)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        }
//        database.addValueEventListener(getdata)
//        database.addListenerForSingleValueEvent(getdata)


    }
}