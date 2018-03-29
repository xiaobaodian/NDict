package com.threecats.ndictdataset.Shells.RecyclerViewShell

import java.util.ArrayList

/**
 * Created by zhang on 2017/8/7.
 */

open class RecyclerViewItem {
    var itemType: ItemType
    var checked = false
    val parentGroups: MutableList<RecyclerViewGroup> = ArrayList()

    init {
        itemType = ItemType.Item
    }
}
