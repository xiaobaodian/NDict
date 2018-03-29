package com.threecats.ndictdataset.Shells.RecyclerViewShell

import java.util.ArrayList

/**
 * Created by zhang on 2017/8/7.
 */

open class RecyclerViewItem {
    var viewType: RecyclerViewViewType? = null
    var checked = false
    val parentGroups: MutableList<RecyclerViewGroup> = ArrayList()
}
