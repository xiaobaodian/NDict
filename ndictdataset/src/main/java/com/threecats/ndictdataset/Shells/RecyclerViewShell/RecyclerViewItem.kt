package com.threecats.ndictdataset.Shells.RecyclerViewShell

import java.util.ArrayList

/**
 * Created by zhang on 2017/8/7.
 */

class RecyclerViewItem<G,I>(item: I): RecyclerViewBaseItem() {

    val parentGroups: MutableList<RecyclerViewGroup<G,I>> = ArrayList()

    private var data: I
    val self: I
        get() = data

    init {
        viewType = RecyclerViewViewType(ItemType.Item)
        data = item
    }

    fun getObject(): I{
        return data
    }

}
