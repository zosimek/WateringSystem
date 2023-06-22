package com.example.dbtry

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class WateringHistoryActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart
    private lateinit var db : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moisture_history)

        var plantRecord = intent.getStringExtra("PLANT_NAME")

        lineChart = findViewById(R.id.lineChart)

        // Get the entries and labels from Firebase
        val entries = mutableListOf<Entry>()
        val labels = mutableListOf<String>()

        // Set up Firebase reference
        db = FirebaseDatabase.getInstance().getReference("10032311").child(plantRecord.toString()).child("pump").child("history")

        // Retrieve data from Firebase
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

                // Update the chart with the new entry
                updateChart(entries, labels)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // Handle data changes if needed
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Handle data removal if needed
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Handle child movement if needed
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle cancellation if needed
            }
        })
    }

    private fun updateChart(entries: List<Entry>, labels: List<String>) {
        // Create a LineDataSet with the entries
        val dataSet = LineDataSet(entries, "Percentage")
        dataSet.color = ContextCompat.getColor(this@WateringHistoryActivity, R.color.theme_green)
        dataSet.setDrawFilled(true)
        dataSet.fillColor = ContextCompat.getColor(this@WateringHistoryActivity, R.color.theme_green)
        dataSet.fillAlpha = 100
        dataSet.setDrawCircles(true)
        dataSet.setCircleColors(listOf(ContextCompat.getColor(this@WateringHistoryActivity, R.color.theme_green)))
        dataSet.circleRadius = 5f
        dataSet.circleHoleColor = ContextCompat.getColor(this@WateringHistoryActivity, R.color.theme_green)
        dataSet.setDrawValues(false)

        // Create a LineData object with the LineDataSet
        val lineData = LineData(dataSet)

        // Set the data to the line chart
        lineChart.data = lineData

        // Customize the chart appearance
        lineChart.setBackgroundColor(Color.WHITE)
        lineChart.setDrawGridBackground(true)
        lineChart.setPinchZoom(true)

        val ll1 = LimitLine(30f, "Title")

        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM)
        ll1.setTextSize(10f)

        val ll2 = LimitLine(35f, "")
        ll2.lineWidth = 4f
        ll2.enableDashedLine(10f, 10f, 0f)

        val xAxisFormatter = object : ValueFormatter() {
            private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

            override fun getFormattedValue(value: Float): String {
                // Convert the value from milliseconds to a Date object
                val date = Date(value.toLong())

                // Format the date using SimpleDateFormat
                return dateFormatter.format(date)
            }
        }

        val xAxis: XAxis = lineChart.getXAxis()
        xAxis.valueFormatter = xAxisFormatter
        val leftAxis: YAxis = lineChart.getAxisLeft()
        xAxis.labelRotationAngle = -70f
        val position = XAxisPosition.BOTTOM
        xAxis.position = position
        xAxis.granularity = 1f
        xAxis.textSize = 12f


        // Customize the y-axis
        val yAxis = lineChart.axisLeft
        yAxis.setDrawGridLines(true)
        lineChart.legend.isEnabled = false
        yAxis.textSize = 12f

        yAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return String.format("%.0f ml", value)
            }
        }
        yAxis.granularity = 1f
        yAxis.isGranularityEnabled = true

        // Hide the right y-axis
        lineChart.axisRight.isEnabled = false

        // Set the description
        val description = Description()
        description.text = ""
        lineChart.description = description

        // Enable scrolling
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)
        lineChart.setTouchEnabled(true)

        // Refresh the chart
        lineChart.invalidate()
    }

    inner class DateTimeValueFormatter(private val labels: List<String>) : ValueFormatter() {

        private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        override fun getFormattedValue(value: Float): String {
            val index = value.toInt()
            return if (index >= 0 && index < labels.size) {
                val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    .parse(labels[index])
                dateTime?.let {
                    dateFormatter.format(it)
                } ?: ""
            } else {
                ""
            }
        }
    }
    class ClaimsXAxisValueFormatter(private val datesList: List<String>) : ValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val position = value.roundToInt()
            val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())

            return when {
                value > 1 && value < 2 -> {
                    val newPosition = 0
                    if (newPosition < datesList.size)
                        sdf.format(Date(Utils.getDateInMilliseconds(datesList[newPosition], "yyyy-MM-dd")))
                    else
                        ""
                }
                value > 2 && value < 3 -> {
                    val newPosition = 1
                    if (newPosition < datesList.size)
                        sdf.format(Date(Utils.getDateInMilliseconds(datesList[newPosition], "yyyy-MM-dd")))
                    else
                        ""
                }
                value > 3 && value < 4 -> {
                    val newPosition = 2
                    if (newPosition < datesList.size)
                        sdf.format(Date(Utils.getDateInMilliseconds(datesList[newPosition], "yyyy-MM-dd")))
                    else
                        ""
                }
                value > 4 && value <= 5 -> {
                    val newPosition = 3
                    if (newPosition < datesList.size)
                        sdf.format(Date(Utils.getDateInMilliseconds(datesList[newPosition], "yyyy-MM-dd")))
                    else
                        ""
                }
                else -> ""
            }
        }
    }
    object Utils {
        fun getDateInMilliseconds(dateString: String, pattern: String): Long {
            val sdf = SimpleDateFormat(pattern, Locale.getDefault())
            val date = sdf.parse(dateString)
            return date?.time ?: 0L
        }
    }
}