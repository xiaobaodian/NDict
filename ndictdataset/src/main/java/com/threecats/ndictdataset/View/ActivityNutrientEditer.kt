package com.threecats.ndictdataset.View

import android.app.Activity
import android.os.Bundle
import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.Shells.TabViewShell.TabViewLayoutShell

class ActivityNutrientEditer : Activity() {

    val viewPagerShell: TabViewLayoutShell = TabViewShell()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrient_editer)
    }
}
