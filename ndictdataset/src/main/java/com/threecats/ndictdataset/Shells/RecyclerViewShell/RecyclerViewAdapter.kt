package com.threecats.ndictdataset.Shells.RecyclerViewShell

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

/**
 * 由 zhang 于 2018/3/28 创建
 */

class RecyclerViewAdapter(private val dataSet: RecyclerViewData, private val shell: RecyclerViewShell) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isChecked = false

    inner class ItemViewHolder(internal var currentItemView: View) : RecyclerView.ViewHolder(currentItemView) {
        //internal var checkBox: CheckBox? = null
        val item: RecyclerViewItem
            get() = dataSet.recyclerViewItems[adapterPosition]

//        init {
//            checkBox = currentItemView.findViewById(R.id.checkBox)
//            checkBox?.let {
//                it.setOnClickListener { v ->
//                    if (checkBox == null) {
//                        Toast.makeText(v.context, "Check Box is NULL !!!", Toast.LENGTH_SHORT).show()
//                        return@checkBox.setOnClickListener
//                    }
//                    val task = checkBox!!.tag as Task
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
            if (text.length == 0) {
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

    inner class GroupViewHolder(internal var currentGroupView: View) : RecyclerView.ViewHolder(currentGroupView) {
        val group: RecyclerViewGroup
            get() = dataSet.recyclerViewItems[adapterPosition] as RecyclerViewGroup

        fun displayText(R: Int, text: String): GroupViewHolder {
            val textView = currentGroupView.findViewById<TextView>(R)
            textView.text = text
            return this@GroupViewHolder
        }

        fun displayImage(R: Int, imageResId: Int): GroupViewHolder {
            val imageView = currentGroupView.findViewById<ImageView>(R)
            imageView.setImageResource(imageResId)
            return this@GroupViewHolder
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        val type = shell.getViewType(viewType)
        type?.let {
            val view: View
            when (it.itemType) {
                ItemType.Item -> {
                    view = LayoutInflater.from(parent.context).inflate(it.LayoutID!!, parent, false)
                    val itemViewHolder = ItemViewHolder(view)
                    itemViewHolder.currentItemView.setOnClickListener { v ->
                        val item = itemViewHolder.item
                        shell.clickItem(item, itemViewHolder)
                        //App.self().getDataManger().setCurrentTask(task)
//                    if (isChecked) {
//                        itemViewHolder.checkBox!!.isChecked = !task.getChecked()
//                        task.setChecked(itemViewHolder.checkBox!!.isChecked)
//                    } else {
//                        //Intent taskIntent = new Intent(App.getMainActivity(),TaskDetailsActivity.class);
//                        val taskIntent = Intent(App.self().getMainActivity(), TaskDisplayActivity::class.java)
//                        App.self().getMainActivity().startActivity(taskIntent)
//                    }
                    }
                    itemViewHolder.currentItemView.setOnLongClickListener { v ->
                        //if (isChecked) return@itemViewHolder.currentItemView.setOnLongClickListener false
                        val item = itemViewHolder.item
                        shell.longClickItem(item, itemViewHolder)
                        //App.self().getDataManger().setCurrentTask(task)
                        //暂时关闭长安多选功能
                        //App.getDataManger().getCurrentGroupList().setItemChecked(true);
                        true
                    }
                    return itemViewHolder
                }
                ItemType.Group -> {
                    view = LayoutInflater.from(parent.context).inflate(it.LayoutID!!, parent, false)
                    val groupViewHolder = GroupViewHolder(view)
                    groupViewHolder.currentGroupView.setOnClickListener { v ->
                        val group = groupViewHolder.group
                        shell.clickGroup(group, groupViewHolder)
                    }
                    groupViewHolder.currentGroupView.setOnLongClickListener { v ->
                        val group = groupViewHolder.group
                        shell.longClickGroup(group, groupViewHolder)
                        true
                    }
                    return groupViewHolder
                }
            }
        }
        return null
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val recyclerItem = dataSet.recyclerViewItems[position]
        when (recyclerItem.viewType?.itemType) {
            ItemType.Item -> {
                val itemViewHolder = holder as ItemViewHolder
                val item = recyclerItem as RecyclerViewItem
//                if (isChecked) {
//                    itemViewHolder.checkBox!!.visibility = View.VISIBLE
//                    itemViewHolder.checkBox!!.isChecked = item.getChecked()
//                } else {
//                    itemViewHolder.checkBox!!.visibility = View.GONE
//                }
//                itemViewHolder.checkBox!!.tag = item
                //val groupType = item.getCurrentGroup(parentGroups).getGroupType()
                //OnBindItem(itemViewHolder, item, groupType)
                shell.displayItem(item, itemViewHolder)
            }
            ItemType.Group -> {
                val groupViewHolder = holder as GroupViewHolder
                val group = recyclerItem as RecyclerViewGroup
                //OnBindGroup(groupViewHolder, group)
                shell.displayGroup(group, groupViewHolder)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item: RecyclerViewItem = dataSet.recyclerViewItems[position]
        return item.viewType!!.hashCode()
    }

    override fun getItemCount(): Int {
        val size = dataSet.recyclerViewItems.size
        shell.itemSizeChanged(size)
        return size
    }

}
