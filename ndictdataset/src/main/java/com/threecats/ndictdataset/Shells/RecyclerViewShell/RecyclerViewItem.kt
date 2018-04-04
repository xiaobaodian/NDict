package com.threecats.ndictdataset.Shells.RecyclerViewShell

import java.util.ArrayList

/**
 * Created by zhang on 2017/8/7.
 */

class RecyclerViewItem<G,I>: RecyclerViewBaseItem() {

    val parentGroups: MutableList<RecyclerViewGroup<G,I>> = ArrayList()
    val self: I
        get() = data!!
    private var data: I? = null

    init {
        viewType = RecyclerViewViewType(ItemType.Item)
    }

    fun putObject(o: I): RecyclerViewItem<G,I> {
        data = o
        return this
    }

    fun getObject(): I?{
        return data
    }

}
