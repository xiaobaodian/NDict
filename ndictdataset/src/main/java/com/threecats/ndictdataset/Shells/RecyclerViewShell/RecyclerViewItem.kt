package com.threecats.ndictdataset.Shells.RecyclerViewShell

import java.util.ArrayList

/**
 * Created by zhang on 2017/8/7.
 */

class RecyclerViewItem<G,I>: RecyclerViewBaseItem() {

    val parentGroups: MutableList<RecyclerViewGroup<G,I>> = ArrayList()
    private var data: I? = null

    init {
        viewType = RecyclerViewViewType(ItemType.Item)
    }

    fun putObject(o: I){
        data = o
    }

    fun getObject(): I?{
        return data
    }

}
