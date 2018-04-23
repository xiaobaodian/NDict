package com.threecats.ndictdataset.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Bmob.BFoodCategory
import com.threecats.ndictdataset.Enum.EChangeBlock
import com.threecats.ndictdataset.Enum.EEditerState
import com.threecats.ndictdataset.EventClass.DeleteFoodRecyclerItem
import com.threecats.ndictdataset.EventClass.NextFragment
import com.threecats.ndictdataset.EventClass.UpdateFoodRecyclerItem
import com.threecats.ndictdataset.FoodFragments.*
import com.threecats.ndictdataset.Helper.ErrorMessage
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

    private val nutrientFragments = TabViewLayoutShell()
    private var currentTabPosition = 0
    private val changBlockList: MutableList<EChangeBlock> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_editer)
        setSupportActionBar(FoodEditerToolbar)
        FoodEditerToolbar.setNavigationOnClickListener { onBackPressed() }  //FoodNutrientFragment

        nutrientFragments.setOnTabSelectedListener(object: onShellTabSelectedListener{
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
        nutrientFragments.parent(this)
                .tab(FoodPropertyTabs)
                .viewPage(FoodEditerViewPage)
                .addFragment(FoodNameFragment(),"名称")
                .addFragment(FoodNutrientFragment(),"营养素")
                .addFragment(FoodVitaminFragment(),"维生素")
                .addFragment(FoodMineralFragment(),"矿物质")
                .addFragment(FoodNoteFragment(),"描述")
                .link()
        val food: BFood = shareSet.currentFood!!
        val category: BFoodCategory = shareSet.currentCategory!!
        if (food.foodBased != category.foodBased) {
            food.foodBased = category.foodBased
            updateFoodToBmob(food)
        }
        EventBus.getDefault().register(this@FoodEditerActivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this@FoodEditerActivity)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.foodediter_menu, menu!!)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when (shareSet.ItemEditState){
            EEditerState.FoodAppend -> {menu!!.findItem(R.id.SaveAddItem).isVisible = true}
            EEditerState.FoodEdit   -> {menu!!.findItem(R.id.SaveAddItem).isVisible = false}
            else -> toast("EditState Error !")
        }
        val menuItem = menu!!.findItem((R.id.REChange))
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
                shareSet.currentFood?.let {
                    val food = it
                    nutrientFragments.fragments.forEach { (it as FoodPropertyFragment).exportFields(food) }
                    processFood(food)
                }
                shareSet.createFood()
                shareSet.currentFood?.let {
                    val food = it
                    nutrientFragments.fragments.forEach { (it as FoodPropertyFragment).importFields(food) }
                    nutrientFragments.fragments.forEach { (it as FoodPropertyFragment).firstEditTextFocus() }
                }
                FoodPropertyTabs.getTabAt(0)?.select()
            }
            R.id.REChange -> {
                val fragment = nutrientFragments.fragments[2]
                if (fragment is FoodVitaminFragment) {
                    fragment.switchREMode()
                }
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (shareSet.ItemEditState == EEditerState.FoodAppend) {
            shareSet.currentFood?.let {
                val food = it
                nutrientFragments.fragments.forEach { (it as FoodPropertyFragment).exportFields(food) }
                processFood(food)
            }
        } else {
            changBlockList.clear()
            nutrientFragments.fragments.forEach { (it as FoodPropertyFragment).blockChangeState(this) }
            if (changBlockList.size > 0) {
                shareSet.currentFood?.let {
                    val food = it
                    nutrientFragments.fragments.forEach { (it as FoodPropertyFragment).exportFields(food) }
                    processFood(food)
                }
            }
        }
        super.onBackPressed()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)  //, sticky = true
    fun nextFragment(next: NextFragment){
        nutrientFragments.next()
    }

    fun addChangeBlock(changeBlock: EChangeBlock){
        val position = changBlockList.indexOf(changeBlock)
        if (position < 0) {
            changBlockList.add(changeBlock)
        }
    }

    private fun processFood(food: BFood){

        if (food.name.isEmpty()) return

        when (shareSet.ItemEditState){
            EEditerState.FoodAppend -> {
                addFoodToBmob(food)
            }
            EEditerState.FoodEdit -> {
                updateFoodToBmob(food)
            }
            else -> toast("EditState Error !")
        }
    }

    private fun addFoodToBmob(food: BFood){
        food.category = shareSet.currentCategory
        food.save(object: SaveListener<String>() {
            override fun done(objectID: String?, e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("添加了食材[${food.name}]，objectID：$objectID")
                    EventBus.getDefault().post(UpdateFoodRecyclerItem(food, EEditerState.FoodAppend))  //Sticky
                } else {
                    //longToast("添加食材${food.name}出现错误。错误信息：${e.message}")
                    ErrorMessage(this@FoodEditerActivity, e)
                }
            }
        })
    }

    private fun updateFoodToBmob(food: BFood){
        food.update(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("更新了食材数据")
                    EventBus.getDefault().post(UpdateFoodRecyclerItem(food, EEditerState.FoodEdit))
                } else {
                    //longToast("更新食材数据出现错误：${e.message}")
                    ErrorMessage(this@FoodEditerActivity, e)
                }
            }
        })
    }

    private fun alertDeleteFood(food: BFood){

        alert("确实要删除食材 ${food.name} 吗？", "删除食材") {
            positiveButton("确定") { deleteFoodFromBmob(food) }
            negativeButton("取消") {  }
        }.show()

    }

    private fun deleteFoodFromBmob(food: BFood){

        food.delete(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    toast("删除食材 ${food.name} 成功")
                    EventBus.getDefault().post(DeleteFoodRecyclerItem(food))
                } else {
                    ErrorMessage(this@FoodEditerActivity, e)
                }
            }
        })
        onBackPressed()
    }

}
