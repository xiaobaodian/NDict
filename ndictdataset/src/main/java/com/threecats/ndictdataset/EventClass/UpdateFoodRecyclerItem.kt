package com.threecats.ndictdataset.EventClass

import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Enum.EditerState

/**
 * 由 zhang 于 2018/2/19 创建
 */
class UpdateFoodRecyclerItem(food: BFood, state: EditerState) {
    val Food = food
    val State = state
}