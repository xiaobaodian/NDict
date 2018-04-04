package com.threecats.ndictdataset.Models

import android.content.Context
import com.threecats.ndictdataset.Bmob.*
import com.threecats.ndictdataset.Enum.EEditerState
import com.threecats.ndictdataset.Shells.RecyclerViewShell.RecyclerViewItem
import com.threecats.ndictdataset.Shells.RecyclerViewShell.RecyclerViewShell

/**
 * 由 zhang 于 2018/2/17 创建
 */
class PublicSet(val AppContext: Context) {

    var ItemEditState = EEditerState.FoodEdit
    var CurrentCategory: RecyclerViewItem<Any, BFoodCategory>? = null
    var CurrentCategoryPosition: Int = 0
    var CurrentFood: RecyclerViewItem<Any, BFood>? = null
    var CurrentNutrient: BNutrient? = null

    fun createFood(){
        ItemEditState = EEditerState.FoodAppend
        val food = BFood().apply {
            category = CurrentCategory?.getObject()
            vitamin = BFoodVitamin()
            mineral = BFoodMineral()
            mineralExt = BFoodMineralExt()
        }
        CurrentFood = RecyclerViewItem<Any, BFood>().apply{
            putObject(food)
        }

    }

    fun editFood(food: RecyclerViewItem<Any, BFood>){
        ItemEditState = EEditerState.FoodEdit
        CurrentFood = food
    }

}