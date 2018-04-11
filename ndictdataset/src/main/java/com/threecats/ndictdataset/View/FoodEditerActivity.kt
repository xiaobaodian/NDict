package com.threecats.ndictdataset.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import cn.bmob.v3.BmobBatch
import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BatchResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Bmob.BFoodCategory
import com.threecats.ndictdataset.Enum.EChangeBlock
import com.threecats.ndictdataset.Enum.EEditerState
import com.threecats.ndictdataset.EventClass.DeleteFoodRecyclerItem
import com.threecats.ndictdataset.EventClass.UpdateFoodRecyclerItem
import com.threecats.ndictdataset.FoodFragment.*
import com.threecats.ndictdataset.Helper.ErrorMessage
import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.Shells.RecyclerViewShell.RecyclerViewItem
import com.threecats.ndictdataset.Shells.TabViewShell.TabViewLayoutShell
import com.threecats.ndictdataset.Shells.TabViewShell.onShellTabSelectedListener
import kotlinx.android.synthetic.main.activity_food_editer.*
import org.greenrobot.eventbus.EventBus
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
                    FoodEditerToolbar.title = shareSet.CurrentFood?.getObject()!!.name
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
        val food: BFood = shareSet.CurrentFood!!.self
        val category: BFoodCategory = shareSet.CurrentCategory!!.self
        if (food.foodBased != category.foodBased) {
            food.foodBased = category.foodBased
            updateVitaminToBmob(food)
        }
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
        shareSet.CurrentFood?.let {
            if (it.self.foodBased == 0) {
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
                shareSet.CurrentFood?.let { alertDeleteFood(it) }
            }
            R.id.SaveAddItem -> {
                shareSet.CurrentFood?.let {
                    val food = it
                    nutrientFragments.fragments.forEach { (it as FoodPropertyFragment).exportFields(food.self) }
                    processFood(food.self)
                }
                shareSet.createFood()
                shareSet.CurrentFood?.let {
                    val food = it
                    nutrientFragments.fragments.forEach { (it as FoodPropertyFragment).importFields(food.self) }
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
            shareSet.CurrentFood?.let {
                val food = it
                nutrientFragments.fragments.forEach { (it as FoodPropertyFragment).exportFields(food.self) }
                processFood(food.self)
            }
        } else {
            changBlockList.clear()
            nutrientFragments.fragments.forEach { (it as FoodPropertyFragment).blockChangeState(this) }
            if (changBlockList.size > 0) {
                shareSet.CurrentFood?.let {
                    val food = it
                    nutrientFragments.fragments.forEach { (it as FoodPropertyFragment).exportFields(food.self) }
                    processFood(food.self)
                }
            }
        }
        super.onBackPressed()
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
                // 加入数据需要在Bmob段建立关联，需要加入维生素、矿物质数据后获取返回的objectId，所以
                // 采用链式调用
                addBatchFoodExtToBmob(food)
            }
            EEditerState.FoodEdit -> {
                changBlockList.forEach {
                    when (it) {
                        EChangeBlock.Food -> updateFoodToBmob(food)
                        EChangeBlock.Vitamin -> updateVitaminToBmob(food)
                        EChangeBlock.Mineral -> updateMineralToBmob(food)
                        EChangeBlock.MineralExt -> updateMineralExtToBmob(food)
                        else -> toast("changBlockList Error !")
                    }
                }
            }
            else -> toast("EditState Error !")
        }
    }

//    private fun addVitaminItem(food: BFood){
//        val v = food.vitamin!!
//        v.save(object: SaveListener<String>() {
//            override fun done(objectID: String?, e: BmobException?) {
//                if (e == null) {
//                    if (BDM.ShowTips) toast("添加了维生素，objectID：$objectID")
//                    v.objectId = objectID
//                    addMineral(food)
//                } else {
//                    longToast("添加食材${food.name}维生素出现错误。错误信息：${e.message} -- ${e.errorCode}")
//                }
//            }
//        })
//    }
//    private fun addMineralExt(food: BFood){
//        val mext = food.mineralExt!!
//        mext.save(object: SaveListener<String>() {
//            override fun done(objectID: String?, e: BmobException?) {
//                if (e == null) {
//                    if (BDM.ShowTips) toast("添加了矿物质扩展，objectID：$objectID")
//                    mext.objectId = objectID
//                    addFoodItem(food)
//                } else {
//                    longToast("添加食材${food.name}矿物质扩展出现错误，回滚资料。错误信息：${e.message}")
//                    food.mineral?.delete(object: UpdateListener(){
//                        override fun done(p0: BmobException?) {
//                        }
//                    })
//                    food.vitamin?.delete(object: UpdateListener(){
//                        override fun done(p0: BmobException?) {
//                        }
//                    })
//                }
//            }
//        })
//    }
//    private fun addMineral(food: BFood){
//        val m = food.mineral!!
//        m.save(object: SaveListener<String>() {
//            override fun done(objectID: String?, e: BmobException?) {
//                if (e == null) {
//                    if (BDM.ShowTips) toast("添加了矿物质，objectID：$objectID")
//                    m.objectId = objectID
//                    addMineralExt(food)
//                } else {
//                    longToast("添加食材${food.name}矿物质出现错误，回滚资料。错误信息：${e.message}")
//                    food.vitamin?.delete(object: UpdateListener(){
//                        override fun done(p0: BmobException?) {
//                        }
//                    })
//                }
//            }
//        })
//    }

    private fun addBatchFoodExtToBmob(food: BFood){
        val items: MutableList<BmobObject> = arrayListOf()
        food.vitamin?.let { items.add(it) }
        food.mineral?.let { items.add(it) }
        food.mineralExt?.let { items.add(it) }
        BmobBatch().insertBatch(items).doBatch(object: QueryListListener<BatchResult>(){
            override fun done(result: MutableList<BatchResult>?, e: BmobException?) {
                if (e == null) {
                    if (result?.get(0)?.error == null) food.vitamin?.objectId = result?.get(0)?.objectId
                    if (result?.get(1)?.error == null) food.mineral?.objectId = result?.get(1)?.objectId
                    if (result?.get(2)?.error == null) food.mineralExt?.objectId = result?.get(2)?.objectId
                    addFoodToBmob(food)
                } else {
                    //longToast("添加食材扩展数据出现错误：${e.message}")
                    ErrorMessage(this@FoodEditerActivity, e)
                }
            }
        })
    }

    private fun addFoodToBmob(food: BFood){
        food.category = shareSet.CurrentCategory?.getObject()!!
        food.save(object: SaveListener<String>() {
            override fun done(objectID: String?, e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("添加了食材[${food.name}]，objectID：$objectID")
                    val f = RecyclerViewItem<Any, BFood>().putObject(food)
                    EventBus.getDefault().post(UpdateFoodRecyclerItem(f, EEditerState.FoodAppend))  //Sticky
                } else {
                    //longToast("添加食材${food.name}出现错误。错误信息：${e.message}")
                    ErrorMessage(this@FoodEditerActivity, e)
                }
            }
        })
    }

    private fun updateMineralToBmob(food: BFood){
        food.mineral?.update(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("更新了矿物质数据")
                } else {
                    //longToast("更新矿物质数据出现错误：${e.message}")
                    ErrorMessage(this@FoodEditerActivity, e)
                }
            }
        })
    }

    private fun updateMineralExtToBmob(food: BFood){
        food.mineralExt?.update(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("更新了矿物质扩展数据")
                } else {
                    //longToast("更新矿物质扩展数据出现错误：${e.message}")
                    ErrorMessage(this@FoodEditerActivity, e)
                }
            }
        })
    }

    private fun updateVitaminToBmob(food: BFood){
        food.vitamin?.update(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("更新了维生素数据")
                } else {
                    //longToast("更新维生素数据出现错误：${e.message}")
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
                    val f = RecyclerViewItem<Any, BFood>().putObject(food)
                    EventBus.getDefault().post(UpdateFoodRecyclerItem(f, EEditerState.FoodEdit))
                } else {
                    //longToast("更新食材数据出现错误：${e.message}")
                    ErrorMessage(this@FoodEditerActivity, e)
                }
            }
        })
    }

    private fun alertDeleteFood(food: RecyclerViewItem<Any, BFood>){

        alert("确实要删除食材 ${food.getObject()?.name} 吗？", "删除食材") {
            positiveButton("确定") { deleteFoodFromBmob(food) }
            negativeButton("取消") {  }
        }.show()

    }

    private fun deleteFoodFromBmob(food: RecyclerViewItem<Any, BFood>){
        val items: MutableList<BmobObject> = arrayListOf()
        food.getObject()?.vitamin?.let { items.add(it) }
        food.getObject()?.mineral?.let { items.add(it) }
        food.getObject()?.mineralExt?.let { items.add(it) }
        items.add(food.getObject()!!)
        BmobBatch().deleteBatch(items).doBatch(object: QueryListListener<BatchResult>() {
            override fun done(p0: MutableList<BatchResult>?, e: BmobException?) {
                if (e == null) {
                    toast("删除食材 ${food.getObject()?.name} 成功")
                    EventBus.getDefault().post(DeleteFoodRecyclerItem(food))
                } else {
                    ErrorMessage(this@FoodEditerActivity, e)
                    //toast("删除食材 ${food.name} 失败")
                }
            }
        })
        onBackPressed()
    }


}
