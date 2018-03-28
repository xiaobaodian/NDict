package com.threecats.ndictdataset.Shells.RecyclerViewShell

import java.util.ArrayList

/**
 * 由 zhang 于 2018/3/28 创建
 */
class RecyclerViewData {

    val groups:  MutableList<RecyclerViewGroup> = ArrayList()
    val recyclerItems: MutableList<RecyclerViewItem> = ArrayList()

    fun addGroup(group: RecyclerViewGroup){
        groups.add(group)
    }

    fun getGroup(id: Long): RecyclerViewGroup? {
        val group = groups.find { it.id == id }
        return group
    }

    fun addItem(groupID: Long, item: RecyclerViewItem): Int {
        var position = -1
        val group = getGroup(groupID)
        group?.let {
            position = it.addItem(item)
        }
        return position
    }

    fun addItem(item: RecyclerViewItem): Int{
        var position = -1
        if (groups.size == 0) {
            recyclerItems.add(item)
            position = recyclerItems.size
        }
        return position
    }
}