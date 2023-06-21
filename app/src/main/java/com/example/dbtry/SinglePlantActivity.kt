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
import java.util.*
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
            var plantRecord = "plant" + plantNumber
            val intent =
                Intent(this@SinglePlantActivity, WateringSettingsActivity::class.java).also {
                    it.putExtra("PLANT_NAME", plantRecord)
                }
            startActivity(intent)
        }


        btnWateringHistory = findViewById<Button>(R.id.btnWateringHistory)
        btnWateringHistory.setOnClickListener {
            var plantRecord = "plant" + plantNumber
            val intent =
                Intent(this@SinglePlantActivity, WateringHistoryActivity::class.java).also {
                    it.putExtra("PLANT_NAME", plantRecord)
                }
            startActivity(intent)
        }


        btnMoistureHistory = findViewById<Button>(R.id.btnMoistureHistory)
        btnMoistureHistory.setOnClickListener {
            var plantRecord = "plant" + plantNumber
            val intent =
                Intent(this@SinglePlantActivity, MoistureHistoryActivity::class.java).also {
                    it.putExtra("PLANT_NAME", plantRecord)
                }
            startActivity(intent)
        }

        readData(plantNumber.toString())

////////////////////////////////////////////////////////////////////////////////////////////////////
//  updating the donut char each secund with the new upcoming moisture level
////////////////////////////////////////////////////////////////////////////////////////////////////

        val timer = Timer()

        val task = object : TimerTask() {
            override fun run() {
                runOnUiThread{
                    readMoistureLevel(plantNumber.toString())
                }
            }
        }

        val delay = 0L // 1 second
        val period = 2000L // 2 seconds

        timer.scheduleAtFixedRate(task, delay, period)
    }
////////////////////////////////////////////////////////////////////////////////////////////////////



    private fun readData(plantNumber: String) {
        db = FirebaseDatabase.getInstance().getReference("10032311")

        var plantNumber = ("plant" + plantNumber)
        db.child(plantNumber).get().addOnSuccessListener {

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

//                Toast.makeText(this, "${currentMoistureLevel}", Toast.LENGTH_SHORT).show()
                moistureText.text = currentMoistureLevel.toString()
                moistureLevel.progress = Integer.parseInt(currentMoistureLevel.toString())
            }
        }
    }
}