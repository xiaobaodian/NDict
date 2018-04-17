package com.threecats.ndictdataset.EventClass

import com.threecats.ndictdataset.Bmob.BFoodCategory
import com.threecats.ndictdataset.Enum.EEditerState
import com.threecats.ndictdataset.Shells.RecyclerViewShell.RecyclerViewItem

/**
 * 由 zhang 于 2018/2/19 创建
 */
class UpdateCategoryRecyclerItem<out G>(val category: G, val state: EEditerState)