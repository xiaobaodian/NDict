package com.threecats.ndictdataset.View

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.webkit.WebView
import android.widget.Toast
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.UpdateListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Bmob.BFoodCategory
import com.threecats.ndictdataset.R

import kotlinx.android.synthetic.main.activity_food_list.*
import kotlinx.android.synthetic.main.content_food_list.*

class FoodListActivity : AppCompatActivity() {

    private var foodList: MutableList<BFood>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        FoodRView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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
                            updateCategoryFoodTatol(foods!!.size)
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

    private fun updateCategoryFoodTatol(tatol: Int){
        val currentCategory = BDM.ShareSet?.CurrentCategory
        val oId = currentCategory?.objectId
        var tempCategory = BFoodCategory()
        tempCategory.FoodTotal = tatol
        tempCategory.update(oId, object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    Toast.makeText(this@FoodListActivity, "更新${currentCategory?.LongTitle}类的食材总数：$tatol ", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}
