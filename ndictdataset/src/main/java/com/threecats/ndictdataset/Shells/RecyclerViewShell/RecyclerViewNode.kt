package com.threecats.ndictdataset.Shells.RecyclerViewShell

import java.util.ArrayList


/**
 * 由 zhang 于 2017/8/3 创建
 */

class RecyclerViewNode<G, I>(group: G): RecyclerViewBaseItem() {  //abstract

    var id: Long = 0
    var title: String = ""

    private var data: G = group
    val self: G
        get() = data
    //var groupType: GroupType? = null

    var parentData: RecyclerViewData<G, I>? = null
    var previousNode: RecyclerViewNode<G, I>? = null
    var nextNode: RecyclerViewNode<G, I>? = null

    var groupPositionID: Int = -1
    var state: DisplayState = DisplayState.Hide

    val isEmpty: Boolean
        get() = groupItems.isEmpty()

    val groupItems: MutableList<RecyclerViewItem<G,I>> = ArrayList()
    var items: MutableList<I> = ArrayList()

    init {
        viewType = RecyclerViewType(ItemType.Group)
    }

    fun getObject(): G{
        return data
    }


    //以下是分组里面的条目管理
    //    public int addTask(Task item){
    //        item.addParentGroup(this);
    //        groupItems.add(item);
    //        if (state == DisplayState.Hide) state = DisplayState.Show;
    //        return groupItems.size();      //返回加入的任务的位置序号，便于组列表处理（0位是组标题）
    //    }
    fun addItem(item: RecyclerViewItem<G, I>): Int {
        item.parentNodes.add(this)
        groupItems.add(item)
        items.add(item.self)
        val position = groupItems.size - 1

//        if (groupItems.size == 0) {
//            groupItems.add(item)
//            site = 0
//        } else if (item.compareTo(groupItems[groupItems.size - 1]) >= 0) {
//            groupItems.add(item)
//            site = groupItems.size - 1
//        } else {
//            for (i in groupItems.indices) {
//                if (item.compareTo(groupItems[i]) < 0) {
//                    site = i
//                    break
//                }
//            }
//            groupItems.add(site, item)
//        }
//        if (state === DisplayState.Hide) state = DisplayState.Show
        parentData?.let {
            if (state === DisplayState.Hide) {
                it.activeGroup(this)
            }
            //it.addItemToRecyclerViewItems(this, position, item)
            //it.calculatorTitlePosition()
        }
        return position
    }

    fun removeItem(item: RecyclerViewItem<G, I>): Int {
        var position = items.indexOf(item.self)
        if (position >= 0) {
            items.removeAt(position)
        }
        position = groupItems.indexOf(item)
        if (position >= 0){
            groupItems.removeAt(position)
            item.parentNodes.remove(this)
//            parentData?.let {
//                if (groupItems.size == 0) {
//                    it.hideGroup(this)
//                }
//            }
        }
        return position
    }

    fun needChangedPosition(item: RecyclerViewItem<G, I>): Boolean {
        val pose = groupItems.indexOf(item)
        if (pose < 0) return false
        val prevTask = if (pose == 0) null else groupItems[pose - 1]
        val nextTask = if (pose == groupItems.size - 1) null else groupItems[pose + 1]
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
