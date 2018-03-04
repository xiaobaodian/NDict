package com.threecats.ndictdataset.View

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Bmob.BFoodMineral
import com.threecats.ndictdataset.Bmob.BFoodMineralExt
import com.threecats.ndictdataset.Bmob.BFoodVitamin
import com.threecats.ndictdataset.Enum.EditerState
import com.threecats.ndictdataset.EventClass.*
import com.threecats.ndictdataset.R

import kotlinx.android.synthetic.main.activity_food_list.*
import kotlinx.android.synthetic.main.content_food_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class FoodListActivity : AppCompatActivity() {

    val shareSet = BDM.ShareSet!!
    val currentCategory = shareSet.CurrentCategory!!

    private var foodList: MutableList<BFood>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)
        setSupportActionBar(FoodToolbar)

        with (FoodToolbar){
            title = currentCategory.LongTitle
            subtitle = "食材列表"
            setNavigationOnClickListener { onBackPressed() }
        }

        fab.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show()
            shareSet.createFoodItem()
            val intent = Intent(this@FoodListActivity, FoodEditerActivity::class.java)
            startActivity(intent)
        }

        FoodRView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        EventBus.getDefault().register(this@FoodListActivity)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.foodlist_menu, menu!!)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

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

    override fun onStart() {
        super.onStart()
        if (foodList == null) {
            val query: BmobQuery<BFood> = BmobQuery()
            query.addWhereEqualTo("category", BmobPointer(currentCategory))
            query.include("Vitamin,Mineral,MineralExt")
            query.setLimit(300)
            query.findObjects(object: FindListener<BFood>(){
                override fun done(foods: MutableList<BFood>?, e: BmobException?) {
                    if (e == null) {
                        if (currentCategory.FoodTotal != foods!!.size) {
                            updateCategoryFoodSize(foods.size, true)
                        }
                        foodList = foods
                        FoodRView.adapter = FoodListAdapter(foodList!!, this@FoodListActivity)
                        if (foodList!!.size > 0) EventBus.getDefault().post(CheckFoodTraceElement(foodList!!))
                    } else {
                        toast("${e.message}")
                    }
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this@FoodListActivity)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)  //, sticky = true
    fun doUpdateRecyclerItem(updateItem: UpdateFoodRecyclerItem){
        val position = foodList?.indexOf(updateItem.Food)
        when (updateItem.State){
            EditerState.FoodEdit -> FoodRView?.adapter?.notifyItemChanged(position!!)
            EditerState.FoodAppend -> {
                foodList?.add(updateItem.Food)
                val foodSize = foodList?.size!!
                FoodRView?.adapter?.notifyItemInserted(foodSize)
                BDM.ShareSet?.ItemEditState = EditerState.CategoryEdit  // ??? 应该可以去掉
                updateCategoryFoodSize(foodSize)
            }
        }
        //EventBus.getDefault().removeStickyEvent(updateItem)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)  //, sticky = true
    fun doDeleteFoodRecyclerItem(deleteItem: DeleteFoodRecyclerItem){
        val position = foodList?.indexOf(BDM.ShareSet?.CurrentFood)
        foodList?.removeAt(position!!)
        FoodRView?.adapter?.notifyItemRemoved(position!!)
        updateCategoryFoodSize(foodList!!.size)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doCheckFoodTraceElement(checkElement: CheckFoodTraceElement){

        val nullVitamins: MutableList<BFood> = arrayListOf()
        val nullMinerals: MutableList<BFood> = arrayListOf()
        val nullMineralexts: MutableList<BFood> = arrayListOf()

        checkElement.Foods.forEach {

            if (it.Vitamin == null) {
                nullVitamins.add(it)
            } else {
                if (it.Vitamin!!.objectId == ""){
                    it.Vitamin = null
                    nullVitamins.add(it)
                }
            }

            if (it.Mineral == null) {
                nullMinerals.add(it)
            } else {
                if (it.Mineral!!.objectId == ""){
                    it.Mineral = null
                    nullMinerals.add(it)
                }
            }

            if (it.MineralExt == null) {
                nullMineralexts.add(it)
            } else {
                if (it.MineralExt!!.objectId == ""){
                    it.MineralExt = null
                    nullMineralexts.add(it)
                }
            }
        }

        if (nullVitamins.size > 0) {
            val vitamins: MutableList<BmobObject> = arrayListOf()

            nullVitamins.forEach {
                it.Vitamin = BFoodVitamin()
                vitamins.add(it.Vitamin!!)
            }
            BmobBatch().insertBatch(vitamins).doBatch(object: QueryListListener<BatchResult>(){
                override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                    if (e == null) {
                        toast("补增了${results?.size}个维生素记录")
                        results?.forEachIndexed { i, batchResult -> nullVitamins[i].Vitamin?.objectId = batchResult.objectId }
                        EventBus.getDefault().post(BatchUpdateFood(nullVitamins, "维生素"))
                    } else {
                        toast("${e.message}")
                    }
                }
            })
        }

        if (nullMinerals.size > 0) {
            val minerals: MutableList<BmobObject> = arrayListOf()

            nullMinerals.forEach {
                it.Mineral = BFoodMineral()
                minerals.add(it.Mineral!!)
            }

            BmobBatch().insertBatch(minerals).doBatch(object: QueryListListener<BatchResult>(){
                override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                    if (e == null) {
                        toast("补增了${results?.size}个矿物质记录")
                        results?.forEachIndexed { i, batchResult -> nullMinerals[i].Mineral?.objectId = batchResult.objectId }
                        EventBus.getDefault().post(BatchUpdateFood(nullMinerals, "矿物资"))
                    } else {
                        toast("${e.message}")
                    }
                }
            })
        }

        if (nullMineralexts.size > 0) {
            val mineralexts: MutableList<BmobObject> = arrayListOf()

            nullMineralexts.forEach {
                it.MineralExt = BFoodMineralExt()
                mineralexts.add(it.MineralExt!!)
            }

            BmobBatch().insertBatch(mineralexts).doBatch(object: QueryListListener<BatchResult>(){
                override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                    if (e == null) {
                        toast("补增了${results?.size}个矿物资扩展记录")
                        results?.forEachIndexed { i, batchResult -> nullMineralexts[i].MineralExt?.objectId = batchResult.objectId }
                        EventBus.getDefault().post(BatchUpdateFood(nullMineralexts, "矿物质扩展"))
                    } else {
                        toast("${e.message}")
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
                    toast("更新了${results?.size}个对${updateItems.Title}记录的引用")
                } else {
                    toast("${e.message}")
                }
            }
        })
    }

    private fun updateCategoryFoodSize(size: Int, showMessage: Boolean = false){
        currentCategory.FoodTotal = size
        currentCategory.update(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    EventBus.getDefault().post(UpdateCategoryRecyclerItem(currentCategory, EditerState.CategoryEdit))
                    if (showMessage) {
                        toast("更新${currentCategory.LongTitle}类的食材总数：$size ")
                    }
                } else {
                    toast("${e.message}")
                }
            }
        })
    }

    private fun checkFoodRelevant(){

        val logshow = AnkoLogger("NDIC")

        val Vitamins: MutableList<BmobObject> = arrayListOf()
        val Minerals: MutableList<BmobObject> = arrayListOf()
        val Mineralexts: MutableList<BmobObject> = arrayListOf()

        foodList?.forEach {
            val vitamin: BFoodVitamin = it.Vitamin!!
            val mineral: BFoodMineral = it.Mineral!!
            val mineralExt: BFoodMineralExt = it.MineralExt!!
            if (vitamin.FoodID == null) {
                vitamin.FoodID = it.objectId
                Vitamins.add(vitamin)
                logshow.info { "${it.name} Vitamin: ${vitamin.objectId} -> ${vitamin.FoodID}" }
            }
            if (mineral.FoodID == null) {
                mineral.FoodID = it.objectId
                Minerals.add(mineral)
                logshow.info { "${it.name} Mineral: ${mineral.objectId} -> ${mineral.FoodID}" }
            }
            if (mineralExt.FoodID == null) {
                mineralExt.FoodID = it.objectId
                Mineralexts.add(mineralExt)
                logshow.info { "${it.name} MineralExt: ${mineralExt.objectId} -> ${mineralExt.FoodID}" }
            }
        }
        toast("${Vitamins.size} / ${Minerals.size} / ${Mineralexts.size}")
        if (Vitamins.size > 0) {
            BmobBatch().updateBatch(Vitamins).doBatch(object: QueryListListener<BatchResult>(){
                override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                    if (e == null) {
                        toast("补增了${results?.size}个矿物资扩展记录")
                    } else {
                        toast("${e.message}")
                    }
                }
            })
        }
        if (Minerals.size > 0) {
            BmobBatch().updateBatch(Minerals).doBatch(object: QueryListListener<BatchResult>(){
                override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                    if (e == null) {
                        toast("补增了${results?.size}个矿物质记录")
                    } else {
                        toast("${e.message}")
                    }
                }
            })
        }
        if (Mineralexts.size > 0) {
            BmobBatch().updateBatch(Mineralexts).doBatch(object: QueryListListener<BatchResult>(){
                override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                    if (e == null) {
                        toast("补增了${results?.size}个矿物资扩展记录")
                    } else {
                        toast("${e.message}")
                    }
                }
            })
        }

    }

}
