package com.threecats.ndict.View

import android.graphics.Color
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import android.R.attr.entries
import android.R.attr.entries
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.utils.ColorTemplate











/**
 * 由 zhang 于 2018/1/24 创建
 */
class PowerChart(chart: PieChart) {
    val powerChart: PieChart
    init {
        powerChart = chart

        powerChart.setDrawMarkers(false)

        powerChart.setUsePercentValues(true)

        powerChart.getDescription().setEnabled(false)
        powerChart.setExtraOffsets(5f,1f,5f,1f)
        powerChart.setDragDecelerationFrictionCoef(0.95f)

        powerChart.setDrawHoleEnabled(true)
        powerChart.setHoleColor(Color.WHITE)

        powerChart.setTransparentCircleColor(Color.WHITE);
        powerChart.setTransparentCircleAlpha(110);

        powerChart.setHoleRadius(70f);
        powerChart.setTransparentCircleRadius(73f);

        powerChart.setDrawCenterText(true)
        powerChart.setDrawEntryLabels(false)

        powerChart.setRotationAngle(0f);
        //enablerotationofthechartbytouch
        powerChart.setRotationEnabled(false);
        powerChart.setHighlightPerTapEnabled(true);
    }

    fun setPower(power: Float){
        val entries = ArrayList<PieEntry>()
        var carbohydrate: Float = (power*0.63).toFloat()
        var protein: Float = (power*0.12).toFloat()
        var fat: Float = (power*0.25).toFloat()
        entries.add(PieEntry(carbohydrate,"碳水化合物：${carbohydrate.toInt()}"))
        entries.add(PieEntry(protein,"蛋白质：${protein.toInt()}"))
        entries.add(PieEntry(fat,"脂肪：${fat.toInt()}"))

        val dataSet = PieDataSet(entries, "每日能量需求构成")
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

        powerChart.centerText = "BMR：${power.toInt()}"
        powerChart.setData(data)

        powerChart.highlightValues(null)
        powerChart.invalidate()
    }
}