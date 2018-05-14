package com.threecats.ndictdataset.Shells.RecyclerViewShell

import org.jetbrains.anko.toast
import java.util.ArrayList

/**
 * 由 zhang 于 2018/3/28 创建
 */

class RecyclerViewData<G, I>(private val shell: RecyclerViewShell<G, I>) {

    var currentNode: RecyclerViewNode<G, I>? = null
    var currentItem: RecyclerViewItem<G, I>? = null
    var currentRecyclerGroupPosition: Int? = null
    var currentRecyclerItemPosition: Int? = null

    val recyclerViewNodes: MutableList<RecyclerViewNode<G, I>> = ArrayList()
    val recyclerViewItems: MutableList<RecyclerViewBaseItem> = ArrayList()
    var adapter: RecyclerViewAdapter<G,I>? = null
    //private val mapRecyclerNode: MutableMap<G, RecyclerViewNode<G, I>> = mutableMapOf()  // mutableMapOf
    //private val mapRecyclerItem: MutableMap<I, RecyclerViewItem<G, I>> = mutableMapOf()
    private val mapRecyclerNode: ItemMap<G, RecyclerViewNode<G, I>> = ItemMap()
    private val mapRecyclerItem: ItemMap<I, RecyclerViewItem<G, I>> = ItemMap()

    val hasGroup: Boolean
        get() = !recyclerViewNodes.isEmpty()
    val noGroup: Boolean
        get() = recyclerViewNodes.isEmpty()
//    val groups: List<G>                                 // = ArrayList()
//        get() = mapRecyclerNode.keys.toList()
//    val items: List<I>                                  // = ArrayList()
//        get() = mapRecyclerItem.keys.toList()

    fun addGroup(group: G){
        val recyclerGroup = RecyclerViewNode<G, I>(group)
        addGroup(recyclerGroup)
    }

    private fun addGroup(recyclerNode: RecyclerViewNode<G, I>){
        if (hasGroup) {
            //val lastGroup = recyclerViewNodes[recyclerViewNodes.size - 1]
            val lastGroup = recyclerViewNodes.last()
            lastGroup.nextNode = recyclerNode
            recyclerNode.previousNode = lastGroup
        }
        mapRecyclerNode.put(recyclerNode.self, recyclerNode)
        recyclerViewNodes.add(recyclerNode)
        recyclerNode.parentData = this
        addGroupInRecyclerViewItems(recyclerNode)
        calculatorGroupPosition()
    }

    fun getGroup(id: Long): RecyclerViewNode<G,I>? {
        val group = recyclerViewNodes.find { it.id == id }
        return group
    }

    fun getItemsCount(): Int {
        var count = 0
        if (hasGroup) {
            recyclerViewNodes.forEach { count += it.groupItems.size }
        } else {
            count = recyclerViewItems.size
        }
        return count
    }

    private fun calculatorGroupPosition() {
        var position = 0
        recyclerViewNodes.forEach {
            if (it.state == DisplayState.Show) {
                it.nodePositionID = position
                position += it.items.size + 1
            }
        }
    }

    private fun addGroupInRecyclerViewItems(node: RecyclerViewNode<G, I>) {
        if (node.state === DisplayState.Hide) return
        recyclerViewItems.add(node)
        recyclerViewItems.addAll(node.groupItems)
    }

    private fun hideGroup(node: RecyclerViewNode<G,I>) {
        if (noGroup) return
        if (node.nodePositionID > recyclerViewItems.size - 1) {
            shell.context.toast("Hide Group : node.nodePositionID > size()")
            return
        }
        if (node === recyclerViewItems[node.nodePositionID]) {
            recyclerViewItems.removeAt(node.nodePositionID)
            adapter?.notifyItemRemoved(node.nodePositionID)
            node.state = DisplayState.Hide
            calculatorGroupPosition()
        } else {
            shell.context.toast("隐藏组出现错误")
        }
    }

    fun activeGroup(node: RecyclerViewNode<G,I>) {
        if (node.state === DisplayState.Show || node.state === DisplayState.Fold) return
        val nextGroupPosition = nextGroupPosition(node)
        if (nextGroupPosition >= 0) {
            node.nodePositionID = nextGroupPosition
            recyclerViewItems.add(nextGroupPosition, node)
            adapter?.notifyItemInserted(nextGroupPosition)
            calculatorGroupPosition()
        } else {
            //如果没有下一组，就表明当前组就是最后一组，不用插入，只需要添加
            recyclerViewItems.add(node)
            node.nodePositionID = recyclerViewItems.size - 1
            adapter?.notifyItemInserted(node.nodePositionID)
        }
        node.state = DisplayState.Show
    }

    private fun nextGroupPosition(node: RecyclerViewNode<G,I>): Int {
        var nextGroupSite = -1
        var hasBeforeGroup = true
        for (currentGroup in recyclerViewNodes) {
            if (hasBeforeGroup) {
                if (currentGroup === node) hasBeforeGroup = false
                continue
            }
            if (currentGroup.state == DisplayState.Show) {
                nextGroupSite = currentGroup.nodePositionID
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
            recyclerViewNodes.forEach {
                val group = it.self
                if (group is GroupMembership) {
                    if (group.isMembers(item as Any)) {
                        if (addItem(recyclerItem, it) > -1) isLostItem = false
                    }
                }
            }
            if (isLostItem) {
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
            position = addItem(RecyclerViewItem(item), recyclerGroup)
        }
        return position
    }

    private fun addItem(recyclerItem: RecyclerViewItem<G, I>, recyclerNode: RecyclerViewNode<G,I>): Int {
        if (recyclerNode.state === DisplayState.Hide) { activeGroup(recyclerNode) }
        val position = recyclerNode.addItem(recyclerItem)
        addItemToRecyclerViewItems(recyclerNode, position, recyclerItem)
        calculatorGroupPosition()
        return position
    }

    private fun addItem(recyclerItem: RecyclerViewItem<G,I>, groupID: Long) {
        val group = recyclerViewNodes.find { it.id == groupID }
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
        if (recyclerItem.parentNodes.isEmpty()) {
            val position = recyclerViewItems.indexOf(recyclerItem)
            if (position >= 0) {
                recyclerViewItems.removeAt(position)
                adapter?.notifyItemRemoved(position)
            }
        } else {
            val tempGroups = recyclerItem.parentNodes.toList()
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
            recyclerNode: RecyclerViewNode<G,I>
    ) {
        val position = recyclerNode.removeItem(recyclerItem)
        if (position >= 0) {
            removeItemFromRecyclerViewItems(recyclerNode, position, recyclerItem)
            if (recyclerNode.groupItems.size == 0) {
                hideGroup(recyclerNode)
            }
            calculatorGroupPosition()
        }
    }

    fun removeItem(recyclerItem: RecyclerViewItem<G,I>, groupID: Long){
        val group = recyclerViewNodes.find { it.id == groupID }
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
        val moveToNodes: MutableList<RecyclerViewNode<G, I>> = ArrayList()
        val holdToNodes: MutableList<RecyclerViewNode<G, I>> = ArrayList()
        val parentNodes: MutableList<RecyclerViewNode<G, I>> = ArrayList()
        moveToNodes.addAll(recyclerViewNodes)
        parentNodes.addAll(recyclerItem.parentNodes)

        parentNodes.forEach {
            val group = it.self
            if (group is GroupMembership) {
                if (group.isMembers(recyclerItem.self as Any)) {
                    moveToNodes.remove(it)
                    holdToNodes.add(it)
                } else {
                    removeItem(recyclerItem, it)
                    recyclerItem.parentNodes.remove(it)
                }
            }
        }
        holdToNodes.forEach { updateItemDisplay(recyclerItem, it) }
        moveToNodes.forEach {
            val group = it.self
            if (group is GroupMembership) {
                if (group.isMembers(recyclerItem.self as Any)) {
                    addItem(recyclerItem, it)
                }
            }
        }
    }

    private fun findRecyclerItem(item: I): RecyclerViewItem<G, I>? = if (currentItem?.self == item) currentItem else mapRecyclerItem.get(item)

    private fun findRecyclerGroup(group: G): RecyclerViewNode<G, I>? = if (currentNode?.self == group) currentNode else mapRecyclerNode.get(group)

//    var recyclerGroup: RecyclerViewNode<G, I>? = null
//    recyclerGroup = if (currentNode?.self == node) currentNode else mapRecyclerNode.get(node)
//    return recyclerGroup

    private fun addItemToRecyclerViewItems(node: RecyclerViewNode<G, I>, groupSite: Int, item: RecyclerViewItem<G, I>) {
        //根据任务在分组中的位置计算出任务在RecyclerView列表中的位置
        val site = node.nodePositionID + groupSite + 1
        if (site >= recyclerViewItems.size) {
            recyclerViewItems.add(item)
        } else {
            recyclerViewItems.add(site, item)
        }
        adapter?.notifyItemInserted(site)
        //shell.recyclerAdapter?.notifyDataSetChanged()
    }

    private fun removeItemFromRecyclerViewItems(node: RecyclerViewNode<G, I>, groupSite: Int, item: RecyclerViewItem<G, I>) {
        val site = node.nodePositionID + groupSite + 1  //从分组中返回的位置是不包括组头的，就是说分组中列表是从0算起的所以+1
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

    private fun updateItemDisplay(recyclerItem: RecyclerViewItem<G, I>, recyclerNode: RecyclerViewNode<G, I>) {
        val site = recyclerNode.groupItems.indexOf(recyclerItem) + recyclerNode.nodePositionID + 1
        adapter?.notifyItemChanged(site)
    }

}