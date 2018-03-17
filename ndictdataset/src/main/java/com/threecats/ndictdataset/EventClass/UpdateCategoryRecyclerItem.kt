package com.threecats.ndictdataset.EventClass

import com.threecats.ndictdataset.Bmob.BFoodCategory
import com.threecats.ndictdataset.Enum.EEditerState

/**
 * 由 zhang 于 2018/2/19 创建
 */
class UpdateCategoryRecyclerItem(category: BFoodCategory, state: EEditerState) {
    val Category = category
    val State = state
}