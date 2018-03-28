package com.threecats.ndictdataset.Shells.RecyclerViewShell

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.threecats.ndictdataset.R

/**
 * 由 zhang 于 2018/3/28 创建
 */

abstract class RecyclerViewAdapter(private val datas: RecyclerViewData) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //private var groups: GroupListBase? = null
    //private var group: GroupBase? = null
    private var itemLayoutID: Int = 0
    private var groupLayoutID: Int = 0
    private var isChecked = false

    private val onItemClickListener: AdapterView.OnItemClickListener? = null

//    fun setGroups(groups: GroupListBase) {
//        this.groups = groups
//    }
//
//    fun setGroup(group: GroupBase) {
//        this.group = group
//    }

    fun itemLayout(itemLayoutID: Int): RecyclerViewAdapter {
        this.itemLayoutID = itemLayoutID
        return this
    }

    fun groupLayout(groupLayoutID: Int): RecyclerViewAdapter {
        this.groupLayoutID = groupLayoutID
        return this
    }

    inner class ItemViewHolder(internal var currentItemView: View) : RecyclerView.ViewHolder(currentItemView) {
        //internal var checkBox: CheckBox? = null
        val item: RecyclerViewItem
            get() = datas.recyclerItems[adapterPosition] as RecyclerViewItem

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

        fun setText(R: Int, text: String): ItemViewHolder {
            val textView = currentItemView.findViewById<TextView>(R)
            if (text.length == 0) {
                textView.visibility = View.GONE
            } else {
                textView.visibility = View.VISIBLE
                textView.text = text
            }
            return this@ItemViewHolder
        }

        fun setImageResource(R: Int, imageResId: Int): ItemViewHolder {
            val imageView = currentItemView.findViewById<ImageView>(R)
            imageView.setImageResource(imageResId)
            return this@ItemViewHolder
        }
    }

    inner class GroupViewHolder(internal var currentGroupView: View) : RecyclerView.ViewHolder(currentGroupView) {
        val group: RecyclerViewGroup
            get() = datas.recyclerItems[adapterPosition] as RecyclerViewGroup

        fun setText(R: Int, text: String): GroupViewHolder {
            val textView = currentGroupView.findViewById<TextView>(R)
            textView.text = text
            return this@GroupViewHolder
        }

        fun setImageResource(R: Int, imageResId: Int): GroupViewHolder {
            val imageView = currentGroupView.findViewById<ImageView>(R)
            imageView.setImageResource(imageResId)
            return this@GroupViewHolder
        }
    }

    protected abstract fun OnBindItem(holder: ItemViewHolder, task: TaskItem, groupType: GroupType?)
    protected abstract fun OnBindGroup(holder: GroupViewHolder, group: GroupBase)
    protected abstract fun OnGroupClick(group: GroupBase)
    protected abstract fun OpenTips()
    protected abstract fun CloseTips()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        val view: View
        when (viewType) {
            0 -> {
                view = LayoutInflater.from(parent.context).inflate(itemLayoutID, parent, false)
                val itemViewHolder = ItemViewHolder(view)
                itemViewHolder.currentItemView.setOnClickListener { v ->
                    val item = itemViewHolder.item
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
                    //App.self().getDataManger().setCurrentTask(task)
                    //暂时关闭长安多选功能
                    //App.getDataManger().getCurrentGroupList().setItemChecked(true);
                    true
                }
                return itemViewHolder
            }
            1 -> {
                view = LayoutInflater.from(parent.context).inflate(groupLayoutID, parent, false)
                val groupViewHolder = GroupViewHolder(view)
                groupViewHolder.currentGroupView.setOnClickListener { v -> OnGroupClick(groupViewHolder.group) }
                return groupViewHolder
            }
        }
        return null
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val recyclerItem = datas.recyclerItems[position]
        when (recyclerItem.itemType) {
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
                //val groupType = item.getCurrentGroup(groups).getGroupType()
                OnBindItem(itemViewHolder, item, groupType)
            }
            ItemType.Group -> {
                val groupViewHolder = holder as GroupViewHolder
                val group = recyclerItem as RecyclerViewGroup
                OnBindGroup(groupViewHolder, group)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item: RecyclerViewItem = datas.recyclerItems[position]
        return item.itemType.ordinal //ordinal()
    }

    override fun getItemCount(): Int {
        val size = datas.recyclerItems.size
        if (size == 0) {
            OpenTips()
        } else {
            CloseTips()
        }
        return size
    }

}
