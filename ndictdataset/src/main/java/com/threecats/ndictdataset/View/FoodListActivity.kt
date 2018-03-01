package com.threecats.ndictdataset.View

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
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
import com.threecats.ndictdataset.Bmob.BFoodVitamin
import com.threecats.ndictdataset.Enum.EditerState
import com.threecats.ndictdataset.EventClass.*
import com.threecats.ndictdataset.R

import kotlinx.android.synthetic.main.activity_food_list.*
import kotlinx.android.synthetic.main.content_food_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
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

    override fun onStart() {
        super.onStart()
        if (foodList == null) {
            //var currentCategory = BDM.ShareSet?.CurrentCategory
            val query: BmobQuery<BFood> = BmobQuery()
            //query.addWhereEqualTo("category", category?.objectId)
            query.addWhereEqualTo("category", BmobPointer(currentCategory))
            query.include("Vitamin")
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
                foodList?.add(BDM.ShareSet?.CurrentFood!!)
                val foodSize = foodList?.size!!
                FoodRView?.adapter?.notifyItemInserted(foodSize)
                BDM.ShareSet?.ItemEditState = EditerState.CategoryEdit
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
        checkElement.Foods.forEach {
            if (it.Vitamin == null) {
                nullVitamins.add(it)
            } else {
                if (it.Vitamin!!.objectId == null) nullVitamins.add(it)
            }

        }
        if (nullVitamins.size > 0) {
            val vitamins: MutableList<BmobObject> = arrayListOf()
            nullVitamins.forEach {
                it.Vitamin = BFoodVitamin().apply { Food = it }
                vitamins.add(it.Vitamin!!)
            }
            BmobBatch().insertBatch(vitamins).doBatch(object: QueryListListener<BatchResult>(){
                override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                    if (e == null) {
                        toast("补增了${results?.size}个维生素记录")
                        results?.forEachIndexed { i, batchResult -> nullVitamins[i].Vitamin?.objectId = batchResult.objectId }
                        EventBus.getDefault().post(BatchUpdateFood(nullVitamins))
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
                    toast("更新了${results?.size}个对维生素记录的引用")
                } else {
                    toast("${e.message}")
                }
            }
        })
    }

    private fun updateCategoryFoodSize(size: Int, showMessage: Boolean = false){
        val currentCategory = BDM.ShareSet?.CurrentCategory!!
        currentCategory.FoodTotal = size
        currentCategory.update(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    EventBus.getDefault().post(UpdateCategoryRecyclerItem(currentCategory, EditerState.CategoryEdit))
                    if (showMessage) {
                        Toast.makeText(this@FoodListActivity,
                                "更新${currentCategory.LongTitle}类的食材总数：$size ",
                                Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@FoodListActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}
