package com.threecats.ndictdataset.View

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import com.threecats.ndictdataset.NutrientFragments.NutrientDosisFragment
import com.threecats.ndictdataset.NutrientFragments.NutrientContextFragment
import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.Shells.TabViewShell.TabViewLayoutShell
import com.threecats.ndictdataset.Shells.TabViewShell.onShellTabSelectedListener
import kotlinx.android.synthetic.main.activity_nutrient_editer.*
import org.jetbrains.anko.toast

class ActivityNutrientEditer : AppCompatActivity() {

    private val viewPagerShell = TabViewLayoutShell()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrient_editer)
        viewPagerShell.setOnTabSelectedListener(object : onShellTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab, fragment: android.support.v4.app.Fragment) {
                toast("${tab.text}")
            }
        })
        viewPagerShell.parent(this)
                .tab(NutrientPropertyTabs)
                .viewPage(NutrientEditerViewPage)
                .addFragment(NutrientContextFragment(), "描述")
                .addFragment(NutrientDosisFragment(), "需求量")
                .link()
    }

}
