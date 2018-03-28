package com.threecats.ndictdataset.Shells.RecyclerViewShell

import java.util.ArrayList

/**
 * 由 zhang 于 2018/3/28 创建
 */
class RecyclerViewData {
    val groups:  MutableList<out RecyclerViewGroup> = ArrayList()
    val recyclerItems: MutableList<out RecyclerViewItem> = ArrayList()
}