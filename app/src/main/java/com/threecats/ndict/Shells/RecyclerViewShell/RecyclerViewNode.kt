package com.threecats.ndict.Shells.RecyclerViewShell

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

    var nodePositionID: Int = -1
    var state: DisplayState = DisplayState.Hide

    val isEmpty: Boolean
        get() = items.isEmpty()

    val items: MutableList<RecyclerViewItem<G,I>> = ArrayList()

    init {
        viewType = RecyclerViewType(ItemType.Node)
    }

    fun getObject(): G{
        return data
    }


    //以下是分组里面的条目管理
    //    public int addTask(Task item){
    //        item.addParentGroup(this);
    //        items.add(item);
    //        if (state == DisplayState.Hide) state = DisplayState.Show;
    //        return items.size();      //返回加入的任务的位置序号，便于组列表处理（0位是组标题）
    //    }
    fun addItem(item: RecyclerViewItem<G, I>): Int {
        item.parentNodes.add(this)
        items.add(item)
        val position = items.size - 1

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
//        if (state === DisplayState.Hide) state = DisplayState.Show
        parentData?.let {
            if (state === DisplayState.Hide) {
                it.activeNode(this)
            }
            //it.addItemToRecyclerViewItems(this, position, item)
            //it.calculatorTitlePosition()
        }
        return position
    }

    fun removeItem(item: RecyclerViewItem<G, I>): Int {
        var position = items.indexOf(item)
        if (position >= 0){
            items.removeAt(position)
            item.parentNodes.remove(this)
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
