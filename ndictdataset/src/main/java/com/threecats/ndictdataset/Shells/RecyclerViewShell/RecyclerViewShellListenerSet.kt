package com.threecats.ndictdataset.Shells.RecyclerViewShell

/**
 * 由 zhang 创建于 2018/3/28.
 */
interface onDisplayGroupListener<G, I>{
    fun onDisplayGroup(group: RecyclerViewGroup<G, I>, holder: RecyclerViewAdapter<G, I>.GroupViewHolder)
}

interface onDisplayItemListener<G, I>{
    fun onDisplayItem(item: RecyclerViewItem<G, I>, holder: RecyclerViewAdapter<G, I>.ItemViewHolder)
}

interface onClickGroupListener<G, I>{
    fun onClickGroup(group: RecyclerViewGroup<G, I>, holder: RecyclerViewAdapter<G, I>.GroupViewHolder)
}

interface onClickItemListener<G, I>{
    fun onClickItem(item: RecyclerViewItem<G, I>, holder: RecyclerViewAdapter<G, I>.ItemViewHolder)
}

interface onLongClickGroupListener<G, I>{
    fun onLongClickGroup(group: RecyclerViewGroup<G, I>, holder: RecyclerViewAdapter<G, I>.GroupViewHolder)
}

interface onLongClickItemListener<G, I>{
    fun onLongClickItem(item: RecyclerViewItem<G, I>, holder: RecyclerViewAdapter<G, I>.ItemViewHolder)
}

interface onNullDataListener{
    fun onNullData(isNull: Boolean)
}