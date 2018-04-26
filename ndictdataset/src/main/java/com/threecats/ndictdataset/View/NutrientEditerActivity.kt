package com.threecats.ndictdataset.View

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.NutrientFragments.NutrientDosisFragment
import com.threecats.ndictdataset.NutrientFragments.NutrientContextFragment
import com.threecats.ndictdataset.NutrientFragments.NutrientSublistFragment
import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.Shells.TabViewShell.TabViewLayoutShell
import com.threecats.ndictdataset.Shells.TabViewShell.onShellTabSelectedListener
import kotlinx.android.synthetic.main.activity_nutrient_editer.*
import org.jetbrains.anko.toast

class NutrientEditerActivity : AppCompatActivity() {

    private val shareSet = BDM.ShareSet!!

    private val viewPagerShell = TabViewLayoutShell()
    private lateinit var workFragment: Fragment
    private lateinit var workTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrient_editer)  //NutrientEditerToolbar
        setSupportActionBar(NutrientEditerToolbar)

        NutrientEditerToolbar.title = shareSet.currentNutrient?.name
        NutrientEditerToolbar.setNavigationOnClickListener { onBackPressed() }

        when (shareSet.currentNutrient?.nutrientID){
            5 -> {
                workFragment = NutrientSublistFragment()
                workTitle = "人体所需维生素列表"
            }
            6 -> {
                workFragment = NutrientSublistFragment()
                workTitle = "人体所需微量元素列表"
            }
            else -> {
                workFragment = NutrientDosisFragment()
                workTitle = "日常需求量"
            }
        }

        viewPagerShell.setOnTabSelectedListener(object : onShellTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab, fragment: Fragment) {
                toast("${tab.text}")
            }
        })
        viewPagerShell.parent(this)
                .tab(NutrientPropertyTabs)
                .viewPage(NutrientEditerViewPage)
                .addFragment(NutrientContextFragment(), "描述")
                .addFragment(workFragment, workTitle)
                .link()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nutrientediter_menu, menu!!)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu!!.findItem(R.id.addDosisItem).isVisible = shareSet.currentNutrient?.nutrientID !in 5..6
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.addDosisItem -> {
                toast("加入摄入量")
                //checkFoodRelevant()
            }
        }
        return true
    }

}
