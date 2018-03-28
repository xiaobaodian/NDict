package com.threecats.ndictdataset.Shells.RecyclerViewShell

/**
 * Created by zhang on 2017/8/7.
 */

open class RecyclerViewItem {
    var itemType: ItemType
    var checked = false
    var group: RecyclerViewGroup? = null

    init {
        itemType = ItemType.Item
    }
}
