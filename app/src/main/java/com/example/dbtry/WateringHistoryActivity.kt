package com.example.dbtry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.widget.Toast

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.EntryXComparator
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class WateringHistoryActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watering_history)

        var plantRecord = intent.getStringExtra("PLANT_NAME")

        lineChart = findViewById(R.id.lineChart)


        val entries = mutableListOf<Entry>()
        val labels = mutableListOf<String>()

        createWeightChart(entries, labels)///////////////////////////////////////////

        db = FirebaseDatabase.getInstance().getReference("10032311").child(plantRecord.toString()).child("pump").child("history")
        db.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val key = snapshot.key
                val value = snapshot.value

                // Parse the key and value
                val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(key)
                val floatValue = value.toString().toFloat()

                // Create an Entry object with the parsed values
                val entry = Entry(dateTime.time.toFloat(), floatValue)

                // Add the entry and label to the respective lists
                entries.add(entry)
                labels.add(key.toString())


                if (labels.size == entries.size) {
                    // Update the chart with the new entry
                    createWeightChart(entries, labels)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                //TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                //TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }

        })
    }

    private fun createWeightChart(entries: List<Entry>, labels: List<String>) {
        if (entries.isEmpty()) {
            // Obsługa braku danych
            val chartMessage = "No soil moisture measurements."
            lineChart.setNoDataText(chartMessage)
            lineChart.setNoDataTextColor(Color.parseColor("#14471E"))
            lineChart.invalidate()
            return
        }

        // Ustawienia dla linii pomiarów wagi
        val dataSet = LineDataSet(entries, "Moisture")
        dataSet.color = Color.parseColor("#8014471E")
        dataSet.valueTextColor = Color.BLACK
        dataSet.setDrawValues(false)
        dataSet.setDrawCircles(true)
        dataSet.setCircleColors(listOf(Color.parseColor("#8014471E")))
        dataSet.circleRadius = 3f
        dataSet.circleHoleColor = Color.parseColor("#8014471E")
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        // Obliczanie punktów dla linii trendu
        val trendLineEntries = mutableListOf(
            Entry(entries.first().x, calculateTrendLineY(entries.first().x, entries)),
            Entry(entries.last().x, calculateTrendLineY(entries.last().x, entries))
        )
        val trendLineDataSet = LineDataSet(trendLineEntries, "Trend")
        trendLineDataSet.color = Color.parseColor("#338309")
        trendLineDataSet.setDrawValues(false)
        trendLineDataSet.setDrawCircles(false)
        trendLineDataSet.setDrawFilled(false)
        trendLineDataSet.enableDashedLine(10f, 5f, 0f)

        // Tworzenie danych dla wykresu
        val lineData = LineData(dataSet)

        // Ustawienie danych dla wykresu
        lineChart.data = lineData

        // Ustawienia dodatkowe
        dataSet.setDrawFilled(true)
        dataSet.fillColor = Color.parseColor("#8014471E")

        lineChart.invalidate()

        val xAxis = lineChart.xAxis
        xAxis.isEnabled = true // Set xAxis visibility to true

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.isGranularityEnabled = true
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.valueFormatter = object : ValueFormatter() {
            private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index >= 0 && index < labels.size) {
                    val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(labels[index])
                    dateTime?.let {
                        dateFormatter.format(it)
                    } ?: ""
                } else {
                    ""
                }
            }
        }



        xAxis.axisMaximum = entries.last().x

        val desiredRecordCount = 1260
        val minimumValue = if (entries.size > desiredRecordCount) {
            entries[entries.size - desiredRecordCount].x
        } else {
            entries.first().x
        }
        xAxis.axisMinimum = minimumValue

//         Obsługa rotacji etykiet osi X w zależności od ilości danych
        if (labels.size > 7) {
            xAxis.labelRotationAngle = -90f
            xAxis.setAvoidFirstLastClipping(false)
            lineChart.setExtraOffsets(0f, 0f, 0f, 30f)
        } else {
            xAxis.labelRotationAngle = 0f
            xAxis.setAvoidFirstLastClipping(false)
            lineChart.setExtraOffsets(0f, 0f, 0f, 0f)
        }

        // Ustawienia dla osi Y
        val yAxis = lineChart.axisLeft
        yAxis.isGranularityEnabled = true
        yAxis.setCenterAxisLabels(true)
        yAxis.setDrawLabels(true)
        yAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return String.format("%.0f %%", value)
            }
        }
        yAxis.granularity = 1f
        yAxis.isGranularityEnabled = true

        lineChart.axisRight.isEnabled = false
        lineChart.legend.isEnabled = false

        // Opis wykresu
        val description = Description()
        description.text = ""
        lineChart.description = description

        if (labels.size == entries.size){
//            Toast.makeText(applicationContext, "Correct", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateTrendLineY(x: Float, entries: List<Entry>): Float {
        val x1 = entries.first().x
        val y1 = entries.first().y
        val x2 = entries.last().x
        val y2 = entries.last().y

        // Obliczanie wartości dla linii trendu
        val m = (y2 - y1) / (x2 - x1)
        val b = y1 - m * x1

        return m * x + b
    }
}