package com.threecats.ndictdataset.Shells.RecyclerViewShell

import org.jetbrains.anko.toast
import java.util.ArrayList

/**
 * 由 zhang 于 2018/3/28 创建
 */
class RecyclerViewData(val shell: RecyclerViewShell) {

    val groups:  MutableList<RecyclerViewGroup> = ArrayList()
    val recyclerViewItems: MutableList<RecyclerViewItem> = ArrayList()

    fun addGroup(group: RecyclerViewGroup){
        if (groups.size > 0) {
            val lastGroup = groups[groups.size - 1]
            lastGroup.nextGroup = group
            group.previousGroup = lastGroup
        }
        groups.add(group)
        group.parentData = this
        addGroupInRecyclerViewItems(group)
        calculatorTitleSite()
    }

    fun getGroup(id: Long): RecyclerViewGroup? {
        val group = groups.find { it.id == id }
        return group
    }

    fun getItemsCount(): Int {
        var count = 0
        groups.forEach { count += it.items.size }
        return count
    }

    private fun calculatorTitleSite() {
        var site = 0
        //parentGroups.takeWhile { it.State === DisplayState.Show }.forEach {
        groups.filter { it.State === DisplayState.Show }.forEach {
            it.groupSiteID = site
            site += it.items.size + 1
        }
    }

    private fun addGroupInRecyclerViewItems(group: RecyclerViewGroup) {
        if (group.State === DisplayState.Hide) return
        recyclerViewItems.add(group)
        recyclerViewItems.addAll(group.items)
    }

    private fun hideGroup(group: RecyclerViewGroup) {
        if (group.items.size > 0) return
        if (group.groupSiteID > recyclerViewItems.size - 1) {
            shell.view.context.toast("Hide Group : group.groupSiteID > size()")
            return
        }
        if (group === recyclerViewItems[group.groupSiteID]) {
            recyclerViewItems.removeAt(group.groupSiteID)
            shell.recyclerAdapter?.notifyItemRemoved(group.groupSiteID)
            group.State = DisplayState.Hide
            calculatorTitleSite()
        } else {
            shell.view.context.toast("HideGroup出现错误")
        }
    }

    private fun activeGroup(group: RecyclerViewGroup) {
        if (group.State === DisplayState.Show || group.State === DisplayState.Fold) return
        val nextGroupSite = nextGroupSite(group)
        if (nextGroupSite >= 0) {
            group.groupSiteID = nextGroupSite
            recyclerViewItems.add(nextGroupSite, group)
            shell.recyclerAdapter?.notifyItemInserted(nextGroupSite)
        } else {
            //如果没有下一组，就表明当前组就是最后一组，不用插入，只需要添加
            recyclerViewItems.add(group)
            group.groupSiteID = recyclerViewItems.size - 1
            shell.recyclerAdapter?.notifyItemInserted(group.groupSiteID)
        }
        group.State = DisplayState.Show
    }

    private fun nextGroupSite(group: RecyclerViewGroup): Int {
        var nextGroupSite = -1
        var hasBeforeGroup = true
        for (currentGroup in groups) {
            if (hasBeforeGroup) {
                if (currentGroup === group) hasBeforeGroup = false
                continue
            }
            if (currentGroup.State === DisplayState.Show) {
                nextGroupSite = currentGroup.groupSiteID
                break
            }
        }
        return nextGroupSite
    }

    fun addItem(item: RecyclerViewItem): Int{
        var position = -1
        if (groups.size == 0) {
            item.viewType?.itemType = ItemType.Item
            recyclerViewItems.add(item)
            position = recyclerViewItems.size - 1
            shell.recyclerAdapter?.notifyItemInserted(position)
        }
        return position
    }

    fun addItem(item: RecyclerViewItem, group: RecyclerViewGroup) {
        if (group.State === DisplayState.Hide) {
            activeGroup(group)
        }
        val site = group.addItem(item)
        addItemToRecyclerViewItems(group, site, item)
        calculatorTitleSite()
    }

    fun addItem(item: RecyclerViewItem, groupID: Long) {
        val group = groups.find { it.id == groupID }
        group?.let { addItem(item, it) }
    }

    fun removeItem(item: RecyclerViewItem) {
        if (groups.size == 0) {
            val site = recyclerViewItems.indexOf(item)
            if (site >= 0) {
                recyclerViewItems.removeAt(site)
                shell.recyclerAdapter?.notifyItemRemoved(site)
            }
        }
    }

    fun removeItem(item: RecyclerViewItem, group: RecyclerViewGroup) {
        item.parentGroups.remove(group)
        val site = group.removeItem(item)
        if (site >= 0) {
            removeItemFromRecyclerViewItems(group, site, item)
            if (group.items.size == 0) {
                hideGroup(group)
            }
            calculatorTitleSite()
        }
    }

    fun removeItem(item: RecyclerViewItem, groupID: Long){
        val group = groups.find { it.id == groupID }
        group?.let { removeItem(item, it) }
    }

    private fun addItemToRecyclerViewItems(group: RecyclerViewGroup, groupSite: Int, item: RecyclerViewItem) {
        //根据任务在分组中的位置计算出任务在RecyclerView列表中的位置
        val site = group.groupSiteID + groupSite + 1
        recyclerViewItems.add(site, item)
        shell.recyclerAdapter?.notifyItemInserted(site)
    }

    private fun removeItemFromRecyclerViewItems(group: RecyclerViewGroup, groupSite: Int, item: RecyclerViewItem) {
        val site = group.groupSiteID + groupSite + 1  //从分组中返回的位置是不包括组头的，就是说分组中列表是从0算起的所以+1
        if (recyclerViewItems[site] === item) {
            recyclerViewItems.removeAt(site)
            shell.recyclerAdapter?.notifyItemRemoved(site)
        } else {
            shell.view.context.toast("要删除的Item与列表中指定位置的记录不相符")
        }
    }

    fun updateItemDisplay(item: RecyclerViewItem, group: RecyclerViewGroup) {
        val site = group.items.indexOf(item) + group.groupSiteID + 1
        shell.recyclerAdapter?.notifyItemChanged(site)
    }

    fun updateItemDisplay(item: RecyclerViewItem, groupID: Long){
        val group = groups.find { it.id == groupID }
        group?.let { updateItemDisplay(item, it) }
    }
}