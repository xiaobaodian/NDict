package com.threecats.ndictdataset.Shells.RecyclerViewShell

import android.support.v7.widget.RecyclerView

/**
 * 由 zhang 于 2018/3/28 创建
 */
class RecyclerViewShell {

    private var recyclerView: RecyclerView? = null
    private var recyclerAdapter: RecyclerViewAdapter? = null

    private var globalItemLayoutID: Int = 0
    private var globalGroupLayoutID: Int = 0

    fun recyclerView(r: RecyclerView): RecyclerViewShell {
        recyclerView = r
        return this
    }

    fun globalItemLayout(itemLayout: Int): RecyclerViewShell {
        globalItemLayoutID = itemLayout
        return this
    }

    fun globalGroupLayout(groupLayout: Int): RecyclerViewShell {
        globalGroupLayoutID = groupLayout
        return this
    }
}