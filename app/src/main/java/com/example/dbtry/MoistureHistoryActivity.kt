package com.example.dbtry

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class MoistureHistoryActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moisture_history)

        lineChart = findViewById(R.id.lineChart)

        // Tworzenie wykresu wagi na podstawie danych wejściowych
//        createWeightChart(entries, labels)
    }

    private fun createWeightChart(entries: List<Entry>, labels: List<String>) {
        if (entries.isEmpty()) {
            // Obsługa braku danych
            val chartMessage = "No data. Add weight measurements."
            lineChart.setNoDataText(chartMessage)
            lineChart.setNoDataTextColor(Color.parseColor("#14471E"))
            lineChart.invalidate()
            return
        }

        // Ustawienia dla linii pomiarów wagi
        val dataSet = LineDataSet(entries, "Weight")
        dataSet.color = Color.parseColor("#8014471E")
        dataSet.valueTextColor = Color.BLACK
        dataSet.setDrawValues(false)
        dataSet.setDrawCircles(true)
        dataSet.setCircleColors(listOf(Color.parseColor("#8014471E")))
        dataSet.circleRadius = 5f
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
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.isGranularityEnabled = true
        xAxis.setCenterAxisLabels(false)

        // Ustawienie wartości dla osi X
        xAxis.valueFormatter = object : ValueFormatter() {
            private val dateFormatter = SimpleDateFormat("MM.yyyy", Locale.getDefault())

            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index >= 0 && index < labels.size) {
                    labels[index]
                } else {
                    ""
                }
            }
        }

        xAxis.labelCount = labels.size

        xAxis.axisMinimum = 0f

        // Obsługa rotacji etykiet osi X w zależności od ilości danych
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
        yAxis.setDrawLabels(true)
        yAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return String.format("%.1f kg", value)
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