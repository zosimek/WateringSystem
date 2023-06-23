package com.example.dbtry

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
    private lateinit var imgInfo : ImageView


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
        imgInfo = findViewById<ImageView>(R.id.imgInfo)

        imgInfo.setOnClickListener {
            val intent =
                Intent(this@WateringSettingsActivity, ModeInfoActivity::class.java).also {
                    it
                }
            startActivity(intent)
        }

        val defaultTextColor = inputTxtPlantName.currentTextColor

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed
            }

            override fun afterTextChanged(s: Editable?) {
                val numericRegex = Regex("[0-9]+")

                if (inputTxtMoistureThreshold.text.toString().matches(numericRegex) || inputTxtMoistureThreshold.text.toString().isBlank()){
                    inputTxtMoistureThreshold.setTextColor(defaultTextColor)
                }else{
                    inputTxtMoistureThreshold.setTextColor(Color.RED)
                    Toast.makeText(applicationContext, "Please enter a numeric value for Moisture threshold", Toast.LENGTH_SHORT).show()
                }

                if (inputTxtIntervalMin.text.toString().matches(numericRegex) || inputTxtIntervalMin.text.toString().isBlank()){
                    inputTxtIntervalMin.setTextColor(defaultTextColor)
                }else{
                    inputTxtIntervalMin.setTextColor(Color.RED)
                    Toast.makeText(applicationContext, "Please enter a numeric value for interval min.", Toast.LENGTH_SHORT).show()
                }

                if (inputTxtIntervalMax.text.toString().matches(numericRegex) || inputTxtIntervalMax.text.toString().isBlank()){
                    inputTxtIntervalMax.setTextColor(defaultTextColor)
                }else{
                    inputTxtIntervalMax.setTextColor(Color.RED)
                    Toast.makeText(applicationContext, "Please enter a numeric value for interval max.", Toast.LENGTH_SHORT).show()
                }

                if (inputTxtWaterAmount.text.toString().matches(numericRegex) || inputTxtWaterAmount.text.toString().isBlank()){
                    inputTxtWaterAmount.setTextColor(defaultTextColor)
                }else{
                    inputTxtWaterAmount.setTextColor(Color.RED)
                    Toast.makeText(applicationContext, "Please enter a numeric value for interval max.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        inputTxtMoistureThreshold.addTextChangedListener(textWatcher)
        inputTxtWaterAmount.addTextChangedListener(textWatcher)
        inputTxtIntervalMin.addTextChangedListener(textWatcher)
        inputTxtIntervalMax.addTextChangedListener(textWatcher)


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

            val numericRegex = Regex("[0-9]+")

            var name = inputTxtPlantName.text.toString()
            var mode = spinnerMode.selectedItem.toString()
            var moistureThreshold = 0
            var intervalMin = 0
            var intervalMax = 0
            var waterAmount = 0

            if (inputTxtMoistureThreshold.text.toString().matches(numericRegex) || inputTxtMoistureThreshold.text.toString().isBlank()){
                moistureThreshold = inputTxtMoistureThreshold.text.toString().toIntOrNull() ?: 0
            }else{
                inputTxtMoistureThreshold.setTextColor(Color.RED)
            }

            if (inputTxtIntervalMin.text.toString().matches(numericRegex) || inputTxtIntervalMin.text.toString().isBlank()){
                intervalMin = inputTxtIntervalMin.text.toString().toIntOrNull() ?: 0
            }else{
                inputTxtIntervalMin.setTextColor(Color.RED)
            }

            if (inputTxtIntervalMax.text.toString().matches(numericRegex) || inputTxtIntervalMax.text.toString().isBlank()){
                intervalMax = inputTxtIntervalMax.text.toString().toIntOrNull() ?: 0
            }else{
                inputTxtIntervalMax.setTextColor(Color.RED)
            }

            if (inputTxtWaterAmount.text.toString().matches(numericRegex) || inputTxtWaterAmount.text.toString().isBlank()){
                waterAmount = inputTxtWaterAmount.text.toString().toIntOrNull() ?: 0
            }else{
                inputTxtWaterAmount.setTextColor(Color.RED)
            }


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