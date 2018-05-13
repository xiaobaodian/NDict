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
    var adapter: RecyclerViewAdapter<G,I>? = null
    //private val mapRecyclerGroup: MutableMap<G, RecyclerViewGroup<G, I>> = mutableMapOf()  // mutableMapOf
    //private val mapRecyclerItem: MutableMap<I, RecyclerViewItem<G, I>> = mutableMapOf()
    private val mapRecyclerGroup: ItemMap<G, RecyclerViewGroup<G, I>> = ItemMap()
    private val mapRecyclerItem: ItemMap<I, RecyclerViewItem<G, I>> = ItemMap()

    val hasGroup: Boolean
        get() = !recyclerViewGroups.isEmpty()
    val noGroup: Boolean
        get() = recyclerViewGroups.isEmpty()
//    val groups: List<G>                                 // = ArrayList()
//        get() = mapRecyclerGroup.keys.toList()
//    val items: List<I>                                  // = ArrayList()
//        get() = mapRecyclerItem.keys.toList()

    fun addGroup(group: G){
        val recyclerGroup = RecyclerViewGroup<G, I>(group)
        addGroup(recyclerGroup)
    }

    private fun addGroup(recyclerGroup: RecyclerViewGroup<G, I>){
        if (hasGroup) {
            //val lastGroup = recyclerViewGroups[recyclerViewGroups.size - 1]
            val lastGroup = recyclerViewGroups.last()
            lastGroup.nextGroup = recyclerGroup
            recyclerGroup.previousGroup = lastGroup
        }
        mapRecyclerGroup.put(recyclerGroup.self, recyclerGroup)
        recyclerViewGroups.add(recyclerGroup)
        recyclerGroup.parentData = this
        addGroupInRecyclerViewItems(recyclerGroup)
        calculatorGroupPosition()
    }

    fun getGroup(id: Long): RecyclerViewGroup<G,I>? {
        val group = recyclerViewGroups.find { it.id == id }
        return group
    }

    fun getItemsCount(): Int {
        var count = 0
        if (hasGroup) {
            recyclerViewGroups.forEach { count += it.groupItems.size }
        } else {
            count = recyclerViewItems.size
        }
        return count
    }

    private fun calculatorGroupPosition() {
        var position = 0
        recyclerViewGroups.forEach {
            if (it.state == DisplayState.Show) {
                it.groupPositionID = position
                position += it.items.size + 1
            }
        }
    }

    private fun addGroupInRecyclerViewItems(group: RecyclerViewGroup<G, I>) {
        if (group.state === DisplayState.Hide) return
        recyclerViewItems.add(group)
        recyclerViewItems.addAll(group.groupItems)
    }

    private fun hideGroup(group: RecyclerViewGroup<G,I>) {
        if (noGroup) return
        if (group.groupPositionID > recyclerViewItems.size - 1) {
            shell.context.toast("Hide Group : group.groupPositionID > size()")
            return
        }
        if (group === recyclerViewItems[group.groupPositionID]) {
            recyclerViewItems.removeAt(group.groupPositionID)
            adapter?.notifyItemRemoved(group.groupPositionID)
            group.state = DisplayState.Hide
            calculatorGroupPosition()
        } else {
            shell.context.toast("隐藏组出现错误")
        }
    }

    fun activeGroup(group: RecyclerViewGroup<G,I>) {
        if (group.state === DisplayState.Show || group.state === DisplayState.Fold) return
        val nextGroupPosition = nextGroupPosition(group)
        if (nextGroupPosition >= 0) {
            group.groupPositionID = nextGroupPosition
            recyclerViewItems.add(nextGroupPosition, group)
            adapter?.notifyItemInserted(nextGroupPosition)
            calculatorGroupPosition()
        } else {
            //如果没有下一组，就表明当前组就是最后一组，不用插入，只需要添加
            recyclerViewItems.add(group)
            group.groupPositionID = recyclerViewItems.size - 1
            adapter?.notifyItemInserted(group.groupPositionID)
        }
        group.state = DisplayState.Show
    }

    private fun nextGroupPosition(group: RecyclerViewGroup<G,I>): Int {
        var nextGroupSite = -1
        var hasBeforeGroup = true
        for (currentGroup in recyclerViewGroups) {
            if (hasBeforeGroup) {
                if (currentGroup === group) hasBeforeGroup = false
                continue
            }
            if (currentGroup.state == DisplayState.Show) {
                nextGroupSite = currentGroup.groupPositionID
                break
            }
        }
        return nextGroupSite
    }

    fun addItem(item: I): Int{
        var position: Int = -1
        if (noGroup) {
            position = addItem(RecyclerViewItem(item))
        } else {
            var isLostItem = true
            val recyclerItem = RecyclerViewItem<G,I>(item)
            mapRecyclerItem.put(item, recyclerItem)
            recyclerViewGroups.forEach {
                val group = it.self
                if (group is GroupMembership) {
                    if (group.isMembers(item as Any)) {
                        if (addItem(recyclerItem, it) > -1) isLostItem = false
                    }
                }
            }
            if (isLostItem) {
                mapRecyclerItem.remove(item)
                mapRecyclerItem.remove(item)
                shell.context.toast("丢失了记录：${item.toString()}")
            }
        }
        return position
    }

    private fun addItem(recyclerItem: RecyclerViewItem<G, I>): Int{
        var position = -1
        if (noGroup) {
            //items.add(recyclerItem.self)  // 建立I类的实例对象的映射列表
            mapRecyclerItem.put(recyclerItem.self, recyclerItem)
            recyclerItem.viewType.itemType = ItemType.Item
            recyclerViewItems.add(recyclerItem)
            position = recyclerViewItems.size - 1
            adapter?.notifyItemInserted(position)
        }
        return position
    }

    fun addItem(item: I, group: G): Int{
        var position = -1
        val recyclerGroup = findRecyclerGroup(group)
        recyclerGroup?.let {
            //val recyclerItem = RecyclerViewItem<G, I>(item)
            position = addItem(RecyclerViewItem(item), recyclerGroup)
        }
        return position
    }

    private fun addItem(recyclerItem: RecyclerViewItem<G, I>, recyclerGroup: RecyclerViewGroup<G,I>): Int {
        if (recyclerGroup.state === DisplayState.Hide) { activeGroup(recyclerGroup) }
        val position = recyclerGroup.addItem(recyclerItem)
        addItemToRecyclerViewItems(recyclerGroup, position, recyclerItem)
        calculatorGroupPosition()
        return position
    }

    private fun addItem(recyclerItem: RecyclerViewItem<G,I>, groupID: Long) {
        val group = recyclerViewGroups.find { it.id == groupID }
        group?.let { addItem(recyclerItem, it) }
    }

    fun removeItem(item: I){
        val recyclerItem = findRecyclerItem(item)
        recyclerItem?.let{
            removeItem(it)
            mapRecyclerItem.remove(item)
        }
    }

    private fun removeItem(recyclerItem: RecyclerViewItem<G, I>) {
        if (recyclerItem.parentGroups.isEmpty()) {
            val position = recyclerViewItems.indexOf(recyclerItem)
            if (position >= 0) {
                recyclerViewItems.removeAt(position)
                adapter?.notifyItemRemoved(position)
            }
        } else {
            val tempGroups = recyclerItem.parentGroups.toList()
            tempGroups.forEach { removeItem(recyclerItem, it) }
        }
    }

    fun removeItem(item: I, group: G){
        val recyclerItem = findRecyclerItem(item)
        val recyclerGroup = findRecyclerGroup(group)
        if (recyclerItem != null && recyclerGroup != null) {
            removeItem(recyclerItem, recyclerGroup)
        }
    }

    private fun removeItem(
            recyclerItem: RecyclerViewItem<G,I>,
            recyclerGroup: RecyclerViewGroup<G,I>
    ) {
        val position = recyclerGroup.removeItem(recyclerItem)
        if (position >= 0) {
            removeItemFromRecyclerViewItems(recyclerGroup, position, recyclerItem)
            if (recyclerGroup.groupItems.size == 0) {
                hideGroup(recyclerGroup)
            }
            calculatorGroupPosition()
        }
    }

    fun removeItem(recyclerItem: RecyclerViewItem<G,I>, groupID: Long){
        val group = recyclerViewGroups.find { it.id == groupID }
        group?.let { removeItem(recyclerItem, it) }
    }

    fun updateItem(item: I){
        val recyclerItem = findRecyclerItem(item)
        recyclerItem?.let {
            if (noGroup) {
                updateCurrentItemDisplay(item)
            } else {
                updateItem(recyclerItem)
            }
        }
    }

    private fun updateItem(recyclerItem: RecyclerViewItem<G,I>){
        val moveToGroups: MutableList<RecyclerViewGroup<G, I>> = ArrayList()
        val holdToGroups: MutableList<RecyclerViewGroup<G, I>> = ArrayList()
        val parentGroups: MutableList<RecyclerViewGroup<G, I>> = ArrayList()
        moveToGroups.addAll(recyclerViewGroups)
        parentGroups.addAll(recyclerItem.parentGroups)

        parentGroups.forEach {
            val group = it.self
            if (group is GroupMembership) {
                if (group.isMembers(recyclerItem.self as Any)) {
                    moveToGroups.remove(it)
                    holdToGroups.add(it)
                } else {
                    removeItem(recyclerItem, it)
                    recyclerItem.parentGroups.remove(it)
                }
            }
        }
        holdToGroups.forEach { updateItemDisplay(recyclerItem, it) }
        moveToGroups.forEach {
            val group = it.self
            if (group is GroupMembership) {
                if (group.isMembers(recyclerItem.self as Any)) {
                    addItem(recyclerItem, it)
                }
            }
        }
    }

    private fun findRecyclerItem(item: I): RecyclerViewItem<G, I>? = if (currentItem?.self == item) currentItem else mapRecyclerItem.get(item)

    private fun findRecyclerGroup(group: G): RecyclerViewGroup<G, I>? = if (currentGroup?.self == group) currentGroup else mapRecyclerGroup.get(group)

//    var recyclerGroup: RecyclerViewGroup<G, I>? = null
//    recyclerGroup = if (currentGroup?.self == group) currentGroup else mapRecyclerGroup.get(group)
//    return recyclerGroup

    private fun addItemToRecyclerViewItems(group: RecyclerViewGroup<G, I>, groupSite: Int, item: RecyclerViewItem<G, I>) {
        //根据任务在分组中的位置计算出任务在RecyclerView列表中的位置
        val site = group.groupPositionID + groupSite + 1
        if (site >= recyclerViewItems.size) {
            recyclerViewItems.add(item)
        } else {
            recyclerViewItems.add(site, item)
        }
        adapter?.notifyItemInserted(site)
        //shell.recyclerAdapter?.notifyDataSetChanged()
    }

    private fun removeItemFromRecyclerViewItems(group: RecyclerViewGroup<G, I>, groupSite: Int, item: RecyclerViewItem<G, I>) {
        val site = group.groupPositionID + groupSite + 1  //从分组中返回的位置是不包括组头的，就是说分组中列表是从0算起的所以+1
        if (recyclerViewItems[site] === item) {
            recyclerViewItems.removeAt(site)
            adapter?.notifyItemRemoved(site)
        } else {
            shell.context.toast("注意：要删除的Item与列表中指定位置的记录不相符 ！")
        }
    }

    private fun updateCurrentItemDisplay(item: I){
        currentRecyclerItemPosition?.let {
            adapter?.notifyItemChanged(it)
        }
    }

    private fun updateItemDisplay(recyclerItem: RecyclerViewItem<G, I>, recyclerGroup: RecyclerViewGroup<G, I>) {
        val site = recyclerGroup.groupItems.indexOf(recyclerItem) + recyclerGroup.groupPositionID + 1
        adapter?.notifyItemChanged(site)
    }

}