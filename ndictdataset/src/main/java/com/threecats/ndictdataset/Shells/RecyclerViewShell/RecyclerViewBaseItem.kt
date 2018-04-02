package com.threecats.ndictdataset.Shells.RecyclerViewShell

import java.util.ArrayList

/**
 * Created by zhang on 2017/8/7.
 */

open class RecyclerViewBaseItem {
    var viewType: RecyclerViewViewType
    var checked = false

    init {
        viewType = RecyclerViewViewType(ItemType.Item)
    }
}
