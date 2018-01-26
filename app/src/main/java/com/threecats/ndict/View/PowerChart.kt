package com.threecats.ndict.View

import android.graphics.Color
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import android.R.attr.entries
import android.R.attr.entries
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate


/**
 * 由 zhang 于 2018/1/24 创建
 */
class PowerChart(chart: PieChart) : OnChartValueSelectedListener {
    val powerChart: PieChart

    init {
        powerChart = chart

        with(powerChart) {
            setDrawMarkers(false)
            setUsePercentValues(false)
            description.isEnabled = false
            setExtraOffsets(5f, 1f, 5f, 1f)
            setDragDecelerationFrictionCoef(0.95f)
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            transparentCircleRadius = 67f
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(110)
            holeRadius = 65f
            setDrawCenterText(true)
            setDrawEntryLabels(false)
            rotationAngle = 0f
            isRotationEnabled = false
            isHighlightPerTapEnabled = true
            legend.isEnabled = true
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
            legend.setOrientation(Legend.LegendOrientation.VERTICAL)
            legend.textColor = Color.WHITE
            legend.setDrawInside(false)
            legend.setXEntrySpace(3f)
            legend.setYEntrySpace(3f)
            legend.setYOffset(16f)
            setOnChartValueSelectedListener(this@PowerChart)

        }

        //powerChart.setDrawMarkers(false)

        //powerChart.setUsePercentValues(false)

        //powerChart.getDescription().setEnabled(false)
        //powerChart.setExtraOffsets(5f,1f,5f,1f)
        //powerChart.setDragDecelerationFrictionCoef(0.95f)

        //powerChart.setDrawHoleEnabled(true)
        //powerChart.setHoleColor(Color.WHITE)

        //powerChart.setTransparentCircleColor(Color.WHITE);
        //powerChart.setTransparentCircleAlpha(110)

        //powerChart.setHoleRadius(70f)
        //powerChart.setTransparentCircleRadius(73f)

        //powerChart.setDrawCenterText(true)
        //powerChart.setDrawEntryLabels(false)

        //powerChart.setRotationAngle(0f)
        //enablerotationofthechartbytouch
        //powerChart.setRotationEnabled(false)
        //powerChart.setHighlightPerTapEnabled(true)
        //powerChart.legend.isEnabled = false
    }

    fun setPower(power: Float) {
        val entries = ArrayList<PieEntry>()
        var carbohydrate: Float = (power * 0.63).toFloat()
        var protein: Float = (power * 0.12).toFloat()
        var fat: Float = (power * 0.25).toFloat()
        entries.add(PieEntry(carbohydrate, "碳水化合物"))
        entries.add(PieEntry(protein, "蛋白质"))
        entries.add(PieEntry(fat, "脂肪"))

        val dataSet = PieDataSet(entries, "每日能量需求")
        val colors = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors

        val data = PieData(dataSet)
        //data.setValueFormatter(newPercentFormatter())
        data.setValueTextSize(8f)
        data.setValueTextColor(Color.BLUE)
        //data.setValueTypeface(mTfLight)

        powerChart.centerText = "BMR\n${power.toInt()}"
        powerChart.setData(data)

        powerChart.highlightValues(null)
        //powerChart.invalidate()
        powerChart.animateXY(800, 800);
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        changeLegend()
    }

    override fun onNothingSelected() {
        //changeLegend()
    }

    fun changeLegend() {
        powerChart.legend.isEnabled = !powerChart.legend.isEnabled
        powerChart.invalidate()
    }
}