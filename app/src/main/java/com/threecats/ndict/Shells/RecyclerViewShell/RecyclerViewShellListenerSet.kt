package com.threecats.ndict.Shells.RecyclerViewShell

/**
 * 由 zhang 创建于 2018/3/28.
 */

interface NodeMembership{
    fun isMembers(item: Any): Boolean
}

interface ItemViewType{
    fun viewType(): Int
}

interface GroupViewType{
    fun viewType(): Int
}

interface DisplayNodeListener<G, I>{
    fun onDisplayNode(node: G, holder: RecyclerViewAdapter<G, I>.NodeViewHolder)
}

interface DisplayItemListener<G, I>{
    fun onDisplayItem(item: I, holder: RecyclerViewAdapter<G, I>.ItemViewHolder)
}

interface ClickNodeListener<G, I>{
    fun onClickNode(node: G, holder: RecyclerViewAdapter<G, I>.NodeViewHolder)
}

interface ClickItemListener<G, I>{
    fun onClickItem(item: I, holder: RecyclerViewAdapter<G, I>.ItemViewHolder)
}

interface LongClickNodeListener<G, I>{
    fun onLongClickNode(node: G, holder: RecyclerViewAdapter<G, I>.NodeViewHolder)
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

interface DataSetEmptyListener{
    fun onDataSetEmpty(isEmpty: Boolean)
}