package com.threecats.ndictdataset.Shells.RecyclerViewShell

import android.content.Context
import android.view.View
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import org.jetbrains.anko.toast
import java.lang.reflect.Array

/**
 * 由 zhang 于 2018/3/28 创建
 */
class RecyclerViewShell<G,I>(val context: Context) {

    var recyclerView: RecyclerView? = null
        get() = field
    var recyclerAdapter: RecyclerViewAdapter<G,I>? = null
        get() = field

    internal val viewTypes: MutableList<RecyclerViewViewType> = ArrayList()

    var dataSet: RecyclerViewData<G, I>

    private var clickGroupListener: onClickGroupListener<G,I>? = null
    private var clickItemListener: onClickItemListener<G, I>? = null
    private var longClickGroupListener: onLongClickGroupListener<G,I>? = null
    private var longClickItemListener: onLongClickItemListener<G, I>? = null
    private var displayGroupListener: onDisplayGroupListener<G,I>? = null
    private var displayItemListener: onDisplayItemListener<G, I>? = null
    private var nullDataListener: onNullDataListener? = null

    init {
        dataSet = RecyclerViewData(this)
        recyclerAdapter = RecyclerViewAdapter(dataSet, this)
    }

    fun recyclerView(recyclerView: RecyclerView): RecyclerViewShell<G, I> {
        this.recyclerView = recyclerView
        return this
    }

    fun link(){
        if (viewTypes.size == 0) {
            context.toast("组头或条目的布局资源未设置")
            return
        }
        val layoutManager = LinearLayoutManager(context)
        recyclerView?.let {
            it.setLayoutManager(layoutManager)
            it.setAdapter(recyclerAdapter)
        }
    }

    fun addViewType(title: String, type: ItemType, layout: Int): RecyclerViewShell<G, I>{
        viewTypes.add(RecyclerViewViewType(title, type, layout))
        return this
    }

    fun getViewType(title: String): RecyclerViewViewType? {
        return viewTypes.find { it.title === title }
    }

    fun getViewType(index: Int): RecyclerViewViewType {
        return viewTypes[index]
    }

    fun addGroup(group: G){
        val g = RecyclerViewGroup<G, I>()
        g.putObject(group)
        dataSet.addGroup(g)
    }

    fun addItem(item: I, group: G){
        val recyclerGroup = dataSet.groups.find { it.getObject() === group }
        val recyclerItem = RecyclerViewItem<G, I>()
        recyclerItem.putObject(item)
        dataSet.addItem(recyclerItem, recyclerGroup!!)
    }

    fun addItem(item: I){
        val recyclerItem = RecyclerViewItem<G, I>()
        recyclerItem.putObject(item)
        dataSet.addItem(recyclerItem)
        //context.toast("加入了一个记录")
    }

    fun addItems(items: MutableList<I>){
        items.forEach {
            val recyclerItem = RecyclerViewItem<G, I>()
            recyclerItem.putObject(it)
            dataSet.addItem(recyclerItem)
        }
        //context.toast("加入了${dataSet.recyclerViewItems.size}个记录")
    }

    //=================================================

    fun setOnClickGroupListener(listener: onClickGroupListener<G, I>){
        clickGroupListener = listener
    }

    fun setOnClickItemListener(listener: onClickItemListener<G, I>){
        clickItemListener = listener
    }

    fun setOnLongClickGroupListener(listener: onLongClickGroupListener<G, I>){
        longClickGroupListener = listener
    }

    fun setOnLongClickItemListener(listener: onLongClickItemListener<G, I>){
        longClickItemListener = listener
    }

    fun setDisplayGroupListener(listener: onDisplayGroupListener<G, I>){
        displayGroupListener = listener
    }

    fun setDisplayItemListener(listener: onDisplayItemListener<G, I>){
        displayItemListener = listener
    }

    fun setOnNullDataListener(listener: onNullDataListener){
        nullDataListener = listener
    }

    internal fun displayGroup(group: RecyclerViewGroup<G, I>, holder: RecyclerViewAdapter<G, I>.GroupViewHolder){
        displayGroupListener?.onDisplayGroup(group, holder)
    }

    internal fun displayItem(item: RecyclerViewItem<G, I>, holder: RecyclerViewAdapter<G, I>.ItemViewHolder){
        displayItemListener?.onDisplayItem(item, holder)
    }

    internal fun clickGroup(group: RecyclerViewGroup<G, I>, holder: RecyclerViewAdapter<G, I>.GroupViewHolder){
        clickGroupListener?.onClickGroup(group, holder)
    }

    internal fun clickItem(item: RecyclerViewItem<G, I>, holder: RecyclerViewAdapter<G, I>.ItemViewHolder){
        clickItemListener?.onClickItem(item, holder)
    }

    internal fun longClickGroup(group: RecyclerViewGroup<G, I>, holder: RecyclerViewAdapter<G, I>.GroupViewHolder){
        longClickGroupListener?.onLongClickGroup(group, holder)
    }

    internal fun longClickItem(item: RecyclerViewItem<G, I>, holder: RecyclerViewAdapter<G, I>.ItemViewHolder){
        longClickItemListener?.onLongClickItem(item, holder)
    }

    internal fun whenNullData(isNullData: Boolean){
        nullDataListener?.onNullData(isNullData)
    }

}