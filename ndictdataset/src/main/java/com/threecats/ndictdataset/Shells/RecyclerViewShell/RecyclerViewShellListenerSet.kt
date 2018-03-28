package com.threecats.ndictdataset.Shells.RecyclerViewShell

/**
 * 由 zhang 创建于 2018/3/28.
 */
interface onDisplayGroupListener{
    fun onDisplayGroup(group: RecyclerViewGroup, holder: RecyclerViewAdapter.GroupViewHolder)
}

interface onDisplayItemListener{
    fun onDisplayItem(item: RecyclerViewItem, holder: RecyclerViewAdapter.ItemViewHolder)
}

interface onClickGroupListener{
    fun onClickGroup(group: RecyclerViewGroup, holder: RecyclerViewAdapter.GroupViewHolder)
}

interface onClickItemListener{
    fun onClickItem(item: RecyclerViewItem, holder: RecyclerViewAdapter.ItemViewHolder)
}

interface onLongClickGroupListener{
    fun onLongClickGroup(group: RecyclerViewGroup, holder: RecyclerViewAdapter.GroupViewHolder)
}

interface onLongClickItemListener{
    fun onLongClickItem(item: RecyclerViewItem, holder: RecyclerViewAdapter.ItemViewHolder)
}

interface onItemSizeChangedListener{
    fun onItemSizeChanged(size: Int)
}