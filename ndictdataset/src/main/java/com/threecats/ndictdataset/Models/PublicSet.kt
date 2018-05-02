package com.threecats.ndictdataset.Models

import android.content.Context
import com.threecats.ndictdataset.Bmob.*
import com.threecats.ndictdataset.Enum.EEditerState
import com.threecats.ndictdataset.Shells.EditorShell.EditorShell

/**
 * 由 zhang 于 2018/2/17 创建
 */
class PublicSet(val AppContext: Context) {

    var ItemEditState = EEditerState.FoodEdit
    var currentCategory: BFoodCategory? = null
    var CurrentCategoryPosition: Int = 0
    var currentFood: BFood? = null
    var currentNutrient: BNutrient? = null
    var currentTraceElement: BTraceElement? = null
    val editorProposedDosage = EditorShell<ProposedDosage>()
    val editorNutrient = EditorShell<BNutrient>()

    fun createFood(){
        ItemEditState = EEditerState.FoodAppend
        val food = BFood().apply {
            category = currentCategory
        }
        currentFood = food
    }

    fun editFood(food: BFood){
        ItemEditState = EEditerState.FoodEdit
        currentFood = food
    }

}