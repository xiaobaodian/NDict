package com.threecats.ndictdataset.Models

import android.content.Context
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Bmob.BFoodCategory
import com.threecats.ndictdataset.Bmob.BFoodVitamin
import com.threecats.ndictdataset.Enum.EditerState
import org.jetbrains.anko.toast

/**
 * 由 zhang 于 2018/2/17 创建
 */
class PublicSet(val appContext: Context) {

    var ItemEditState = EditerState.FoodEdit
    var CurrentCategory: BFoodCategory? = null
    var CurrentCategoryPosition: Int = 0
    var CurrentFood: BFood? = null
    var CurrentFoodPosition: Int = 0
    var CurrentVitamin: BFoodVitamin? = null

    fun createFoodItem(){
        ItemEditState = EditerState.FoodAppend
        CurrentFood = BFood().apply { category = CurrentCategory }
        CurrentVitamin = BFoodVitamin().apply { Food = CurrentFood }
    }

    fun editFoodItem(food: BFood){
        ItemEditState = EditerState.FoodEdit
        CurrentFood = food

        var query: BmobQuery<BFoodVitamin> = BmobQuery()
        query.addWhereEqualTo("Food", BmobPointer(food))
        query.setLimit(1)
        query.findObjects(object: FindListener<BFoodVitamin>(){
            override fun done(vitamins: MutableList<BFoodVitamin>?, e: BmobException?) {
                if (e == null) {
                    vitamins?.forEach { CurrentVitamin = it }
                } else {
                    CurrentVitamin = BFoodVitamin()
                    appendVitaminItem(CurrentVitamin!!)
                }
            }
        })
    }

    private fun appendVitaminItem(vitaminItem: BFoodVitamin){
        vitaminItem.Food = CurrentFood
        vitaminItem.save(object: SaveListener<String>() {
            override fun done(objectID: String?, e: BmobException?) {
                if (e == null) {
                    appContext.toast("添补了维生素数据记录")
                } else {
                    appContext.toast("${e.message}")
                }
            }
        })
    }
}