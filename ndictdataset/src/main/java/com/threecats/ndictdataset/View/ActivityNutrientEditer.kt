package com.threecats.ndictdataset.View

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import com.threecats.ndictdataset.NutrientFragment.DemandFragment
import com.threecats.ndictdataset.NutrientFragment.NutrientFragment
import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.Shells.TabViewShell.TabViewLayoutShell
import kotlinx.android.synthetic.main.activity_nutrient_editer.*

class ActivityNutrientEditer : AppCompatActivity() {

    val viewPagerShell = TabViewLayoutShell()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrient_editer)
        viewPagerShell.parent(this)
                .tab(NutrientPropertyTabs)
                .viewPage(NutrientEditerViewPage)
                .addFragment(NutrientFragment(), "描述")
                .addFragment(DemandFragment(), "需求量")
                .link()
    }

}
