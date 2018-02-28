package com.threecats.ndictdataset.View

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.UpdateListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Enum.EditerState
import com.threecats.ndictdataset.EventClass.DeleteFoodRecyclerItem
import com.threecats.ndictdataset.EventClass.UpdateCategoryRecyclerItem
import com.threecats.ndictdataset.EventClass.UpdateFoodRecyclerItem
import com.threecats.ndictdataset.R

import kotlinx.android.synthetic.main.activity_food_list.*
import kotlinx.android.synthetic.main.content_food_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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
//        FoodToolbar.title = currentCategory.LongTitle
//        FoodToolbar.subtitle = "食材列表"
//        FoodToolbar.setNavigationOnClickListener { onBackPressed() }

        fab.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show()
            shareSet.createFoodItem()
//            BDM.ShareSet?.ItemEditState = EditerState.FoodAppend
//            BDM.ShareSet?.CurrentFood = BFood()
//            BDM.ShareSet?.CurrentVitamin = BFoodVitamin()
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
            var query: BmobQuery<BFood> = BmobQuery()
            //query.addWhereEqualTo("category", category?.objectId)
            query.addWhereEqualTo("category", BmobPointer(currentCategory))
            query.setLimit(300)
            query.findObjects(object: FindListener<BFood>(){
                override fun done(foods: MutableList<BFood>?, e: BmobException?) {
                    if (e == null) {
                        if (currentCategory.FoodTotal != foods!!.size) {
                            updateCategoryFoodSize(foods!!.size, true)
                        }
                        foodList = foods
                        FoodRView.adapter = FoodListAdapter(foodList!!, this@FoodListActivity)
                    } else {
                        Toast.makeText(this@FoodListActivity, e.message, Toast.LENGTH_SHORT).show()
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

    private fun updateCategoryFoodSize(size: Int, showMessage: Boolean = false){
        val currentCategory = BDM.ShareSet?.CurrentCategory!!
        currentCategory.FoodTotal = size
        currentCategory.update(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    EventBus.getDefault().post(UpdateCategoryRecyclerItem(currentCategory, EditerState.CategoryEdit))
                    if (showMessage) {
                        Toast.makeText(this@FoodListActivity,
                                "更新${currentCategory?.LongTitle}类的食材总数：$size ",
                                Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@FoodListActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}
