package com.threecats.ndictdataset.Models

import android.content.Context
import com.threecats.ndictdataset.Bmob.*
import com.threecats.ndictdataset.Enum.EEditerState
import com.threecats.ndictdataset.Shells.EditorShell.EditorShell

/**
 * 由 zhang 于 2018/2/17 创建
 */
class PublicSet(val appContext: Context) {

    var ItemEditState = EEditerState.FoodEdit
    var currentCategory: BFoodCategory? = null
    var currentFood: BFood? = null
    var currentNutrient: BNutrient? = null
    var currentTraceElement: BTraceElement? = null
    val editorCategory = EditorShell<BFoodCategory>()
    val editorFood = EditorShell<BFood>()
    val editorProposedDosage = EditorShell<ProposedDosage>()
    val editorTraceElement = EditorShell<BTraceElement>()
    val editorNutrient = EditorShell<BNutrient>()
    val lastUpdateState = LastUpdateState(appContext)


}