package com.threecats.ndict.View


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndict.Helper.SpinnerHelper
import com.threecats.ndict.Models.DataSet

import com.threecats.ndict.R
import com.threecats.ndict.ViewModels.PersonPlus
import kotlinx.android.synthetic.main.fragment_diet_records.*


class DietRecordsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_diet_records, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //date.text="后天"
        val man = PersonPlus(DataSet.currentPerson)
        val powerChart = PowerChart(piechart)
        val list: List<String> = arrayListOf("轻微活动","日常家务","轻体力劳动","中体力劳动","重体力劳动")
        powerChart.setPower(man.BMR.auto.toFloat())
        val sa = SpinnerHelper(context, spinner)
        sa.items = arrayListOf("轻微活动","日常家务","轻体力","中体力","重体力")
        sa.bind()
    }

}
