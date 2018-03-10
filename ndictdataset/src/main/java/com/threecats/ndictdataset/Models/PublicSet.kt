package com.threecats.ndictdataset.Models

import android.content.Context
import com.threecats.ndictdataset.Bmob.*
import com.threecats.ndictdataset.Enum.EditerState

/**
 * 由 zhang 于 2018/2/17 创建
 */
class PublicSet(val ApplicationContext: Context) {

    var ItemEditState = EditerState.FoodEdit
    var CurrentCategory: BFoodCategory? = null
    var CurrentCategoryPosition: Int = 0
    var CurrentFood: BFood? = null

    fun createFood(){
        ItemEditState = EditerState.FoodAppend
        CurrentFood = BFood().apply {
            category = CurrentCategory
            vitamin = BFoodVitamin()
            mineral = BFoodMineral()
            mineralExt = BFoodMineralExt()
        }
    }

    fun editFood(food: BFood){
        ItemEditState = EditerState.FoodEdit
        CurrentFood = food
    }

}