package com.threecats.ndictdataset.Shells.RecyclerViewShell

import java.util.ArrayList
import java.util.Collections


/**
 * 由 zhang 于 2017/8/3 创建
 */

class RecyclerViewGroup<G,I>: RecyclerViewBaseItem() {  //abstract

    var id: Long = 0
    var title: String = ""
    var note: String = ""
    //var groupType: GroupType? = null

    var parentData: RecyclerViewData<G, I>? = null
    var previousGroup: RecyclerViewGroup<G, I>? = null
    var nextGroup: RecyclerViewGroup<G, I>? = null

    var groupSiteID: Int = -1
    var State: DisplayState = DisplayState.Hide

    var imageID: Int = 0
    var layoutID: Int = 0
    var isEmpty = false

    private var data: G? = null
    val items: MutableList<RecyclerViewItem<G,I>> = ArrayList()

    fun putObject(o: G){
        data = o
    }

    fun getObject(): G?{
        return data
    }


    //以下是分组里面的条目管理
    //    public int addTask(Task item){
    //        item.addParentGroup(this);
    //        items.add(item);
    //        if (State == DisplayState.Hide) State = DisplayState.Show;
    //        return items.size();      //返回加入的任务的位置序号，便于组列表处理（0位是组标题）
    //    }
    fun addItem(item: RecyclerViewItem<G, I>): Int {
        item.parentGroups.add(this)
        items.add(item)
        val position = items.size
//        if (items.size == 0) {
//            items.add(item)
//            site = 0
//        } else if (item.compareTo(items[items.size - 1]) >= 0) {
//            items.add(item)
//            site = items.size - 1
//        } else {
//            for (i in items.indices) {
//                if (item.compareTo(items[i]) < 0) {
//                    site = i
//                    break
//                }
//            }
//            items.add(site, item)
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
        val position = items.indexOf(item)
        if (position >= 0){
            items.removeAt(position)
            item.parentGroups.remove(this)
            parentData?.let {
                if (items.size == 0) {
                    it.hideGroup(this)
                }
                it.removeItemFromRecyclerViewItems(this, position, item)
                it.calculatorTitleSite()
            }
        }
        return position
    }

    fun needChangedPosition(item: RecyclerViewItem<G, I>): Boolean {
        val pose = items.indexOf(item)
        if (pose < 0) return false
        val prevTask = if (pose == 0) null else items[pose - 1]
        val nextTask = if (pose == items.size - 1) null else items[pose + 1]
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
