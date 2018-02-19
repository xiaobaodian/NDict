package com.threecats.ndictdataset.Models

import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Bmob.BFoodCategory
import com.threecats.ndictdataset.Enum.EditerState

/**
 * 由 zhang 于 2018/2/17 创建
 */
class PublicSet {
    var ItemEditState = EditerState.FoodEdit
    var CurrentCategory: BFoodCategory? = null
    var CurrentCategoryPosition: Int = 0
    var CurrentFood: BFood? = null
    var CurrentFoodPosition: Int = 0
}