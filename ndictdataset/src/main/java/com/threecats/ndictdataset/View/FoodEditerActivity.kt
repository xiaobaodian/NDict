package com.threecats.ndictdataset.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.EventClass.NextFragment
import com.threecats.ndictdataset.FoodFragments.*
import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.Shells.TabViewShell.TabViewLayoutShell
import com.threecats.ndictdataset.Shells.TabViewShell.onShellTabSelectedListener
import kotlinx.android.synthetic.main.activity_food_editer.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.*

class FoodEditerActivity : AppCompatActivity() {

    private val shareSet = BDM.ShareSet!!

    //private val foodPropertyFragments = mutableListOf<FoodPropertyFragment>()

    private val nutrientFragmentTabs = TabViewLayoutShell()
    private var currentTabPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_editer)
        setSupportActionBar(FoodEditerToolbar)
        FoodEditerToolbar.setNavigationOnClickListener { onBackPressed() }  //FoodNutrientFragment

        nutrientFragmentTabs.setOnTabSelectedListener(object: onShellTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab, fragment: Fragment) {
                currentTabPosition = tab.position
                if (tab.position == 0) {
                    FoodEditerToolbar.title = "食材详情"
                    FoodEditerToolbar.subtitle = ""
                } else {
                    FoodEditerToolbar.title = shareSet.currentFood!!.name
                    FoodEditerToolbar.subtitle = "每100克中的含量"
                }
            }
        })
        nutrientFragmentTabs.parent(this)
                .tab(FoodPropertyTabs)
                .viewPage(FoodEditerViewPage)
                .addFragment(FoodNameFragment(),"名称")
                .addFragment(FoodNutrientFragment(),"营养素")
                .addFragment(FoodVitaminFragment(),"维生素")
                .addFragment(FoodMineralFragment(),"矿物质")
                .addFragment(FoodNoteFragment(),"描述")
                .link()

        shareSet.editorFood.item?.let {
            val food = it
            shareSet.currentCategory?.let {
                if (food.foodBased != it.foodBased) food.foodBased = it.foodBased
            }
        }

        EventBus.getDefault().register(this@FoodEditerActivity)
    }

    override fun onDestroy() {
        shareSet.editorFood.item?.let { viewFieldsToItem(it) }
        shareSet.editorFood.commit()
        EventBus.getDefault().unregister(this@FoodEditerActivity)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.foodediter_menu, menu!!)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu!!.findItem(R.id.SaveAddItem).isVisible = shareSet.editorFood.isAppend
        val menuItem = menu.findItem((R.id.REChange))
        shareSet.currentFood?.let {
            if (it.foodBased == 0) {
                menuItem.title = "视黄醇/胡萝卜素"
            } else {
                menuItem.title = "视黄醇/维生素A"
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.DeleteFood -> {
                shareSet.currentFood?.let { alertDeleteFood(it) }
            }
            R.id.SaveAddItem -> {
                shareSet.editorFood.item?.let { viewFieldsToItem(it) }
                shareSet.editorFood.commit().append(BFood())
                //shareSet.editorFood.append(BFood())
                shareSet.currentFood = shareSet.editorFood.item   // 考虑去掉
                shareSet.editorFood.item?.let {
                    itemToViewFields(it)
                    firstEditTextFocus()
                }
                nutrientFragmentTabs.selectTab(0)
            }
            R.id.REChange -> {
                val fragment = nutrientFragmentTabs.fragments[2]
                if (fragment is FoodVitaminFragment) {
                    fragment.switchREMode()
                }
            }
        }
        return true
    }

//    override fun onBackPressed()

    private fun viewFieldsToItem(food: BFood){
        nutrientFragmentTabs.fragments.forEach { (it as FoodPropertyFragment).viewFieldsToItem(food) }
    }

    private fun itemToViewFields(food: BFood){
        nutrientFragmentTabs.fragments.forEach { (it as FoodPropertyFragment).itemToViewFields(food) }
    }

    private fun firstEditTextFocus(){
        nutrientFragmentTabs.fragments.forEach { (it as FoodPropertyFragment).firstEditTextFocus() }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)  //, sticky = true
    fun nextFragment(next: NextFragment){
        nutrientFragmentTabs.next()
    }

    private fun alertDeleteFood(food: BFood){

        alert("确实要删除食材 ${food.name} 吗？", "删除食材") {
            positiveButton("确定") {
                //deleteFoodFromBmob(food)
                shareSet.editorFood.delete()
                onBackPressed()
            }
            negativeButton("取消") {  }
        }.show()

    }

}
