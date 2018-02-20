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

    private var foodList: MutableList<BFood>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)
        setSupportActionBar(FoodToolbar)

        FoodToolbar.title = BDM.ShareSet?.CurrentCategory?.LongTitle
        FoodToolbar.subtitle = "食材列表"
        FoodToolbar.setNavigationOnClickListener { onBackPressed() }

        fab.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show()
            BDM.ShareSet?.ItemEditState = EditerState.FoodAppend
            BDM.ShareSet?.CurrentFood = BFood()
            val intent = Intent(this@FoodListActivity, FoodEditerActivity::class.java)
            startActivity(intent)
        }

        FoodRView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        EventBus.getDefault().register(this@FoodListActivity)
    }

    override fun onStart() {
        super.onStart()
        if (foodList == null) {
            var currentCategory = BDM.ShareSet?.CurrentCategory
            var query: BmobQuery<BFood> = BmobQuery()
            //query.addWhereEqualTo("category", category?.objectId)
            query.addWhereEqualTo("category", BmobPointer(currentCategory))
            query.setLimit(300)
            query.findObjects(object: FindListener<BFood>(){
                override fun done(foods: MutableList<BFood>?, e: BmobException?) {
                    if (e == null) {
                        if (currentCategory?.FoodTotal != foods!!.size) {
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
        when (BDM.ShareSet?.ItemEditState){
            EditerState.FoodEdit -> FoodRView?.adapter?.notifyItemChanged(updateItem.Position)
            EditerState.FoodAppend -> {
                foodList?.add(BDM.ShareSet?.CurrentFood!!)
                val foodSize = foodList?.size!!
                FoodRView?.adapter?.notifyItemInserted(foodSize)
                updateCategoryFoodSize(foodSize)
                BDM.ShareSet?.ItemEditState = EditerState.FoodEdit
            }
        }
        //EventBus.getDefault().removeStickyEvent(updateItem)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)  //, sticky = true
    fun doDeleteFoodRecyclerItem(deleteItem: DeleteFoodRecyclerItem){
        foodList?.removeAt(deleteItem.Position)
        FoodRView?.adapter?.notifyItemRemoved(deleteItem.Position)
        updateCategoryFoodSize(foodList!!.size)
    }

    private fun updateCategoryFoodSize(size: Int, showMessage: Boolean = false){
        val currentCategory = BDM.ShareSet?.CurrentCategory!!
        currentCategory.FoodTotal = size
        currentCategory.update(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    EventBus.getDefault().post(UpdateCategoryRecyclerItem(BDM.ShareSet?.CurrentCategoryPosition!!))
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
