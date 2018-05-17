package com.threecats.ndict.Shells.RecyclerViewShell

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

/**
 * 由 zhang 于 2018/3/28 创建
 */

class RecyclerViewAdapter<G, I>(
        private val dataSet: RecyclerViewData<G,I>,
        private val shell: RecyclerViewShell<G, I>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isChecked = false
    private var isNullData: Boolean? = null

    inner class ItemViewHolder(internal var currentItemView: View) : RecyclerView.ViewHolder(currentItemView) {
        //internal var checkBox: CheckBox? = null
        val item: RecyclerViewItem<G, I>
            @Suppress("UNCHECKED_CAST")
            get() = dataSet.recyclerViewItems[adapterPosition] as RecyclerViewItem<G, I>


//        init {
//            checkBox = currentItemView.findViewById(R.id.checkBox)
//            checkBox?.let {
//                it.setOnClickListener { v ->
//                    if (checkBox == null) {
//                        Toast.makeText(v.context, "Check Box is NULL !!!", Toast.LENGTH_SHORT).show()
//                        return@checkBox.setOnClickListener
//                    }
//                    val task = checkBox!!.ID as Task
//                    if (checkBox!!.isChecked) {
//                        task.setChecked(true)
//                    } else {
//                        task.setChecked(false)
//                    }
//                    //这里可以加入抽象方法，实现用户定制的点击CheckBox动作
//                    //doClickCheckBox();
//                }
//
//            }
//        }

        fun displayText(R: Int, text: String): ItemViewHolder {
            val textView = currentItemView.findViewById<TextView>(R)
            if (text.isEmpty()) {
                textView.visibility = View.GONE
            } else {
                textView.visibility = View.VISIBLE
                textView.text = text
            }
            return this@ItemViewHolder
        }

        fun displayImage(R: Int, imageResId: Int): ItemViewHolder {
            val imageView = currentItemView.findViewById<ImageView>(R)
            imageView.setImageResource(imageResId)
            return this@ItemViewHolder
        }
    }

    inner class NodeViewHolder(internal var currentNodeView: View) : RecyclerView.ViewHolder(currentNodeView) {

        val node: RecyclerViewNode<G, I>
            @Suppress("UNCHECKED_CAST")
            get() = dataSet.recyclerViewItems[adapterPosition] as RecyclerViewNode<G, I>

        fun displayText(R: Int, text: String): NodeViewHolder {
            val textView = currentNodeView.findViewById<TextView>(R)
            textView.text = text
            return this@NodeViewHolder
        }

        fun displayImage(R: Int, imageResId: Int): NodeViewHolder {
            val imageView = currentNodeView.findViewById<ImageView>(R)
            imageView.setImageResource(imageResId)
            return this@NodeViewHolder
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val type = shell.viewTypes[viewType]  //这里viewType参数传递的是type在viewTypes中的下标
        val view: View

        when (type.itemType) {
            ItemType.Item -> {
                view = LayoutInflater.from(parent.context).inflate(type.layoutID!!, parent, false)
                val itemViewHolder = ItemViewHolder(view)
                itemViewHolder.currentItemView.setOnClickListener {
                    val item = itemViewHolder.item
                    dataSet.currentItem = item
                    dataSet.currentRecyclerItemPosition = itemViewHolder.adapterPosition
                    //App.self().getDataManger().setCurrentTask(task)
//                    if (isChecked) {
//                        itemViewHolder.checkBox!!.isChecked = !task.getChecked()
//                        task.setChecked(itemViewHolder.checkBox!!.isChecked)
//                    } else {
//                        //Intent taskIntent = new Intent(App.getMainActivity(),TaskDetailsActivity.class);
//                        val taskIntent = Intent(App.self().getMainActivity(), TaskDisplayActivity::class.java)
//                        App.self().getMainActivity().startActivity(taskIntent)
//                    }
                    if (dataSet.hasNode) {
                        findCurrentNode(itemViewHolder.adapterPosition)
                    }
                    shell.clickItem(item.self, itemViewHolder)
                }
                itemViewHolder.currentItemView.setOnLongClickListener {
                    //if (isChecked) return@itemViewHolder.currentItemView.setOnLongClickListener false
                    val item = itemViewHolder.item
                    dataSet.currentItem = item
                    dataSet.currentRecyclerItemPosition = itemViewHolder.adapterPosition
                    //App.self().getDataManger().setCurrentTask(task)
                    //暂时关闭长安多选功能
                    //App.getDataManger().getCurrentGroupList().setItemChecked(true);
                    if (dataSet.hasNode) {
                        findCurrentNode(itemViewHolder.adapterPosition)
                    }
                    shell.longClickItem(item.self, itemViewHolder)
                    true
                }
                return itemViewHolder
            }

            ItemType.Node -> {
                view = LayoutInflater.from(parent.context).inflate(type.layoutID!!, parent, false)
                val nodeViewHolder = NodeViewHolder(view)
                nodeViewHolder.currentNodeView.setOnClickListener {
                    val group = nodeViewHolder.node as RecyclerViewNode<G, I>
                    dataSet.currentNode = group
                    dataSet.currentRecyclerNodePosition = nodeViewHolder.adapterPosition
                    shell.clickNode(group.self, nodeViewHolder)
                }
                nodeViewHolder.currentNodeView.setOnLongClickListener {
                    val group = nodeViewHolder.node
                    dataSet.currentNode = group
                    dataSet.currentRecyclerNodePosition = nodeViewHolder.adapterPosition
                    shell.longClickNode(group.self, nodeViewHolder)
                    true
                }
                return nodeViewHolder
            }
        }
        //return null
    }

    /**
     * 当前的item可能属于多个group（同dataSet的或是当前dataSet都有可能），所以不能通过item的parentGroups来判断
     * 当前item的当前group，只有通过获取当前item的position，让后判断这个position处于当前dataSet中那个group的区
     * 间来确定当前group是那一个（只判断当前dataSet，自然排除了其他dataSet中的group）。
     */

    private fun findCurrentNode(position: Int){
        dataSet.currentNode = null
        for (group in dataSet.recyclerViewNodes) {
            if (position > group.nodePositionID && position <= (group.nodePositionID + group.items.size)) {
                dataSet.currentNode = group
                break
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val recyclerItem = dataSet.recyclerViewItems[position]
        when (recyclerItem.viewType.itemType) {
            ItemType.Item -> {
                @Suppress("UNCHECKED_CAST")
                val itemViewHolder = holder as RecyclerViewAdapter<G, I>.ItemViewHolder
//                if (isChecked) {
//                    itemViewHolder.checkBox!!.visibility = View.VISIBLE
//                    itemViewHolder.checkBox!!.isChecked = item.getChecked()
//                } else {
//                    itemViewHolder.checkBox!!.visibility = View.GONE/
//                }
//                itemViewHolder.checkBox!!.ID = item
                //val groupType = item.getCurrentNode(parentNodes).getGroupType()
                //OnBindItem(itemViewHolder, item, groupType)
                @Suppress("UNCHECKED_CAST")
                shell.displayItem((recyclerItem as RecyclerViewItem<G, I>).self, itemViewHolder)
            }
            ItemType.Node -> {
                @Suppress("UNCHECKED_CAST")
                val groupViewHolder = holder as RecyclerViewAdapter<G, I>.NodeViewHolder
                @Suppress("UNCHECKED_CAST")
                val group = recyclerItem as RecyclerViewNode<G, I>
                //OnBindGroup(groupViewHolder, node)
                shell.displayNode(group.self, groupViewHolder)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item: RecyclerViewBaseItem = dataSet.recyclerViewItems[position]
        var index = item.viewType.indexAt(shell.viewTypes)
        if (index < 0) {
            if (item.viewType.itemType == ItemType.Item) {
                index = shell.viewTypes.indexOfFirst { it.itemType == ItemType.Item }
            } else {
                index = shell.viewTypes.indexOfFirst { it.itemType == ItemType.Node }
            }
        }
        return index
    }

    override fun getItemCount(): Int {
        val size = dataSet.recyclerViewItems.size
        val nullData = size == 0
        if (isNullData == null) {
            isNullData = nullData
            shell.whenNullData(nullData)
        } else {
            if (isNullData != nullData) {
                isNullData = nullData
                shell.whenNullData(nullData)
            } //只有当数据为空状态发生了改变时才回调，所以不能将whenNullData放到if外面
        }
        return size
    }

}
