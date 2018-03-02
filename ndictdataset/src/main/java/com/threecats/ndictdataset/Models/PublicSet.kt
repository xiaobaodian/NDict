package com.threecats.ndictdataset.Models

import android.content.Context
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import com.threecats.ndictdataset.Bmob.*
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
    var CurrentVitamin: BFoodVitamin? = null

    fun createFoodItem(){
        ItemEditState = EditerState.FoodAppend
        CurrentFood = BFood().apply {
            category = CurrentCategory
            Vitamin = BFoodVitamin()
            Mineral = BFoodMineral()
            MineralExt = BFoodMineralExt()
        }
    }

    fun editFoodItem(food: BFood){
        ItemEditState = EditerState.FoodEdit
        CurrentFood = food
        CurrentVitamin = null
    }

}