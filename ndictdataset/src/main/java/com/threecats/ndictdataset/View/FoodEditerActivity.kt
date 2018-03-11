package com.threecats.ndictdataset.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
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
                    FoodEditerToolbar.subtitle = ""
                } else {
                    FoodEditerToolbar.title = shareSet.CurrentFood?.name
                    FoodEditerToolbar.subtitle = "每100克中的含量"
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
                shareSet.CurrentFood?.let { alertDeleteFood(it) }
            }
            R.id.SaveAddItem -> {
                shareSet.CurrentFood?.let {
                    val food = it
                    foodPropertyFragments.forEach { it.ExportFields(food) }
                    processFood(food)
                }
                shareSet.createFood()
                shareSet.CurrentFood?.let {
                    val food = it
                    foodPropertyFragments.forEach { it.ImportFields(food) }
                    foodPropertyFragments.forEach { it.FirstEditTextFocus() }
                }
                FoodPropertyTabs.getTabAt(0)?.select()
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (shareSet.ItemEditState == EditerState.FoodAppend) {
            foodPropertyFragments.forEach { it.ExportFields(shareSet.CurrentFood!!) }
            shareSet.CurrentFood?.let { processFood(it) }
        } else {
            changBlockList.clear()
            foodPropertyFragments.forEach { it.BlockChangeState(this) }
            if (changBlockList.size > 0) {
                foodPropertyFragments.forEach { it.ExportFields(shareSet.CurrentFood!!) }
                shareSet.CurrentFood?.let { processFood(it) }
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
                addBatchFoodExtToBmob(food)
            }
            EditerState.FoodEdit -> {
                changBlockList.forEach {
                    when (it) {
                        ChangeBlock.Food -> updateFoodToBmob(food)
                        ChangeBlock.Vitamin -> updateVitaminToBmob(food)
                        ChangeBlock.Mineral -> updateMineralToBmob(food)
                        ChangeBlock.MineralExt -> updateMineralExtToBmob(food)
                    }
                }
            }
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
        var items: MutableList<BmobObject> = arrayListOf()
        food.vitamin?.let { items.add(it) }
        food.mineral?.let { items.add(it) }
        food.mineralExt?.let { items.add(it) }
        BmobBatch().insertBatch(items).doBatch(object: QueryListListener<BatchResult>(){
            override fun done(p0: MutableList<BatchResult>?, p1: BmobException?) {
                if (p1 == null) {
                    if (p0?.get(0)?.error == null) food.vitamin?.objectId = p0?.get(0)?.objectId
                    if (p0?.get(1)?.error == null) food.mineral?.objectId = p0?.get(1)?.objectId
                    if (p0?.get(2)?.error == null) food.mineralExt?.objectId = p0?.get(2)?.objectId
                    addFoodToBmob(food)
                } else {
                    longToast("添加食材扩展数据出现错误：${p1.message}")
                }
            }
        })
    }

    private fun addFoodToBmob(food: BFood){
        food.category = shareSet.CurrentCategory
        food.save(object: SaveListener<String>() {
            override fun done(objectID: String?, e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("添加了食材，objectID：$objectID")
                    EventBus.getDefault().post(UpdateFoodRecyclerItem(food, EditerState.FoodAppend))  //Sticky
                } else {
                    longToast("添加食材${food.name}出现错误。错误信息：${e.message}")
                }
            }
        })
    }

    private fun updateMineralToBmob(food: BFood){
        val m = food.mineral!!
        m.update(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("更新了矿物质数据")
                } else {
                    longToast("更新矿物质数据出现错误：${e.message}")
                }
            }
        })
    }

    private fun updateMineralExtToBmob(food: BFood){
        val mext = food.mineralExt!!
        mext.update(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("更新了矿物质扩展数据")
                } else {
                    longToast("更新矿物质扩展数据出现错误：${e.message}")
                }
            }
        })
    }

    private fun updateVitaminToBmob(food: BFood){
        val v = food.vitamin!!
        v.update(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("更新了维生素数据")
                } else {
                    longToast("更新维生素数据出现错误：${e.message}")
                }
            }
        })
    }

    private fun updateFoodToBmob(food: BFood){
        food.update(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("更新了食材数据")
                    EventBus.getDefault().post(UpdateFoodRecyclerItem(food, EditerState.FoodEdit))
                } else {
                    longToast("更新食材数据出现错误：${e.message}")
                }
            }
        })
    }

    private fun updateBatchFoodToBmob(food: BFood){
        var items: MutableList<BmobObject> = arrayListOf()
        food.vitamin?.let { items.add(it) }
        food.mineral?.let { items.add(it) }
        food.mineralExt?.let { items.add(it) }
        items.add(food)
        BmobBatch().updateBatch(items).doBatch(object: QueryListListener<BatchResult>(){
            override fun done(p0: MutableList<BatchResult>?, p1: BmobException?) {
                if (p1 == null) {
                    if (BDM.ShowTips) toast("更新了食材数据")
                    EventBus.getDefault().post(UpdateFoodRecyclerItem(food, EditerState.FoodEdit))  //Sticky
                } else {
                    longToast("更新了食材数据出现错误：${p1.message}")
                }
            }
        })
    }

    private fun alertDeleteFood(food: BFood){

        alert("确实要删除食材 ${shareSet.CurrentFood?.name} 吗？", "删除食材") {
            positiveButton("确定") { deleteFoodFromBmob(food) }
            negativeButton("取消") {  }
        }.show()

    }

    private fun deleteFoodFrom(food: BFood){

        var number = 3

        while (food.mineral != null || food.mineralExt != null || food.vitamin != null){

            if (number < 0) break

            food.mineral?.let {
                it.delete(object: UpdateListener(){
                    override fun done(e: BmobException?) {
                        if (e == null) {
                            if (BDM.ShowTips) toast("删除${food.name}矿物质数据成功")
                            food.mineral = null
                        } else {
                            if (e.errorCode == 9006) {
                                food.mineral = null
                            } else {
                                longToast("删除${food.name}矿物质数据出现错误：${e.message}")
                                number -= 1
                            }
                        }
                    }
                })
            }
            food.mineralExt?.let {
                it?.delete(object: UpdateListener(){
                    override fun done(e: BmobException?) {
                        if (e == null) {
                            if (BDM.ShowTips) toast("删除${food.name}矿物质扩展数据成功")
                            food.mineralExt = null
                        } else {
                            if (e.errorCode == 9006) {
                                food.mineral = null
                            } else {
                                longToast("删除${food.name}矿物质扩展数据出现错误：${e.message}")
                                number -= 1
                            }
                        }
                    }
                })

            }

            food.vitamin?.let {
                it.delete(object: UpdateListener(){
                    override fun done(e: BmobException?) {
                        if (e == null) {
                            if (BDM.ShowTips) toast("删除${food.name}维生素数据成功")
                            food.vitamin = null
                        } else {
                            if (e.errorCode == 9006) {
                                food.mineral = null
                            } else {
                                longToast("删除${food.name}维生素数据出现错误：${e.message}")
                                number -= 1
                            }
                        }
                    }
                })
            }
        }

        if (food.vitamin == null && food.mineral == null && food.mineralExt == null) {
            food.delete(object: UpdateListener(){
                override fun done(e: BmobException?) {
                    if (e == null) {
                        toast("删除${food.name}成功")
                        EventBus.getDefault().post(DeleteFoodRecyclerItem(shareSet.CurrentFood!!))
                    } else {
                        longToast("删除${food.name}出现错误：${e.message}")
                    }
                }
            })
        } else {
            food.update()
            longToast("删除${food.name}的扩展属性出现错误，终止删除。数据不完整！")
        }
        onBackPressed()
    }

    private fun deleteFoodBmob(food: BFood){

        food.mineral?.let {
            it.delete(object: UpdateListener(){
                override fun done(e: BmobException?) {
                    if (e == null) {
                        if (BDM.ShowTips) toast("删除${food.name}矿物质数据成功")
                        food.mineral = null
                    } else {
                        if (e.errorCode == 9006) {
                            food.mineral = null
                        } else {
                            longToast("删除${food.name}矿物质数据出现错误：${e.message}")
                        }
                    }
                }
            })
        }
        food.mineralExt?.let {
            it?.delete(object: UpdateListener(){
                override fun done(e: BmobException?) {
                    if (e == null) {
                        if (BDM.ShowTips) toast("删除${food.name}矿物质扩展数据成功")
                        food.mineralExt = null
                    } else {
                        if (e.errorCode == 9006) {
                            food.mineral = null
                        } else {
                            longToast("删除${food.name}矿物质扩展数据出现错误：${e.message}")
                        }
                    }
                }
            })

        }

        food.vitamin?.let {
            it.delete(object: UpdateListener(){
                override fun done(e: BmobException?) {
                    if (e == null) {
                        if (BDM.ShowTips) toast("删除${food.name}维生素数据成功")
                        food.vitamin = null
                    } else {
                        if (e.errorCode == 9006) {
                            food.mineral = null
                        } else {
                            longToast("删除${food.name}维生素数据出现错误：${e.message}")
                        }
                    }
                }
            })
        }

        if (food.vitamin == null && food.mineral == null && food.mineralExt == null) {
            food.delete(object: UpdateListener(){
                override fun done(e: BmobException?) {
                    if (e == null) {
                        toast("删除${food.name}成功")
                        EventBus.getDefault().post(DeleteFoodRecyclerItem(shareSet.CurrentFood!!))
                    } else {
                        longToast("删除${food.name}出现错误：${e.message}")
                    }
                }
            })
        } else {
            food.update()
            longToast("删除${food.name}的扩展属性出现错误，终止删除。数据不完整！")
        }
        onBackPressed()
    }

    private fun deleteFoodFromBmob(food: BFood){
        var items: MutableList<BmobObject> = arrayListOf()
        food.vitamin?.let { items.add(it) }
        food.mineral?.let { items.add(it) }
        food.mineralExt?.let { items.add(it) }
        items.add(food)
        BmobBatch().deleteBatch(items).doBatch(object: QueryListListener<BatchResult>() {
            override fun done(p0: MutableList<BatchResult>?, p1: BmobException?) {
                if (p1 == null) {
                    toast("批量模式删除食材${food.name}成功")
                    EventBus.getDefault().post(DeleteFoodRecyclerItem(food))
                } else {
                    toast("批量模式删除食材${food.name}失败")
                }
            }
        })
        onBackPressed()
    }


}
