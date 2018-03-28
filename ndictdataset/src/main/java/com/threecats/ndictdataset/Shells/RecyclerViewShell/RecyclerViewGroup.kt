package com.threecats.ndictdataset.Shells.RecyclerViewShell

import java.util.ArrayList
import java.util.Collections


/**
 * 由 zhang 于 2017/8/3 创建
 */

class RecyclerViewGroup: RecyclerViewItem() {  //abstract

    var id: Long = 0
    var title: String = ""
    var note: String = ""
    //var groupType: GroupType? = null
    var previousGroup: RecyclerViewGroup? = null
    var nextGroup: RecyclerViewGroup? = null

    var positionID: Int = -1
    var State: DisplayState = DisplayState.Hide

    var imageID: Int = 0
    var layoutID: Int = 0
    var isEmpty = false

    val items: MutableList<RecyclerViewItem> = ArrayList()

    init {
        itemType = ItemType.Group
    }

    //以下是分组里面的条目管理
    //    public int addTask(Task item){
    //        item.addParentGroup(this);
    //        items.add(item);
    //        if (State == DisplayState.Hide) State = DisplayState.Show;
    //        return items.size();      //返回加入的任务的位置序号，便于组列表处理（0位是组标题）
    //    }
    fun addItem(item: RecyclerViewItem): Int {
        item.group = this
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
        return position
    }

    fun removeItem(item: RecyclerViewItem): Int {
        val position = items.indexOf(item)
        if (position >= 0) items.removeAt(position)
        return position
    }

    fun needChangedPosition(item: RecyclerViewItem): Boolean {
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
