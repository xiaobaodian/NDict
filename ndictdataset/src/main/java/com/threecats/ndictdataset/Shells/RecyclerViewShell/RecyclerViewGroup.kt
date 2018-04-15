package com.threecats.ndictdataset.Shells.RecyclerViewShell

import java.util.ArrayList


/**
 * 由 zhang 于 2017/8/3 创建
 */

class RecyclerViewGroup<G, I>: RecyclerViewBaseItem() {  //abstract

    var id: Long = 0
    var title: String = ""
    //var groupType: GroupType? = null

    var parentData: RecyclerViewData<G, I>? = null
    var previousGroup: RecyclerViewGroup<G, I>? = null
    var nextGroup: RecyclerViewGroup<G, I>? = null

    var groupSiteID: Int = -1
    var State: DisplayState = DisplayState.Hide

    var isEmpty = false

    private var data: G? = null
    val recyclerViewItems: MutableList<RecyclerViewItem<G,I>> = ArrayList()
    var items: MutableList<I> = ArrayList()

    fun putObject(o: G){
        data = o
    }

    fun getObject(): G?{
        return data
    }


    //以下是分组里面的条目管理
    //    public int addTask(Task item){
    //        item.addParentGroup(this);
    //        recyclerViewItems.add(item);
    //        if (State == DisplayState.Hide) State = DisplayState.Show;
    //        return recyclerViewItems.size();      //返回加入的任务的位置序号，便于组列表处理（0位是组标题）
    //    }
    fun addItem(item: RecyclerViewItem<G, I>): Int {
        item.parentGroups.add(this)
        recyclerViewItems.add(item)
        items.add(item.self)
        val position = recyclerViewItems.size
//        if (recyclerViewItems.size == 0) {
//            recyclerViewItems.add(item)
//            site = 0
//        } else if (item.compareTo(recyclerViewItems[recyclerViewItems.size - 1]) >= 0) {
//            recyclerViewItems.add(item)
//            site = recyclerViewItems.size - 1
//        } else {
//            for (i in recyclerViewItems.indices) {
//                if (item.compareTo(recyclerViewItems[i]) < 0) {
//                    site = i
//                    break
//                }
//            }
//            recyclerViewItems.add(site, item)
//        }
//        if (State === DisplayState.Hide) State = DisplayState.Show
        parentData?.let {
            if (State === DisplayState.Hide) {
                it.activeGroup(this)
            }
            it.addItemToRecyclerViewItems(this, position, item)
            it.calculatorTitleSite()
        }
        return position
    }

    fun removeItem(item: RecyclerViewItem<G, I>): Int {
        var position = items.indexOf(item.self)
        if (position > 0) {
            items.removeAt(position)
        }
        position = recyclerViewItems.indexOf(item)
        if (position >= 0){
            recyclerViewItems.removeAt(position)
            item.parentGroups.remove(this)
            parentData?.let {
                if (recyclerViewItems.size == 0) {
                    it.hideGroup(this)
                }
                it.removeItemFromRecyclerViewItems(this, position, item)
                it.calculatorTitleSite()
            }
        }
        return position
    }

    fun needChangedPosition(item: RecyclerViewItem<G, I>): Boolean {
        val pose = recyclerViewItems.indexOf(item)
        if (pose < 0) return false
        val prevTask = if (pose == 0) null else recyclerViewItems[pose - 1]
        val nextTask = if (pose == recyclerViewItems.size - 1) null else recyclerViewItems[pose + 1]
        var needChange = false
//        if (prevTask == null && nextTask == null) {
//            needChange = false
//        } else if (prevTask == null) {
//            if (item.compareTo(nextTask) > 0) needChange = true
//        } else if (nextTask == null) {
//            if (item.compareTo(prevTask) < 0) needChange = true
//        } else if (item.compareTo(prevTask) < 0 || item.compareTo(nextTask) > 0) {
//            needChange = true
//        }
        return needChange
    }


}
