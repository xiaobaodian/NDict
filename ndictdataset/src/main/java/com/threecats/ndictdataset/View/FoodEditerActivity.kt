package com.threecats.ndictdataset.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.Menu
import android.view.MenuItem
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Enum.ChangeBlock
import com.threecats.ndictdataset.Enum.EditerState
import com.threecats.ndictdataset.EventClass.DeleteFoodRecyclerItem
import com.threecats.ndictdataset.EventClass.UpdateFoodRecyclerItem
import com.threecats.ndictdataset.FoodFragment.*
import com.threecats.ndictdataset.R
import kotlinx.android.synthetic.main.activity_food_editer.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.*

class FoodEditerActivity : AppCompatActivity() {

    val shareSet = BDM.ShareSet!!

    val foodPropertyFragments = mutableListOf<FoodPropertyFragment>()
    var currentFragment: FoodPropertyFragment? = null
    val changBlockList: MutableList<ChangeBlock> = arrayListOf()

    var test = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_editer)
        setSupportActionBar(FoodEditerToolbar)
        FoodEditerToolbar.setNavigationOnClickListener { onBackPressed() }  //FoodNutrientFragment

        addFragments(FoodNameFragment(),"名称")
        addFragments(FoodNutrientFragment(),"营养素")
        addFragments(FoodVitaminFragment(),"维生素")
        addFragments(FoodMineralFragment(),"矿物质")
        addFragments(FoodNoteFragment(),"描述")
        //addFragments(FoodPictureFragment(),"图片")

        currentFragment = foodPropertyFragments[0]

        FoodEditerViewPage.offscreenPageLimit = foodPropertyFragments.size
        FoodEditerViewPage.adapter = FoodEditerGroupAdapter(supportFragmentManager, foodPropertyFragments)
        FoodPropertyTabs.setupWithViewPager(FoodEditerViewPage)
        FoodPropertyTabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { currentFragment = foodPropertyFragments[tab!!.position] }
                if (tab?.position == 0) {
                    FoodEditerToolbar.title = "食材详情"
                } else {
                    FoodEditerToolbar.title = shareSet.CurrentFood?.name
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.foodediter_menu, menu!!)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when (shareSet.ItemEditState){
            EditerState.FoodAppend -> {menu!!.findItem(R.id.SaveAddItem).isVisible = true}
            EditerState.FoodEdit   -> {menu!!.findItem(R.id.SaveAddItem).isVisible = false}
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.DeleteFood -> {
                alertDeleteFood()
            }
            R.id.SaveAddItem -> {
                var saveFood = shareSet.CurrentFood!!
                foodPropertyFragments.forEach { it.ExportFields(saveFood) }
                processFood(saveFood)
                shareSet.createFood()
                foodPropertyFragments.forEach { it.ImportFields(shareSet.CurrentFood!!) }
                foodPropertyFragments.forEach { it.FirstEditTextFocus() }
                FoodPropertyTabs.getTabAt(0)?.select()
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (shareSet.ItemEditState == EditerState.FoodAppend) {
            test = true
            foodPropertyFragments.forEach { it.ExportFields(shareSet.CurrentFood!!) }
            processFood(shareSet.CurrentFood!!)
        } else {
            changBlockList.clear()
            foodPropertyFragments.forEach { it.BlockChangeState(this) }
            if (changBlockList.size > 0) {
                foodPropertyFragments.forEach { it.ExportFields(shareSet.CurrentFood!!) }
                processFood(shareSet.CurrentFood!!)
            }
        }
        super.onBackPressed()
    }

    fun addChangeBlock(changeBlock: ChangeBlock){
        val position = changBlockList.indexOf(changeBlock)
        if (position < 0) {
            changBlockList.add(changeBlock)
        }
    }

    private fun addFragments(fragment: FoodPropertyFragment, name: String){
        val bundle = Bundle()
        bundle.putString("name", name)
        fragment.setArguments(bundle)
        foodPropertyFragments.add(fragment)

//        if (BuildConfig.DEBUG) {
//            val logshow = AnkoLogger("NDIC")
//            logshow.info { "设置Fragment参数传递" }
//        }
    }

    private fun processFood(food: BFood){

        if (food.name.length == 0) return

        when (shareSet.ItemEditState){
            EditerState.FoodAppend -> {
                // 加入数据需要在Bmob段建立关联，需要加入维生素、矿物质数据后获取返回的objectId，所以
                // 采用链式调用
                addVitaminItem(food)
            }
            EditerState.FoodEdit -> {
                changBlockList.forEach {
                    when (it) {
                        ChangeBlock.Food -> updateFoodItem(food)
                        ChangeBlock.Vitamin -> updateVitaminItem(food)
                        ChangeBlock.Mineral -> updateMineralItem(food)
                        ChangeBlock.MineralExt -> updateMineralExtItem(food)
                    }
                }
            }
        }
    }

    private fun addVitaminItem(food: BFood){
        val v = food.vitamin!!
        v.save(object: SaveListener<String>() {
            override fun done(objectID: String?, e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("添加了维生素，objectID：$objectID")
                    v.objectId = objectID
                    addMineral(food)
                } else {
                    toast("${e.message} -- ${e.errorCode}")
                }
            }
        })
    }

    private fun addMineralExt(food: BFood){
        val mext = food.mineralExt!!
        mext.save(object: SaveListener<String>() {
            override fun done(objectID: String?, e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("添加了矿物质扩展，objectID：$objectID")
                    mext.objectId = objectID
                    addFoodItem(food)
                } else {
                    toast("${e.message}")
                    food.mineral?.delete(object: UpdateListener(){
                        override fun done(p0: BmobException?) {
                        }
                    })
                    food.vitamin?.delete(object: UpdateListener(){
                        override fun done(p0: BmobException?) {
                        }
                    })
                }
            }
        })
    }
    private fun addMineral(food: BFood){
        val m = food.mineral!!
        m.save(object: SaveListener<String>() {
            override fun done(objectID: String?, e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("添加了矿物资，objectID：$objectID")
                    m.objectId = objectID
                    addMineralExt(food)
                } else {
                    toast("${e.message}")
                    food.vitamin?.delete(object: UpdateListener(){
                        override fun done(p0: BmobException?) {
                        }
                    })
                }
            }
        })
    }

    private fun addFoodItem(food: BFood){
        food.category = shareSet.CurrentCategory
        food.save(object: SaveListener<String>() {
            override fun done(objectID: String?, e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("添加了食材，objectID：$objectID")
                    EventBus.getDefault().post(UpdateFoodRecyclerItem(food, EditerState.FoodAppend))  //Sticky
                } else {
                    toast("${e.message}")
                    food.vitamin?.delete(object: UpdateListener(){
                        override fun done(p0: BmobException?) {
                        }
                    })
                    food.mineral?.delete(object: UpdateListener(){
                        override fun done(p0: BmobException?) {
                        }
                    })
                    food.mineralExt?.delete(object: UpdateListener(){
                        override fun done(p0: BmobException?) {
                        }
                    })
                }
            }
        })
    }

    private fun updateMineralItem(food: BFood){
        val m = food.mineral!!
        m.update(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("更新了矿物质数据")
                } else {
                    toast("${e.message}")
                }
            }
        })
    }

    private fun updateMineralExtItem(food: BFood){
        val mext = food.mineralExt!!
        mext.update(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("更新了矿物质扩展数据")
                } else {
                    toast("${e.message}")
                }
            }
        })
    }

    private fun updateVitaminItem(food: BFood){
        val v = food.vitamin!!
        v.update(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("更新了维生素数据")
                } else {
                    toast("${e.message}")
                }
            }
        })
    }

    private fun updateFoodItem(food: BFood){
        food.update(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("更新了食材数据")
                    EventBus.getDefault().post(UpdateFoodRecyclerItem(shareSet.CurrentFood!!, EditerState.FoodEdit))  //Sticky
                } else {
                    toast("${e.message}")
                }
            }
        })
    }

    private fun alertDeleteFood(){

        alert("确实要删除食材 ${shareSet.CurrentFood?.name} 吗？", "删除食材") {
            positiveButton("确定") { deleteFoodFromBmob(shareSet.CurrentFood!!) }
            negativeButton("取消") {  }
        }.show()

    }

    private fun deleteFoodFromBmob(food: BFood){
        food.mineral?.delete(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("删除${food.name}矿物质数据成功")
                } else {
                    toast("${e.message}")
                }
            }
        })
        food.mineralExt?.delete(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("删除${food.name}矿物质扩展数据成功")
                } else {
                    toast("${e.message}")
                }
            }
        })
        food.vitamin?.delete(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("删除${food.name}维生素数据成功")
                } else {
                    toast("${e.message}")
                }
            }
        })
        food.delete(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    toast("删除${food.name}成功")
                    EventBus.getDefault().post(DeleteFoodRecyclerItem(shareSet.CurrentFood!!))
                } else {
                    toast("${e.message}")
                }
            }
        })
        onBackPressed()
    }


}
