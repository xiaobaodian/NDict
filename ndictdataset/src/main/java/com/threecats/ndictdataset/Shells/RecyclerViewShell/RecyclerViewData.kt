package com.threecats.ndictdataset.Shells.RecyclerViewShell

import org.jetbrains.anko.toast
import java.util.ArrayList

/**
 * 由 zhang 于 2018/3/28 创建
 */
class RecyclerViewData<G, I>(val shell: RecyclerViewShell<G, I>) {

    var currentGroup: RecyclerViewGroup<G, I>? = null
    var currentItem: RecyclerViewItem<G, I>? = null
    var currentGroupPosition: Int? = null
    var currentRecyclerItemPosition: Int? = null

    val recyclerGroups: MutableList<RecyclerViewGroup<G, I>> = ArrayList()
    val recyclerViewItems: MutableList<RecyclerViewBaseItem> = ArrayList()

    val groups: MutableList<G> = ArrayList()
    val items: MutableList<I> = ArrayList()

    fun addGroup(group: RecyclerViewGroup<G, I>){
        if (recyclerGroups.size > 0) {
            val lastGroup = recyclerGroups[recyclerGroups.size - 1]
            lastGroup.nextGroup = group
            group.previousGroup = lastGroup
        }
        recyclerGroups.add(group)
        group.parentData = this
        addGroupInRecyclerViewItems(group)
        calculatorTitleSite()
    }

    fun getGroup(id: Long): RecyclerViewGroup<G,I>? {
        val group = recyclerGroups.find { it.id == id }
        return group
    }

    fun getItemsCount(): Int {
        var count = 0
        recyclerGroups.forEach { count += it.recyclerViewItems.size }
        return count
    }

    fun calculatorTitleSite() {
        var site = 0
        //parentGroups.takeWhile { it.State === DisplayState.Show }.forEach {
        recyclerGroups.filter { it.State === DisplayState.Show }.forEach {
            it.groupSiteID = site
            site += it.recyclerViewItems.size + 1
        }
    }

    private fun addGroupInRecyclerViewItems(group: RecyclerViewGroup<G,I>) {
        if (group.State === DisplayState.Hide) return
        recyclerViewItems.add(group)
        recyclerViewItems.addAll(group.recyclerViewItems)
    }

    fun hideGroup(group: RecyclerViewGroup<G,I>) {
        if (group.recyclerViewItems.size > 0) return
        if (group.groupSiteID > recyclerViewItems.size - 1) {
            shell.context.toast("Hide Group : group.groupSiteID > size()")
            return
        }
        if (group === recyclerViewItems[group.groupSiteID]) {
            recyclerViewItems.removeAt(group.groupSiteID)
            shell.recyclerAdapter?.notifyItemRemoved(group.groupSiteID)
            group.State = DisplayState.Hide
            calculatorTitleSite()
        } else {
            shell.context.toast("HideGroup出现错误")
        }
    }

    fun activeGroup(group: RecyclerViewGroup<G,I>) {
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

    private fun nextGroupSite(group: RecyclerViewGroup<G,I>): Int {
        var nextGroupSite = -1
        var hasBeforeGroup = true
        for (currentGroup in recyclerGroups) {
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

    fun addItem(item: RecyclerViewItem<G, I>): Int{
        var position = -1
        if (recyclerGroups.size == 0) {
            items.add(item.self)  // 建立I类的实例对象的映射列表
            item.viewType.itemType = ItemType.Item
            recyclerViewItems.add(item)
            position = recyclerViewItems.size - 1
            shell.recyclerAdapter?.notifyItemInserted(position)
        }
        return position
    }

    fun addItem(item: RecyclerViewItem<G, I>, group: RecyclerViewGroup<G,I>) {
        if (group.State === DisplayState.Hide) {
            activeGroup(group)
        }
        val site = group.addItem(item)
        addItemToRecyclerViewItems(group, site, item)
        calculatorTitleSite()
    }

    fun addItem(item: RecyclerViewItem<G,I>, groupID: Long) {
        val group = recyclerGroups.find { it.id == groupID }
        group?.let { addItem(item, it) }
    }

    fun removeItem(item: RecyclerViewItem<G, I>) {
        if (item.parentGroups.size == 0) {
            var position = items.indexOf(item.self)
            if (position >= 0){
                items.removeAt(position)
            }
            position = recyclerViewItems.indexOf(item)
            if (position >= 0) {
                recyclerViewItems.removeAt(position)
                shell.recyclerAdapter?.notifyItemRemoved(position)
            }
        } else {
            val tempGroups = item.parentGroups.toList()
            tempGroups.forEach { removeItem(item, it) }
        }
    }

    fun removeItem(
            item: RecyclerViewItem<G,I>,
            group: RecyclerViewGroup<G,I>
    ) {
        val site = group.removeItem(item)
        if (site >= 0) {
            removeItemFromRecyclerViewItems(group, site, item)
            if (group.recyclerViewItems.size == 0) {
                hideGroup(group)
            }
            calculatorTitleSite()
        }
    }

    fun removeItem(item: RecyclerViewItem<G,I>, groupID: Long){
        val group = recyclerGroups.find { it.id == groupID }
        group?.let { removeItem(item, it) }
    }

    fun addItemToRecyclerViewItems(group: RecyclerViewGroup<G, I>, groupSite: Int, item: RecyclerViewItem<G, I>) {
        //根据任务在分组中的位置计算出任务在RecyclerView列表中的位置
        val site = group.groupSiteID + groupSite + 1
        recyclerViewItems.add(site, item)
        shell.recyclerAdapter?.notifyItemInserted(site)
    }

    fun removeItemFromRecyclerViewItems(group: RecyclerViewGroup<G, I>, groupSite: Int, item: RecyclerViewItem<G, I>) {
        val site = group.groupSiteID + groupSite + 1  //从分组中返回的位置是不包括组头的，就是说分组中列表是从0算起的所以+1
        if (recyclerViewItems[site] === item) {
            recyclerViewItems.removeAt(site)
            shell.recyclerAdapter?.notifyItemRemoved(site)
        } else {
            shell.context.toast("要删除的Item与列表中指定位置的记录不相符")
        }
    }

    fun updateItemDisplay(item: RecyclerViewItem<G, I>){
        currentRecyclerItemPosition?.let {
            shell.recyclerAdapter?.notifyItemChanged(it)
        }
    }

    fun updateItemDisplay(item: RecyclerViewItem<G, I>, group: RecyclerViewGroup<G, I>) {
        val site = group.recyclerViewItems.indexOf(item) + group.groupSiteID + 1
        shell.recyclerAdapter?.notifyItemChanged(site)
    }

    fun updateItemDisplay(item: RecyclerViewItem<G, I>, groupID: Long){
        val group = recyclerGroups.find { it.id == groupID }
        group?.let { updateItemDisplay(item, it) }
    }
}