package com.example.dbtry

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class WateringSettingsActivity : AppCompatActivity() {

    private lateinit var spinnerMode : Spinner
    private lateinit var inputTxtPlantName: TextInputEditText
    private lateinit var inputTxtMoistureThreshold: TextInputEditText
    private lateinit var inputTxtWaterAmount: TextInputEditText
    private lateinit var inputTxtintervalMin: TextInputEditText
    private lateinit var inputTxtIntervalMax: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watering_settings)

        spinnerMode = findViewById<Spinner>(R.id.spinnerMode)
        var wateringModeArray = resources.getStringArray(R.array.watering_mode)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, wateringModeArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMode.adapter = adapter

        spinnerMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Toast.makeText(this@WateringSettingsActivity, "Selected", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@WateringSettingsActivity, "Not selected", Toast.LENGTH_SHORT).show()
            }
        }
    }
}