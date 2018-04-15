package com.threecats.ndictdataset.View

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import cn.bmob.v3.BmobBatch
import cn.bmob.v3.BmobObject
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BatchResult
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListListener
import cn.bmob.v3.listener.UpdateListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.*
import com.threecats.ndictdataset.Enum.EEditerState
import com.threecats.ndictdataset.EventClass.*
import com.threecats.ndictdataset.Helper.ErrorMessage
import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.Shells.RecyclerViewShell.*

import kotlinx.android.synthetic.main.activity_food_list.*
import kotlinx.android.synthetic.main.content_food_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class FoodListActivity : AppCompatActivity() {

    private val shareSet = BDM.ShareSet!!
    private lateinit var currentCategory: RecyclerViewItem<Any, BFoodCategory>
    private var foodListShell: RecyclerViewShell<Any, BFood>? = null

    init{
        if (shareSet.CurrentCategory == null) {
            onBackPressed()
        } else {
            currentCategory = shareSet.CurrentCategory!!
        }
    }

    //private var foodList: MutableList<BFood>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)
        setSupportActionBar(FoodToolbar)

        with (FoodToolbar){
            title = currentCategory.getObject()?.longTitle
            setNavigationOnClickListener { onBackPressed() }
        }

        fab.setOnClickListener {
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show()
            shareSet.createFood()
            val intent = Intent(this@FoodListActivity, FoodEditerActivity::class.java)
            startActivity(intent)
        }

        FoodRView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        if (foodListShell == null) {
            foodListShell = RecyclerViewShell(this)
        }

        foodListShell?.let {
            it.recyclerView(FoodRView).progressBar(progressBarFood).addViewType("item", ItemType.Item, R.layout.food_recycleritem)
            it.setDisplayItemListener(object : onDisplayItemListener<Any, BFood> {
                override fun onDisplayItem(item: RecyclerViewItem<Any, BFood>, holder: RecyclerViewAdapter<Any, BFood>.ItemViewHolder) {
                    val e = item.self
                    val updateTime = if (e.updatedAt == null) "" else e.updatedAt
                    holder.displayText(R.id.ItemName, if (e.alias.isEmpty()) e.name else "${e.name}、${e.alias}")
                    holder.displayText(R.id.ItemAlias, updateTime)
                }
            })
            it.setOnClickItemListener(object : onClickItemListener<Any, BFood> {
                override fun onClickItem(item: RecyclerViewItem<Any, BFood>, holder: RecyclerViewAdapter<Any, BFood>.ItemViewHolder) {
                    BDM.ShareSet?.editFood(item)
                    val intent = Intent(it.context, FoodEditerActivity::class.java)
                    startActivity(intent)
                }
            })
            it.setOnLongClickItemListener(object : onLongClickItemListener<Any, BFood> {
                override fun onLongClickItem(item: RecyclerViewItem<Any, BFood>, holder: RecyclerViewAdapter<Any, BFood>.ItemViewHolder) {
                    BDM.ShareSet?.editFood(item)
                    val intent = Intent(it.context, FoodEditerActivity::class.java)
                    startActivity(intent)
                }
            })
            it.setQueryDataListener(object : onQueryDatasListener<Any, BFood> {
                override fun onQueryDatas(shell: RecyclerViewShell<Any, BFood>) {
                    val query: BmobQuery<BFood> = BmobQuery()
                    query.addWhereEqualTo("category", BmobPointer(currentCategory.getObject()))
                    query.include("vitamin,mineral,mineralExt,article")
                    query.setLimit(300)
                    query.findObjects(object: FindListener<BFood>(){
                        override fun done(foods: MutableList<BFood>?, e: BmobException?) {
                            if (e == null) {
                                foods?.let {
                                    shell.addItems(it)
                                    shell.completeQuery()
                                    if (currentCategory.self.foodTotal != it.size) updateCategoryFoodSize(it.size, true)
//                                    foodList = it
//                                    FoodRView.adapter = FoodListAdapter(it, this@FoodListActivity)
                                    if (it.size > 0) EventBus.getDefault().post(CheckFoodTraceElement(it))
                                }

                            } else {
                                //toast("${e.message}")
                                ErrorMessage(this@FoodListActivity, e)
                            }
                        }
                    })
                }
            })
            it.setOnNullDataListener((object : onNullDataListener {
                override fun onNullData(isNull: Boolean) {
                    if (isNull) {
                        //toast("当前没有数据")
                    } else{
                        //toast("已经添加数据")
                    }
                }
            }))
            it.link()
        }

        EventBus.getDefault().register(this@FoodListActivity)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.foodlist_menu, menu!!)
        return super.onCreateOptionsMenu(menu)
    }

//    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
//        return super.onPrepareOptionsMenu(menu)
//    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.CheckFoodElement -> {
                toast("检查元素关联")
                checkFoodRelevant()
            }
            R.id.SaveAddItem -> {

            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this@FoodListActivity)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)  //, sticky = true
    fun doUpdateRecyclerItem(updateItem: UpdateFoodRecyclerItem){
        when (updateItem.State){
            EEditerState.FoodEdit -> {
                foodListShell?.updateItem(updateItem.Food)
            }
            EEditerState.FoodAppend -> {
                foodListShell?.addItem(updateItem.Food.self)
                updateCategoryFoodSize(foodListShell?.itemsSize()!!)
            }
            else -> toast("EditState Error !")
        }
        //EventBus.getDefault().removeStickyEvent(updateItem)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)  //, sticky = true
    fun doDeleteFoodRecyclerItem(deleteItem: DeleteFoodRecyclerItem){
        foodListShell?.let {
            it.removeItem(deleteItem.Food)
            updateCategoryFoodSize(it.itemsSize())
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doCheckFoodTraceElement(checkElement: CheckFoodTraceElement){

        val nullVitamins: MutableList<BFood> = arrayListOf()
        val nullMinerals: MutableList<BFood> = arrayListOf()
        val nullMineralexts: MutableList<BFood> = arrayListOf()

        checkElement.Foods.forEach {

            if (it.vitamin == null) {
                nullVitamins.add(it)
            } else {
                if (it.vitamin!!.objectId == ""){
                    it.vitamin = null
                    nullVitamins.add(it)
                }
            }

            if (it.mineral == null) {
                nullMinerals.add(it)
            } else {
                if (it.mineral!!.objectId == ""){
                    it.mineral = null
                    nullMinerals.add(it)
                }
            }

            if (it.mineralExt == null) {
                nullMineralexts.add(it)
            } else {
                if (it.mineralExt!!.objectId == ""){
                    it.mineralExt = null
                    nullMineralexts.add(it)
                }
            }
        }

        if (nullVitamins.size > 0) {
            val vitamins: MutableList<BmobObject> = arrayListOf()

            nullVitamins.forEach {
                it.vitamin = BFoodVitamin()
                vitamins.add(it.vitamin!!)
            }
            BmobBatch().insertBatch(vitamins).doBatch(object: QueryListListener<BatchResult>(){
                override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                    if (e == null) {
                        toast("补增了 ${results?.size} 个维生素记录")
                        results?.forEachIndexed { i, batchResult -> nullVitamins[i].vitamin?.objectId = batchResult.objectId }
                        EventBus.getDefault().post(BatchUpdateFood(nullVitamins, "维生素"))
                    } else {
                        //toast("${e.message}")
                        ErrorMessage(this@FoodListActivity, e)
                    }
                }
            })
        }

        if (nullMinerals.size > 0) {
            val minerals: MutableList<BmobObject> = arrayListOf()

            nullMinerals.forEach {
                it.mineral = BFoodMineral()
                minerals.add(it.mineral!!)
            }

            BmobBatch().insertBatch(minerals).doBatch(object: QueryListListener<BatchResult>(){
                override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                    if (e == null) {
                        toast("补增了 ${results?.size} 个矿物质记录")
                        results?.forEachIndexed { i, batchResult -> nullMinerals[i].mineral?.objectId = batchResult.objectId }
                        EventBus.getDefault().post(BatchUpdateFood(nullMinerals, "矿物资"))
                    } else {
                        //toast("${e.message}")
                        ErrorMessage(this@FoodListActivity, e)
                    }
                }
            })
        }

        if (nullMineralexts.size > 0) {
            val mineralexts: MutableList<BmobObject> = arrayListOf()

            nullMineralexts.forEach {
                it.mineralExt = BFoodMineralExt()
                mineralexts.add(it.mineralExt!!)
            }

            BmobBatch().insertBatch(mineralexts).doBatch(object: QueryListListener<BatchResult>(){
                override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                    if (e == null) {
                        toast("补增了 ${results?.size} 个矿物资扩展记录")
                        results?.forEachIndexed { i, batchResult -> nullMineralexts[i].mineralExt?.objectId = batchResult.objectId }
                        EventBus.getDefault().post(BatchUpdateFood(nullMineralexts, "矿物质扩展"))
                    } else {
                        //toast("${e.message}")
                        ErrorMessage(this@FoodListActivity, e)
                    }
                }
            })
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doBatchUpdateFood(updateItems: BatchUpdateFood){
        val batchFoods: MutableList<BmobObject> = arrayListOf()
        updateItems.Foods.forEach { batchFoods.add(it) }
        BmobBatch().updateBatch(batchFoods).doBatch(object: QueryListListener<BatchResult>(){
            override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                if (e == null) {
                    toast("更新了 ${results?.size} 个对 ${updateItems.Title} 记录的引用")
                } else {
                    //toast("${e.message}")
                    ErrorMessage(this@FoodListActivity, e)
                }
            }
        })
    }

    // 已建立DataModel代码
    private fun updateCategoryFoodSize(size: Int, showMessage: Boolean = false){
        currentCategory.self.foodTotal = size
        currentCategory.self.update(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    EventBus.getDefault().post(UpdateCategoryRecyclerItem(currentCategory, EEditerState.CategoryEdit))
                    if (showMessage) {
                        toast("更新 ${currentCategory.getObject()?.longTitle} 类的食材总数：$size ")
                    }
                } else {
                    //toast("${e.message}")
                    ErrorMessage(this@FoodListActivity, e)
                }
            }
        })
    }

    // 已建立DataModel代码
    private fun checkFoodRelevant(){

        val logShow = AnkoLogger("NDIC")

        val vitamins: MutableList<BmobObject> = arrayListOf()
        val minerals: MutableList<BmobObject> = arrayListOf()
        val mineralexts: MutableList<BmobObject> = arrayListOf()

        foodListShell?.recyclerViewItems?.forEach {
            val vitamin: BFoodVitamin = it.self.vitamin!!
            val mineral: BFoodMineral = it.self.mineral!!
            val mineralExt: BFoodMineralExt = it.self.mineralExt!!
            if (vitamin.foodID == null) {
                vitamin.foodID = it.self.objectId
                vitamins.add(vitamin)
                logShow.info { "${it.self.name} vitamin: ${vitamin.objectId} -> ${vitamin.foodID}" }
            }
            if (mineral.foodID == null) {
                mineral.foodID = it.self.objectId
                minerals.add(mineral)
                logShow.info { "${it.self.name} mineral: ${mineral.objectId} -> ${mineral.foodID}" }
            }
            if (mineralExt.foodID == null) {
                mineralExt.foodID = it.self.objectId
                mineralexts.add(mineralExt)
                logShow.info { "${it.self.name} mineralExt: ${mineralExt.objectId} -> ${mineralExt.foodID}" }
            }
        }
        toast("${vitamins.size} / ${minerals.size} / ${mineralexts.size}")
        if (vitamins.size > 0) {
            BmobBatch().updateBatch(vitamins).doBatch(object: QueryListListener<BatchResult>(){
                override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                    if (e == null) {
                        toast("补增了${results?.size}个矿物资扩展记录")
                    } else {
                        //toast("${e.message}")
                        ErrorMessage(this@FoodListActivity, e)
                    }
                }
            })
        }
        if (minerals.size > 0) {
            BmobBatch().updateBatch(minerals).doBatch(object: QueryListListener<BatchResult>(){
                override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                    if (e == null) {
                        toast("补增了${results?.size}个矿物质记录")
                    } else {
                        //toast("${e.message}")
                        ErrorMessage(this@FoodListActivity, e)
                    }
                }
            })
        }
        if (mineralexts.size > 0) {
            BmobBatch().updateBatch(mineralexts).doBatch(object: QueryListListener<BatchResult>(){
                override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                    if (e == null) {
                        toast("补增了${results?.size}个矿物资扩展记录")
                    } else {
                        //toast("${e.message}")
                        ErrorMessage(this@FoodListActivity, e)
                    }
                }
            })
        }
    }
}
