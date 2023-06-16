package com.example.dbtry

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SinglePlantActivity : AppCompatActivity() {

    private lateinit var db: DatabaseReference
    private lateinit var plantName: TextView
    private lateinit var moistureLevel: ProgressBar
    private lateinit var moistureText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_main)

        val plantNumber = intent.getStringExtra("PLANT_NUMBER")

        plantName = findViewById<TextView>(R.id.txtPlantName)
        moistureText = findViewById<TextView>(R.id.txtMoisture)
        moistureLevel = findViewById<ProgressBar>(R.id.progressMoisture)

        readData(plantNumber.toString())

    }

    private fun readData(plantNumber: String) {

        db = FirebaseDatabase.getInstance().getReference("10032311")
        var plantRecord = "plant" + plantNumber

        db.child(plantRecord).get().addOnSuccessListener {

            if (it.exists()) {

                val name = it.child("name").value.toString()

                plantName.text = name

            } else {

                Toast.makeText(this, "Wrong data", Toast.LENGTH_SHORT).show()

            }

        }.addOnFailureListener {
            Toast.makeText(this, "Wrong data", Toast.LENGTH_SHORT).show()
        }

        var sensorRecord = "plant" + plantNumber + "/sensor"
        db.child(sensorRecord).get().addOnSuccessListener {

            if(it.exists()){

                val currentMoistureLevel = it.child("currentMoistureLevel").value

                Toast.makeText(this, "${currentMoistureLevel}, ${currentMoistureLevel?.javaClass}", Toast.LENGTH_SHORT).show()
                moistureText.text = currentMoistureLevel.toString()
                moistureLevel.progress = Integer.parseInt(currentMoistureLevel.toString())
            }
        }

    }
}