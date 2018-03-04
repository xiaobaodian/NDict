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
import com.threecats.ndictdataset.Bmob.BFoodCategory
import com.threecats.ndictdataset.BuildConfig
import com.threecats.ndictdataset.Enum.ChangeBlock
import com.threecats.ndictdataset.Enum.EditerState
import com.threecats.ndictdataset.EventClass.DeleteFoodRecyclerItem
import com.threecats.ndictdataset.EventClass.UpdateFoodRecyclerItem
import com.threecats.ndictdataset.FoodFragment.*
import com.threecats.ndictdataset.R
import kotlinx.android.synthetic.main.activity_food_editer.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class FoodEditerActivity : AppCompatActivity() {

    lateinit var  currentCategory: BFoodCategory
    lateinit var  currentFood: BFood
    lateinit var  editerState: EditerState

    val foodPropertyFragments = mutableListOf<FoodPropertyFragment>()
    var currentFragment: FoodPropertyFragment? = null
    val changBlockList: MutableList<ChangeBlock> = arrayListOf()

    init {
        initShareVar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_editer)
        setSupportActionBar(FoodEditerToolbar)
        FoodEditerToolbar.setNavigationOnClickListener { onBackPressed() }  //FoodNutrientFragment

        addFragments(FoodNameFragment(),"名称")
        addFragments(FoodNoteFragment(),"描述")
        addFragments(FoodNutrientFragment(),"营养素")
        addFragments(FoodVitaminFragment(),"维生素")
        addFragments(FoodMineralFragment(),"矿物质")
        addFragments(FoodPictureFragment(),"图片")

        currentFragment = foodPropertyFragments[0]

        FoodEditerViewPage.adapter = FoodEditerGroupAdapter(supportFragmentManager, foodPropertyFragments)
        FoodPropertyTabs.setupWithViewPager(FoodEditerViewPage)
        FoodPropertyTabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { currentFragment = foodPropertyFragments[tab!!.position] }
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
        when (editerState){
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
                foodPropertyFragments.forEach { it.ExportFields(currentFood) }
                processFood(currentFood)
                BDM.ShareSet?.createFoodItem()
                initShareVar()
                foodPropertyFragments.forEach { it.initShareVar() }
                currentFragment?.ImportFields(currentFood)
                //foodPropertyFragments.forEach { it.ImportFields(BDM.ShareSet?.CurrentFood!!) }
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        changBlockList.clear()
        foodPropertyFragments.forEach { it.BlockChangeState(this) }
        if (changBlockList.size > 0) processFood(currentFood)
    }

    fun addChangeBlock(changeBlock: ChangeBlock){
        val position = changBlockList.indexOf(changeBlock)
        if (position < 0) {
            changBlockList.add(changeBlock)
        }
    }

    private fun initShareVar(){
        currentCategory = BDM.ShareSet?.CurrentCategory?: BFoodCategory()
        currentFood = BDM.ShareSet!!.CurrentFood?: BFood()
        editerState = BDM.ShareSet?.ItemEditState?: EditerState.FoodEdit
    }

    private fun addFragments(fragment: FoodPropertyFragment, name: String){
        val bundle = Bundle()
        bundle.putString("name", name)
        fragment.setArguments(bundle)
        foodPropertyFragments.add(fragment)

        if (BuildConfig.DEBUG) {
            val logshow = AnkoLogger("NDIC")
            logshow.info { "设置Fragment参数传递" }
        }
    }

    private fun processFood(food: BFood){

        when (editerState){
            EditerState.FoodAppend -> {
                // 加入数据需要在Bmob段建立关联，需要加入维生素、矿物质数据后获取返回的objectId，所以
                // 采用链式调用
                if (food.name.isEmpty() || food.name.isBlank()) {
                    onBackPressed()
                }
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
        val v = food.Vitamin!!
        v.save(object: SaveListener<String>() {
            override fun done(objectID: String?, e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("添加了维生素，objectID：$objectID")
                    v.objectId = objectID
                    addMineral(food)
                } else {
                    toast("${e.message}")
                }
            }
        })
    }

    private fun addMineralExt(food: BFood){
        val mext = food.MineralExt!!
        mext.save(object: SaveListener<String>() {
            override fun done(objectID: String?, e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("添加了矿物质扩展，objectID：$objectID")
                    mext.objectId = objectID
                    addFoodItem(food)
                } else {
                    toast("${e.message}")
                    food.Mineral?.delete(object: UpdateListener(){
                        override fun done(p0: BmobException?) {
                        }
                    })
                    food.Vitamin?.delete(object: UpdateListener(){
                        override fun done(p0: BmobException?) {
                        }
                    })
                }
            }
        })
    }
    private fun addMineral(food: BFood){
        val m = food.Mineral!!
        m.save(object: SaveListener<String>() {
            override fun done(objectID: String?, e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("添加了矿物资，objectID：$objectID")
                    m.objectId = objectID
                    addMineralExt(food)
                } else {
                    toast("${e.message}")
                    food.Vitamin?.delete(object: UpdateListener(){
                        override fun done(p0: BmobException?) {
                        }
                    })
                }
            }
        })
    }

    private fun addFoodItem(food: BFood){
        food.category = currentCategory
        food.save(object: SaveListener<String>() {
            override fun done(objectID: String?, e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("添加了食材，objectID：$objectID")
                    EventBus.getDefault().post(UpdateFoodRecyclerItem(food, EditerState.FoodAppend))  //Sticky
                } else {
                    toast("${e.message}")
                    food.Vitamin?.delete(object: UpdateListener(){
                        override fun done(p0: BmobException?) {
                        }
                    })
                    food.Mineral?.delete(object: UpdateListener(){
                        override fun done(p0: BmobException?) {
                        }
                    })
                    food.MineralExt?.delete(object: UpdateListener(){
                        override fun done(p0: BmobException?) {
                        }
                    })
                }
            }
        })
    }

    private fun updateMineralItem(food: BFood){
        val m = food.Mineral!!
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
        val mext = food.MineralExt!!
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
        val v = food.Vitamin!!
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
                    EventBus.getDefault().post(UpdateFoodRecyclerItem(currentFood, EditerState.FoodEdit))  //Sticky
                } else {
                    toast("${e.message}")
                }
            }
        })
    }

    private fun alertDeleteFood(){

        alert("确实要删除食材 ${currentFood.name} 吗？", "删除食材") {
            positiveButton("确定") { deleteFoodFromBmob(currentFood) }
            negativeButton("取消") {  }
        }.show()

    }

    private fun deleteFoodFromBmob(food: BFood){
        food.Mineral?.delete(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("删除${food.name}矿物质数据成功")
                } else {
                    toast("${e.message}")
                }
            }
        })
        food.MineralExt?.delete(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    if (BDM.ShowTips) toast("删除${food.name}矿物质扩展数据成功")
                } else {
                    toast("${e.message}")
                }
            }
        })
        food.Vitamin?.delete(object: UpdateListener(){
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
                    EventBus.getDefault().post(DeleteFoodRecyclerItem(currentFood))
                    onBackPressed()
                } else {
                    toast("${e.message}")
                }
            }
        })
    }


}
