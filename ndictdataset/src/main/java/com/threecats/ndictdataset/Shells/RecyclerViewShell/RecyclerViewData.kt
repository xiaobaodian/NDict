package com.threecats.ndictdataset.Shells.RecyclerViewShell

import org.jetbrains.anko.toast
import java.util.ArrayList

/**
 * 由 zhang 于 2018/3/28 创建
 */
class RecyclerViewData<G, I>(private val shell: RecyclerViewShell<G, I>) {

    var currentGroup: RecyclerViewGroup<G, I>? = null
    var currentItem: RecyclerViewItem<G, I>? = null
    var currentRecyclerGroupPosition: Int? = null
    var currentRecyclerItemPosition: Int? = null

    val recyclerViewGroups: MutableList<RecyclerViewGroup<G, I>> = ArrayList()
    val recyclerViewItems: MutableList<RecyclerViewBaseItem> = ArrayList()
    private val mapRecyclerGroup: HashMap<G, RecyclerViewGroup<G, I>> = hashMapOf()
    private val mapRecyclerItem: HashMap<I, RecyclerViewItem<G, I>> = hashMapOf()

    val groups: MutableList<G> = ArrayList()
    val items: MutableList<I> = ArrayList()

    fun addGroup(group: G){
        val recyclerGroup = RecyclerViewGroup<G, I>(group)
        addGroup(recyclerGroup)
    }

    private fun addGroup(recyclerGroup: RecyclerViewGroup<G, I>){
        if (recyclerViewGroups.size > 0) {
            val lastGroup = recyclerViewGroups[recyclerViewGroups.size - 1]
            lastGroup.nextGroup = recyclerGroup
            recyclerGroup.previousGroup = lastGroup
        }
        mapRecyclerGroup.put(recyclerGroup.self, recyclerGroup)
        recyclerViewGroups.add(recyclerGroup)
        recyclerGroup.parentData = this
        addGroupInRecyclerViewItems(recyclerGroup)
        calculatorTitleSite()
    }

    fun getGroup(id: Long): RecyclerViewGroup<G,I>? {
        val group = recyclerViewGroups.find { it.id == id }
        return group
    }

    fun getItemsCount(): Int {
        var count = 0
        if (recyclerViewGroups.size > 0) {
            recyclerViewGroups.forEach { count += it.recyclerViewItems.size }
        } else {
            count = recyclerViewItems.size
        }

        return count
    }

    fun calculatorTitleSite() {
        var site = 0
        //parentGroups.takeWhile { it.state === DisplayState.Show }.forEach {
        recyclerViewGroups.filter { it.state === DisplayState.Show }.forEach {
            it.groupSiteID = site
            site += it.recyclerViewItems.size + 1
        }
    }

    private fun addGroupInRecyclerViewItems(group: RecyclerViewGroup<G, I>) {
        if (group.state === DisplayState.Hide) return
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
            group.state = DisplayState.Hide
            calculatorTitleSite()
        } else {
            shell.context.toast("HideGroup出现错误")
        }
    }

    fun activeGroup(group: RecyclerViewGroup<G,I>) {
        if (group.state === DisplayState.Show || group.state === DisplayState.Fold) return
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
        group.state = DisplayState.Show
    }

    private fun nextGroupSite(group: RecyclerViewGroup<G,I>): Int {
        var nextGroupSite = -1
        var hasBeforeGroup = true
        for (currentGroup in recyclerViewGroups) {
            if (hasBeforeGroup) {
                if (currentGroup === group) hasBeforeGroup = false
                continue
            }
            if (currentGroup.state === DisplayState.Show) {
                nextGroupSite = currentGroup.groupSiteID
                break
            }
        }
        return nextGroupSite
    }

    fun addItem(item: I): Int{
        var position: Int = -1
        if (groups.isEmpty()) {
            position = addItem(RecyclerViewItem(item))
        } else {
            var lostItem = true
            groups.forEach {
                if (it is GroupMembership) {
                    if (it.isMembers(item)) {
                        addItem(item, it)
                        lostItem = false
                    }
                }
            }
            if (lostItem) {
                // 发出item丢失提醒
            }
        }
        return position
    }

    private fun addItem(recyclerItem: RecyclerViewItem<G, I>): Int{
        var position = -1
        if (recyclerViewGroups.size == 0) {
            items.add(recyclerItem.self)  // 建立I类的实例对象的映射列表
            mapRecyclerItem.put(recyclerItem.self, recyclerItem)
            recyclerItem.viewType.itemType = ItemType.Item
            recyclerViewItems.add(recyclerItem)
            position = recyclerViewItems.size - 1
            shell.recyclerAdapter?.notifyItemInserted(position)
        }
        return position
    }

    fun addItem(item: I, group: G): Int{
        var position = -1
        val recyclerGroup = mapRecyclerGroup[group]
        recyclerGroup?.let {
            //val recyclerItem = RecyclerViewItem<G, I>(item)
            position = addItem(RecyclerViewItem(item), recyclerGroup)
        }
        return position
    }

    private fun addItem(recyclerItem: RecyclerViewItem<G, I>, recyclerGroup: RecyclerViewGroup<G,I>): Int {
        if (recyclerGroup.state === DisplayState.Hide) {
            activeGroup(recyclerGroup)
        }
        val position = recyclerGroup.addItem(recyclerItem)
        mapRecyclerItem.put(recyclerItem.self, recyclerItem)
        addItemToRecyclerViewItems(recyclerGroup, position, recyclerItem)
        calculatorTitleSite()
        return position
    }

    private fun addItem(recyclerItem: RecyclerViewItem<G,I>, groupID: Long) {
        val group = recyclerViewGroups.find { it.id == groupID }
        group?.let { addItem(recyclerItem, it) }
    }

    fun removeItem(item: I){
        val recyclerItem = mapRecyclerItem[item]
        recyclerItem?.let{
            removeItem(recyclerItem)
            mapRecyclerItem.remove(item)
        }
    }

    private fun removeItem(recyclerItem: RecyclerViewItem<G, I>) {
        if (recyclerItem.parentGroups.size == 0) {
            var position = items.indexOf(recyclerItem.self)
            if (position >= 0){
                items.removeAt(position)
            }
            position = recyclerViewItems.indexOf(recyclerItem)
            if (position >= 0) {
                recyclerViewItems.removeAt(position)
                shell.recyclerAdapter?.notifyItemRemoved(position)
            }
        } else {
            val tempGroups = recyclerItem.parentGroups.toList()
            tempGroups.forEach { removeItem(recyclerItem, it) }
        }
    }

    fun removeItem(item: I, group: G){
        val recyclerItem = mapRecyclerItem[item]
        val recyclerGroup = mapRecyclerGroup[group]
        if (recyclerItem != null && recyclerGroup != null) {
            removeItem(recyclerItem, recyclerGroup)
        }
    }

    private fun removeItem(
            recyclerItem: RecyclerViewItem<G,I>,
            recyclerGroup: RecyclerViewGroup<G,I>
    ) {
        val site = recyclerGroup.removeItem(recyclerItem)
        if (site >= 0) {
            removeItemFromRecyclerViewItems(recyclerGroup, site, recyclerItem)
            if (recyclerGroup.recyclerViewItems.size == 0) {
                hideGroup(recyclerGroup)
            }
            calculatorTitleSite()
        }
    }

    fun removeItem(item: RecyclerViewItem<G,I>, groupID: Long){
        val group = recyclerViewGroups.find { it.id == groupID }
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

    fun updateItemDisplay(item: I){
        currentRecyclerItemPosition?.let {
            shell.recyclerAdapter?.notifyItemChanged(it)
        }
    }

    private fun updateItemDisplay(item: RecyclerViewItem<G, I>, group: RecyclerViewGroup<G, I>) {
        val site = group.recyclerViewItems.indexOf(item) + group.groupSiteID + 1
        shell.recyclerAdapter?.notifyItemChanged(site)
    }

    fun updateItemDisplay(item: RecyclerViewItem<G, I>, groupID: Long){
        val group = recyclerViewGroups.find { it.id == groupID }
        group?.let { updateItemDisplay(item, it) }
    }
}