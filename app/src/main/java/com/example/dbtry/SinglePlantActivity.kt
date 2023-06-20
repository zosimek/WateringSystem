package com.example.dbtry

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.concurrent.fixedRateTimer

class SinglePlantActivity : AppCompatActivity() {

    private lateinit var db: DatabaseReference
    private lateinit var plantName: TextView
    private lateinit var moistureLevel: ProgressBar
    private lateinit var moistureText: TextView

    private lateinit var btnWateringSettings: Button
    private lateinit var btnWateringHistory: Button
    private lateinit var btnMoistureHistory: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_main)

        var plantNumber = intent.getStringExtra("PLANT_NUMBER")?.toLongOrNull()

        plantName = findViewById<TextView>(R.id.txtPlantName)
        moistureText = findViewById<TextView>(R.id.txtMoisture)
        moistureLevel = findViewById<ProgressBar>(R.id.progressMoisture)



        btnWateringSettings = findViewById<Button>(R.id.btnWateringSettings)
        btnWateringSettings.setOnClickListener {
            val intent = Intent(this@SinglePlantActivity, WateringSettingsActivity::class.java)
            startActivity(intent)
        }


        btnWateringHistory = findViewById<Button>(R.id.btnWateringHistory)
        btnWateringHistory.setOnClickListener {
            val intent = Intent(this@SinglePlantActivity, WateringHistoryActivity::class.java)
            startActivity(intent)
        }


        btnMoistureHistory = findViewById<Button>(R.id.btnMoistureHistory)
        btnMoistureHistory.setOnClickListener {
            val intent = Intent(this@SinglePlantActivity, MoistureHistoryActivity::class.java)
            startActivity(intent)
        }


        readData(plantNumber.toString())

        fixedRateTimer("timer", false,0L, 1000){
            this@SinglePlantActivity.runOnUiThread {
                readMoistureLevel(plantNumber.toString())
            }
        }
    }



    private fun readData(plantNumber: String) {

        db = FirebaseDatabase.getInstance().getReference("10032311")
        var plantRecord = "plant" + plantNumber

        db.child(plantRecord).get().addOnSuccessListener {

            if (it.exists()) {

                val name = it.child("name").value.toString()

                plantName.text = name

            } else {

//                Toast.makeText(this, "Wrong data", Toast.LENGTH_SHORT).show()

            }

        }.addOnFailureListener {
//            Toast.makeText(this, "Wrong data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readMoistureLevel(plantNumber: String){

        db = FirebaseDatabase.getInstance().getReference("10032311")

        var sensorRecord = "plant" + plantNumber + "/sensor"
        db.child(sensorRecord).get().addOnSuccessListener {

            if(it.exists()){

                val currentMoistureLevel = it.child("currentMoistureLevel").value

//                Toast.makeText(this, "${currentMoistureLevel}, ${currentMoistureLevel?.javaClass}", Toast.LENGTH_SHORT).show()
                moistureText.text = currentMoistureLevel.toString()
                moistureLevel.progress = Integer.parseInt(currentMoistureLevel.toString())
            }
        }
    }
}