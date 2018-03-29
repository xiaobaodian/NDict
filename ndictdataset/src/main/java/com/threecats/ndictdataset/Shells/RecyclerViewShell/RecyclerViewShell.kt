package com.threecats.ndictdataset.Shells.RecyclerViewShell

import android.view.View
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import org.jetbrains.anko.toast

/**
 * 由 zhang 于 2018/3/28 创建
 */
class RecyclerViewShell(val view: View) {

    var recyclerView: RecyclerView? = null
        get() = field
    var recyclerAdapter: RecyclerViewAdapter? = null
        get() = field

    internal val viewTypes: MutableList<RecyclerViewViewType> = ArrayList()

    private var dataSet: RecyclerViewData

    private var clickGroupListener: onClickGroupListener? = null
    private var clickItemListener: onClickItemListener? = null
    private var longClickGroupListener: onLongClickGroupListener? = null
    private var longClickItemListener: onLongClickItemListener? = null
    private var displayGroupListener: onDisplayGroupListener? = null
    private var displayItemListener: onDisplayItemListener? = null
    private var itemSizeChangedListener: onItemSizeChangedListener? = null

    init {
        dataSet = RecyclerViewData(this)
        recyclerAdapter = RecyclerViewAdapter(dataSet, this)
    }

    fun recyclerView(recyclerView: RecyclerView): RecyclerViewShell {
        this.recyclerView = recyclerView
        return this
    }

    fun link(){
        if (viewTypes.size == 0) {
            view.context.toast("组头或条目的布局资源未设置")
            return
        }
        val layoutManager = LinearLayoutManager(view.getContext())
        recyclerView?.let {
            it.setLayoutManager(layoutManager)
            it.setAdapter(recyclerAdapter)
        }
    }

    fun addViewType(tag: String, type: ItemType, layout: Int): RecyclerViewShell{
        viewTypes.add(RecyclerViewViewType(tag, type, layout))
        return this
    }

    fun getViewType(tag: String): RecyclerViewViewType? {
        return viewTypes.find { it.tag === tag }
    }

    fun getViewType(hash: Int): RecyclerViewViewType? {
        return viewTypes.find { it.hashCode() == hash }
    }

    fun setOnClickGroupListener(listener: onClickGroupListener){
        clickGroupListener = listener
    }

    fun setOnClickItemListener(listener: onClickItemListener){
        clickItemListener = listener
    }

    fun setOnLongClickGroupListener(listener: onLongClickGroupListener){
        longClickGroupListener = listener
    }

    fun setOnLongClickItemListener(listener: onLongClickItemListener){
        longClickItemListener = listener
    }

    fun setDisplayGroupListener(listener: onDisplayGroupListener){
        displayGroupListener = listener
    }

    fun setDisplayItemListener(listener: onDisplayItemListener){
        displayItemListener = listener
    }

    fun setItemSizeChangedListener(listener: onItemSizeChangedListener){
        itemSizeChangedListener = listener
    }

    internal fun displayGroup(group: RecyclerViewGroup, holder: RecyclerViewAdapter.GroupViewHolder){
        displayGroupListener?.onDisplayGroup(group, holder)
    }

    internal fun displayItem(item: RecyclerViewItem, holder: RecyclerViewAdapter.ItemViewHolder){
        displayItemListener?.onDisplayItem(item, holder)
    }

    internal fun clickGroup(group: RecyclerViewGroup, holder: RecyclerViewAdapter.GroupViewHolder){
        clickGroupListener?.onClickGroup(group, holder)
    }

    internal fun clickItem(item: RecyclerViewItem, holder: RecyclerViewAdapter.ItemViewHolder){
        clickItemListener?.onClickItem(item, holder)
    }

    internal fun longClickGroup(group: RecyclerViewGroup, holder: RecyclerViewAdapter.GroupViewHolder){
        longClickGroupListener?.onLongClickGroup(group, holder)
    }

    internal fun longClickItem(item: RecyclerViewItem, holder: RecyclerViewAdapter.ItemViewHolder){
        longClickItemListener?.onLongClickItem(item, holder)
    }

    internal fun itemSizeChanged(size: Int){
        itemSizeChangedListener?.onItemSizeChanged(size)
    }

}