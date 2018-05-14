package com.threecats.ndictdataset.Shells.RecyclerViewShell

import org.jetbrains.anko.toast
import java.util.ArrayList

/**
 * 由 zhang 于 2018/3/28 创建
 */

class RecyclerViewData<G, I>(private val shell: RecyclerViewShell<G, I>) {

    var currentNode: RecyclerViewNode<G, I>? = null
    var currentItem: RecyclerViewItem<G, I>? = null
    var currentRecyclerNodePosition: Int? = null
    var currentRecyclerItemPosition: Int? = null

    val recyclerViewNodes: MutableList<RecyclerViewNode<G, I>> = ArrayList()
    val recyclerViewItems: MutableList<RecyclerViewBaseItem> = ArrayList()
    var adapter: RecyclerViewAdapter<G,I>? = null
    //private val mapRecyclerNode: MutableMap<G, RecyclerViewNode<G, I>> = mutableMapOf()  // mutableMapOf
    //private val mapRecyclerItem: MutableMap<I, RecyclerViewItem<G, I>> = mutableMapOf()
    private val mapRecyclerNode: ItemMap<G, RecyclerViewNode<G, I>> = ItemMap()
    private val mapRecyclerItem: ItemMap<I, RecyclerViewItem<G, I>> = ItemMap()

    val hasNode: Boolean
        get() = !recyclerViewNodes.isEmpty()
    val noNode: Boolean
        get() = recyclerViewNodes.isEmpty()
//    val groups: List<G>                                 // = ArrayList()
//        get() = mapRecyclerNode.keys.toList()
//    val items: List<I>                                  // = ArrayList()
//        get() = mapRecyclerItem.keys.toList()

    fun addNode(node: G){
        addNode(RecyclerViewNode<G, I>(node))
    }

    private fun addNode(recyclerNode: RecyclerViewNode<G, I>){
        if (hasNode) {
            //val lastGroup = recyclerViewNodes[recyclerViewNodes.size - 1]
            val lastGroup = recyclerViewNodes.last()
            lastGroup.nextNode = recyclerNode
            recyclerNode.previousNode = lastGroup
        }
        mapRecyclerNode.put(recyclerNode.self, recyclerNode)
        recyclerViewNodes.add(recyclerNode)
        recyclerNode.parentData = this
        addNodeInRecyclerViewItems(recyclerNode)
        calculatorNodePosition()
    }

    fun getItemsCount(): Int {
        var count = 0
        if (hasNode) {
            recyclerViewNodes.forEach { count += it.items.size }
        } else {
            count = recyclerViewItems.size
        }
        return count
    }

    private fun calculatorNodePosition() {
        var position = 0
        recyclerViewNodes.forEach {
            if (it.state == DisplayState.Show) {
                it.nodePositionID = position
                position += it.items.size + 1
            }
        }
    }

    private fun addNodeInRecyclerViewItems(node: RecyclerViewNode<G, I>) {
        if (node.state === DisplayState.Hide) return
        recyclerViewItems.add(node)
        recyclerViewItems.addAll(node.items)
    }

    private fun hideNode(node: RecyclerViewNode<G,I>) {
        if (noNode) return
        if (node.nodePositionID > recyclerViewItems.size - 1) {
            shell.context.toast("Hide Node : node.nodePositionID > size()")
            return
        }
        if (node === recyclerViewItems[node.nodePositionID]) {
            recyclerViewItems.removeAt(node.nodePositionID)
            adapter?.notifyItemRemoved(node.nodePositionID)
            node.state = DisplayState.Hide
            calculatorNodePosition()
        } else {
            shell.context.toast("隐藏组出现错误")
        }
    }

    fun activeNode(node: RecyclerViewNode<G,I>) {
        if (node.state === DisplayState.Show || node.state === DisplayState.Fold) return
        val nextGroupPosition = nextNodePosition(node)
        if (nextGroupPosition >= 0) {
            node.nodePositionID = nextGroupPosition
            recyclerViewItems.add(nextGroupPosition, node)
            adapter?.notifyItemInserted(nextGroupPosition)
            calculatorNodePosition()
        } else {
            //如果没有下一组，就表明当前组就是最后一组，不用插入，只需要添加
            recyclerViewItems.add(node)
            node.nodePositionID = recyclerViewItems.size - 1
            adapter?.notifyItemInserted(node.nodePositionID)
        }
        node.state = DisplayState.Show
    }

    private fun nextNodePosition(node: RecyclerViewNode<G,I>): Int {
        var nextNodePosition = -1
        var hasBeforeNode = true
        for (currentNode in recyclerViewNodes) {
            if (hasBeforeNode) {
                if (currentNode === node) hasBeforeNode = false
                continue
            }
            if (currentNode.state == DisplayState.Show) {
                nextNodePosition = currentNode.nodePositionID
                break
            }
        }
        return nextNodePosition
    }

    fun addItem(item: I): Int{
        var position: Int = -1
        if (noNode) {
            position = addItem(RecyclerViewItem(item))
        } else {
            var isLostItem = true
            val recyclerItem = RecyclerViewItem<G,I>(item)
            mapRecyclerItem.put(item, recyclerItem)
            recyclerViewNodes.forEach {
                val node = it.self
                if (node is NodeMembership) {
                    if (node.isMembers(item as Any)) {
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
        if (noNode) {
            //items.add(recyclerItem.self)  // 建立I类的实例对象的映射列表
            mapRecyclerItem.put(recyclerItem.self, recyclerItem)
            recyclerItem.viewType.itemType = ItemType.Item
            recyclerViewItems.add(recyclerItem)
            position = recyclerViewItems.size - 1
            adapter?.notifyItemInserted(position)
        }
        return position
    }

    fun addItem(item: I, node: G): Int{
        var position = -1
        val recyclerNode = findRecyclerNode(node)
        recyclerNode?.let {
            position = addItem(RecyclerViewItem(item), recyclerNode)
        }
        return position
    }

    private fun addItem(recyclerItem: RecyclerViewItem<G, I>, recyclerNode: RecyclerViewNode<G,I>): Int {
        if (recyclerNode.state === DisplayState.Hide) { activeNode(recyclerNode) }
        val position = recyclerNode.addItem(recyclerItem)
        addItemToRecyclerViewItems(recyclerNode, position, recyclerItem)
        calculatorNodePosition()
        return position
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
            val tempNodes = recyclerItem.parentNodes.toList()
            tempNodes.forEach { removeItem(recyclerItem, it) }
        }
    }

    fun removeItem(item: I, node: G){
        val recyclerItem = findRecyclerItem(item)
        val recyclerNode = findRecyclerNode(node)
        if (recyclerItem != null && recyclerNode != null) {
            removeItem(recyclerItem, recyclerNode)
        }
    }

    private fun removeItem(
            recyclerItem: RecyclerViewItem<G,I>,
            recyclerNode: RecyclerViewNode<G,I>
    ) {
        val position = recyclerNode.removeItem(recyclerItem)
        if (position >= 0) {
            removeItemFromRecyclerViewItems(recyclerNode, position, recyclerItem)
            if (recyclerNode.items.size == 0) {
                hideNode(recyclerNode)
            }
            calculatorNodePosition()
        }
    }

    fun updateItem(item: I){
        val recyclerItem = findRecyclerItem(item)
        recyclerItem?.let {
            if (noNode) {
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
            val node = it.self
            if (node is NodeMembership) {
                if (node.isMembers(recyclerItem.self as Any)) {
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
            val node = it.self
            if (node is NodeMembership) {
                if (node.isMembers(recyclerItem.self as Any)) {
                    addItem(recyclerItem, it)
                }
            }
        }
    }

    private fun findRecyclerItem(item: I): RecyclerViewItem<G, I>? = if (currentItem?.self == item) currentItem else mapRecyclerItem.get(item)

    private fun findRecyclerNode(group: G): RecyclerViewNode<G, I>? = if (currentNode?.self == group) currentNode else mapRecyclerNode.get(group)

//    var recyclerGroup: RecyclerViewNode<G, I>? = null
//    recyclerGroup = if (currentNode?.self == node) currentNode else mapRecyclerNode.get(node)
//    return recyclerGroup

    private fun addItemToRecyclerViewItems(node: RecyclerViewNode<G, I>, nodePosition: Int, item: RecyclerViewItem<G, I>) {
        //根据任务在分组中的位置计算出任务在RecyclerView列表中的位置
        val site = node.nodePositionID + nodePosition + 1
        if (site >= recyclerViewItems.size) {
            recyclerViewItems.add(item)
        } else {
            recyclerViewItems.add(site, item)
        }
        adapter?.notifyItemInserted(site)
        //shell.recyclerAdapter?.notifyDataSetChanged()
    }

    private fun removeItemFromRecyclerViewItems(node: RecyclerViewNode<G, I>, nodePosition: Int, item: RecyclerViewItem<G, I>) {
        val site = node.nodePositionID + nodePosition + 1  //从分组中返回的位置是不包括组头的，就是说分组中列表是从0算起的所以+1
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
        val site = recyclerNode.items.indexOf(recyclerItem) + recyclerNode.nodePositionID + 1
        adapter?.notifyItemChanged(site)
    }

}