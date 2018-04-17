package com.threecats.ndictdataset.Shells.RecyclerViewShell

/**
 * 由 zhang 创建于 2018/3/28.
 */
interface DisplayGroupListener<G, I>{
    fun onDisplayGroup(group: G, holder: RecyclerViewAdapter<G, I>.GroupViewHolder)
}

interface DisplayItemListener<G, I>{
    fun onDisplayItem(item: I, holder: RecyclerViewAdapter<G, I>.ItemViewHolder)
}

interface ClickGroupListener<G, I>{
    fun onClickGroup(group: G, holder: RecyclerViewAdapter<G, I>.GroupViewHolder)
}

interface ClickItemListener<G, I>{
    fun onClickItem(item: I, holder: RecyclerViewAdapter<G, I>.ItemViewHolder)
}

interface LongClickGroupListener<G, I>{
    fun onLongClickGroup(group: G, holder: RecyclerViewAdapter<G, I>.GroupViewHolder)
}

interface LongClickItemListener<G, I>{
    fun onLongClickItem(item: I, holder: RecyclerViewAdapter<G, I>.ItemViewHolder)
}

interface QueryDatasListener<G, I>{
    fun onQueryDatas(shell: RecyclerViewShell<G, I>)
}

interface CompleteQueryListener{
    fun onCompleteQuery()
}

interface NullDataListener{
    fun onNullData(isNull: Boolean)
}