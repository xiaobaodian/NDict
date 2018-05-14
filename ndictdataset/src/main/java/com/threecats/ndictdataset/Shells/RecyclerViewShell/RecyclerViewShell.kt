package com.threecats.ndictdataset.Shells.RecyclerViewShell

import android.content.Context
import android.view.View
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ProgressBar
import org.jetbrains.anko.toast

/**
 * 由 zhang 于 2018/3/28 创建
 */
class RecyclerViewShell<G,I>(val context: Context) {

    private var progressBar: ProgressBar? = null
    private var recyclerView: RecyclerView? = null
    private var recyclerAdapter: RecyclerViewAdapter<G,I>? = null

    private val isNullItems: Boolean
        get() = dataSet.recyclerViewItems.size == 0
    val currentItem: I?
        get() = dataSet.currentItem?.self
    private val currentRecyclerViewItem: RecyclerViewItem<G, I>
        get() = dataSet.currentItem!!
//    val recyclerViewItems: MutableList<RecyclerViewItem<G, I>>?
//        get() = if (dataSet.recyclerViewNodes.size > 0) null else dataSet.recyclerViewItems as MutableList<RecyclerViewItem<G, I>>
//    val items: List<I>?
//        get() = if (dataSet.recyclerViewNodes.size > 0) null else dataSet.items

    internal val viewTypes: MutableList<RecyclerViewType> = ArrayList()

    private var dataSet: RecyclerViewData<G, I> = RecyclerViewData(this)

    private var clickNodeListener: ClickNodeListener<G,I>? = null
    private var clickItemListener: ClickItemListener<G, I>? = null
    private var longClickNodeListener: LongClickNodeListener<G,I>? = null
    private var longClickItemListener: LongClickItemListener<G, I>? = null
    private var displayNodeListener: DisplayNodeListener<G,I>? = null
    private var displayItemListener: DisplayItemListener<G, I>? = null
    private var queryDatasListener: QueryDatasListener<G, I>? = null
    private var completeQueryListener: CompleteQueryListener? = null
    private var nullDataListener: NullDataListener? = null

    init {
        recyclerAdapter = RecyclerViewAdapter(dataSet, this)
        dataSet.adapter = recyclerAdapter
    }

    fun recyclerView(recyclerView: RecyclerView): RecyclerViewShell<G, I> {
        this.recyclerView = recyclerView
        return this
    }

    fun progressBar(progressBar: ProgressBar): RecyclerViewShell<G, I> {
        this.progressBar = progressBar
        this.progressBar?.visibility = View.GONE
        return this
    }

    fun link(){
        if (viewTypes.size == 0) {
            context.toast("组头或条目的布局资源未设置")
            return
        }
        val layoutManager = LinearLayoutManager(context)
        recyclerView?.let {
            it.layoutManager = layoutManager
            it.adapter = recyclerAdapter
        }
        if (isNullItems) {
            queryData(this) // 在查询数据的监听器里面处理数据查询与导入，并调用completeQuery
        } else {
            completeQuery()
        }
    }

    fun addViewType(title: String, type: ItemType, layout: Int): RecyclerViewShell<G, I>{
        viewTypes.add(RecyclerViewType(title, type, layout))
        return this
    }

    fun getViewType(id: String): RecyclerViewType? {
        return viewTypes.find { it.ID === id }
    }

    fun getViewType(index: Int): RecyclerViewType {
        return viewTypes[index]
    }

    fun addNode(node: G){
        dataSet.addNode(node)
    }

    fun addItem(item: I, group: G){
        dataSet.addItem(item, group)
    }

    fun addItem(item: I){
        dataSet.addItem(item)
    }

    fun addItems(items: MutableList<I>){
        items.forEach {
            dataSet.addItem(it)
        }
    }

    fun updateItem(item: I){
        dataSet.updateItem(item)
    }

    fun removeItem(item: I){
        dataSet.removeItem(item)
    }

    fun removeCurrentItem(item: I){
        if (currentRecyclerViewItem.self === item) {
            removeItem(item)
        }
    }

    fun itemsSize(): Int {
        return dataSet.recyclerViewItems.size
    }

    //=================================================

    fun setOnClickNodeListener(listener: ClickNodeListener<G, I>){
        clickNodeListener = listener
    }

    fun setOnClickItemListener(listener: ClickItemListener<G, I>){
        clickItemListener = listener
    }

    fun setOnLongClickNodeListener(listener: LongClickNodeListener<G, I>){
        longClickNodeListener = listener
    }

    fun setOnLongClickItemListener(listener: LongClickItemListener<G, I>){
        longClickItemListener = listener
    }

    fun setDisplayNodeListener(listener: DisplayNodeListener<G, I>){
        displayNodeListener = listener
    }

    fun setDisplayItemListener(listener: DisplayItemListener<G, I>){
        displayItemListener = listener
    }

    fun setQueryDataListener(listener: QueryDatasListener<G, I>){
        queryDatasListener = listener
    }

    fun setonCompleteQueryListener(listener: CompleteQueryListener){
        completeQueryListener = listener
    }

    fun setOnNullDataListener(listener: NullDataListener){
        nullDataListener = listener
    }

    internal fun displayNode(node: G, holder: RecyclerViewAdapter<G, I>.NodeViewHolder){
        displayNodeListener?.onDisplayNode(node, holder)
    }

    internal fun displayItem(item: I, holder: RecyclerViewAdapter<G, I>.ItemViewHolder){
        displayItemListener?.onDisplayItem(item, holder)
    }

    internal fun clickNode(node: G, holder: RecyclerViewAdapter<G, I>.NodeViewHolder){
        clickNodeListener?.onClickNode(node, holder)
    }

    internal fun clickItem(item: I, holder: RecyclerViewAdapter<G, I>.ItemViewHolder){
        clickItemListener?.onClickItem(item, holder)
    }

    internal fun longClickNode(node: G, holder: RecyclerViewAdapter<G, I>.NodeViewHolder){
        longClickNodeListener?.onLongClickNode(node, holder)
    }

    internal fun longClickItem(item: I, holder: RecyclerViewAdapter<G, I>.ItemViewHolder){
        longClickItemListener?.onLongClickItem(item, holder)
    }

    private fun queryData(shell: RecyclerViewShell<G, I>){
        queryDatasListener?.let {
            progressBar?.visibility = View.VISIBLE
            it.onQueryDatas(shell)
        }
    }

    internal fun completeQuery(){
        progressBar?.visibility = View.GONE
        completeQueryListener?.onCompleteQuery()
    }

    internal fun whenNullData(isNullData: Boolean){
        nullDataListener?.onNullData(isNullData)
    }

}