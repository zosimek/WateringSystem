package com.example.dbtry

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.properties.Delegates

class WateringSettingsActivity : AppCompatActivity() {

    private lateinit var db: DatabaseReference
    private lateinit var modeDB : String

    private lateinit var spinnerMode : Spinner
    private lateinit var inputTxtPlantName: TextInputEditText
    private lateinit var inputTxtMoistureThreshold: TextInputEditText
    private lateinit var inputTxtWaterAmount: TextInputEditText
    private lateinit var inputTxtIntervalMin: TextInputEditText
    private lateinit var inputTxtIntervalMax: TextInputEditText
    private lateinit var btnSubmit : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watering_settings)

        var plantRecord = intent.getStringExtra("PLANT_NAME")
//        Toast.makeText(this@WateringSettingsActivity, "Name: $plantRecord", Toast.LENGTH_SHORT).show()

        inputTxtPlantName = findViewById<TextInputEditText>(R.id.txtPlantName)
        inputTxtMoistureThreshold = findViewById<TextInputEditText>(R.id.txtThreshold)
        inputTxtWaterAmount = findViewById<TextInputEditText>(R.id.txtWaterAmount)
        inputTxtIntervalMin = findViewById<TextInputEditText>(R.id.txtMinInterval)
        inputTxtIntervalMax = findViewById<TextInputEditText>(R.id.txtMaxInterval)
        btnSubmit = findViewById<Button>(R.id.btnCommitSettings)

        readDataNameMode(plantRecord.toString())
        readThresholdMoisture(plantRecord.toString())
        readIntervalsWAmount(plantRecord.toString())


        spinnerMode = findViewById<Spinner>(R.id.spinnerMode)
        var wateringModeArray = resources.getStringArray(R.array.watering_mode)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, wateringModeArray)
        adapter.setDropDownViewResource(R.layout.watering_settings_spinner_item)
        spinnerMode.adapter = adapter

        spinnerMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedMode = spinnerMode.getItemAtPosition(p2).toString()
//                Toast.makeText(this@WateringSettingsActivity, "Selected $selectedMode", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
//                Toast.makeText(this@WateringSettingsActivity, "Not selected", Toast.LENGTH_SHORT).show()
            }
        }


        btnSubmit.setOnClickListener {
            var name = inputTxtPlantName.text.toString()
            var mode = spinnerMode.selectedItem.toString()
            var moistureThreshold = inputTxtMoistureThreshold.text.toString().toIntOrNull() ?: 0
            var intervalMin = inputTxtIntervalMin.text.toString().toIntOrNull() ?: 0
            var intervalMax = inputTxtIntervalMax.text.toString().toIntOrNull() ?: 0
            var waterAmount = inputTxtWaterAmount.text.toString().toIntOrNull() ?: 0


            db = FirebaseDatabase.getInstance().getReference(("10032311/" + plantRecord).toString())

            val updates = HashMap<String, Any>()
            if (name.isNotEmpty() && name != null) {
                db.child("name").setValue(name)
            }
            if (mode != modeDB) {
                db.child("mode").setValue(mode)
            }
            if (moistureThreshold !=0 && moistureThreshold != null) {
                db.child("sensor").child("moistureThreshold").setValue(moistureThreshold)
            }
            if (intervalMin !=0 && intervalMin != null) {
                db.child("pump").child("minInterval").setValue(intervalMin)
            }
            if (intervalMax !=0 && intervalMax != null) {
                db.child("pump").child("maxInterval").setValue(intervalMax)
            }
            if (waterAmount !=0 && waterAmount != null) {
                db.child("pump").child("waterAmount").setValue(waterAmount)
            }

            // Update the plant data in the database
            db.updateChildren(updates)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Data updated successfully", Toast.LENGTH_SHORT).show()
                    recreate()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(applicationContext, "Failed to update data: ${e.message}", Toast.LENGTH_SHORT).show()
                }

            recreate()
        }

    }

    private fun readDataNameMode(plantRecord: String){
        db = FirebaseDatabase.getInstance().getReference("10032311")
//        Toast.makeText(this@WateringSettingsActivity, "Name: $plantRecord", Toast.LENGTH_SHORT).show()

        db.child(plantRecord).get().addOnSuccessListener{

            if (it.exists()){

                val name = it.child("name").value.toString()
                inputTxtPlantName.setHint(name)

                modeDB = it.child("mode").value.toString()
                val defaultSelection = when (modeDB) {
                    "manual" -> 0
                    "timer" -> 1
                    "auto" -> 2
                    else -> 0
                }
                spinnerMode.setSelection(defaultSelection)
//                Toast.makeText(this@WateringSettingsActivity, "Plant no. ${spinnerMode.selectedItem}", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun readThresholdMoisture(plantRecord: String) {
        db = FirebaseDatabase.getInstance().getReference("10032311")
        var sensorRecord = plantRecord + "/sensor"

        db.child(sensorRecord).get().addOnSuccessListener{

            if (it.exists()){

                val threshold = it.child("moistureThreshold").value.toString()
                inputTxtMoistureThreshold.setHint(threshold)

            }
        }
    }

    private fun readIntervalsWAmount(plantRecord: String) {
        db = FirebaseDatabase.getInstance().getReference("10032311")
        var sensorRecord = plantRecord + "/pump"

        db.child(sensorRecord).get().addOnSuccessListener{

            if (it.exists()){

                val minInterval = it.child("minInterval").value.toString()
                val maxInterval = it.child("maxInterval").value.toString()
                val waterAmount = it.child("waterAmount").value.toString()
                inputTxtIntervalMin.setHint(minInterval)
                inputTxtIntervalMax.setHint(maxInterval)
                inputTxtWaterAmount.setHint(waterAmount)

            }
        }
    }

}