package com.threecats.ndictdataset.View


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BFoodCategory
import com.threecats.ndictdataset.Bmob.BNutrient
import com.threecats.ndictdataset.Enum.EEditerState
import com.threecats.ndictdataset.EventClass.UpdateCategoryRecyclerItem
import com.threecats.ndictdataset.Helper.ErrorMessage

import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.Shells.RecyclerViewShell.*
import kotlinx.android.synthetic.main.fragment_category_foods.*
import kotlinx.android.synthetic.main.fragment_trace_element.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast


/**
 * A simple [Fragment] subclass.
 */

class CategoryFoodsFragment : Fragment() {

    private val shareSet = BDM.ShareSet!!

    private var categoryList: MutableList<BFoodCategory>? = null
    private var categoryRView: RecyclerView? = null

    private var rvShell: RecyclerViewShell<Any, BFoodCategory>? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        EventBus.getDefault().register(this)
        return inflater!!.inflate(R.layout.fragment_category_foods, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (rvShell == null) {
            rvShell = RecyclerViewShell(context)
        }

        rvShell?.let {
            it.recyclerView(CategoryRView).progressBar(progressBarCategory).addViewType("item", ItemType.Item, R.layout.category_recycleritem)
            it.setDisplayItemListener(object : onDisplayItemListener<Any, BFoodCategory> {
                override fun onDisplayItem(item: RecyclerViewItem<Any, BFoodCategory>, holder: RecyclerViewAdapter<Any, BFoodCategory>.ItemViewHolder) {
                    val e = item.getObject() as BFoodCategory
                    holder.displayText(R.id.categoryTitle, e.longTitle!!)
                    holder.displayText(R.id.subTotal, e.foodTotal.toString())
                }
            })
            it.setOnClickItemListener(object : onClickItemListener<Any, BFoodCategory> {
                override fun onClickItem(item: RecyclerViewItem<Any, BFoodCategory>, holder: RecyclerViewAdapter<Any, BFoodCategory>.ItemViewHolder) {
                    val e = item.getObject()
                    BDM.ShareSet?.CurrentCategory = item
                    val intent = Intent(context, FoodListActivity::class.java)
                    context.startActivity(intent)
                }
            })
            it.setOnLongClickItemListener(object : onLongClickItemListener<Any, BFoodCategory> {
                override fun onLongClickItem(item: RecyclerViewItem<Any, BFoodCategory>, holder: RecyclerViewAdapter<Any, BFoodCategory>.ItemViewHolder) {
                    BDM.ShareSet?.CurrentCategory = it.currentItem
                    val intent = Intent(context, FoodListActivity::class.java)
                    context.startActivity(intent)
                }
            })
            it.setQueryDataListener(object : onQueryDatasListener<Any, BFoodCategory> {
                override fun onQueryDatas(shell: RecyclerViewShell<Any, BFoodCategory>) {
                    val query = BmobQuery<BFoodCategory>()
                    query.findObjects(object : FindListener<BFoodCategory>() {
                        override fun done(categorys: MutableList<BFoodCategory>?, e: BmobException?) {
                            if (e == null) {
                                categorys?.let {
                                    shell.addItems(it)
                                    shell.completeQuery()
                                }
                            } else {
                                if (view != null) {
                                    ErrorMessage(context, e)
                                }
                            }
                        }
                    })
                }
            })
            it.setOnNullDataListener((object : onNullDataListener {
                override fun onNullData(isNull: Boolean) {
                    if (isNull) {
                        //context.toast("当前没有数据")
                    } else{
                        //context.toast("已经添加数据")
                    }
                }
            }))
            it.link()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doUpdateCategoryRecyclerItem(updateItem: UpdateCategoryRecyclerItem<Any, BFoodCategory>){
        when (updateItem.State){
            EEditerState.CategoryEdit -> rvShell!!.updateItem(updateItem.Category)
            EEditerState.CategoryAppend -> {rvShell!!.addItem(updateItem.Category.getObject()!!)
            }
            else -> context.toast("EditState Error !")
        }

    }

    private fun queryOne(objectID: String){
        val categoryQuery: BmobQuery<BFoodCategory> = BmobQuery<BFoodCategory>()
        categoryQuery.getObject(objectID, object : QueryListener<BFoodCategory>() {
            override fun done(category: BFoodCategory?, e: BmobException?) {
                if (e == null) {
                    //message.text = food!!.longTitle
                } else {
                    //message.text = e.message
                }
            }
        })
    }

    private fun saveCategoryOne(categoryID: Int, longTitle: String, shortTitle: String){
        val category = BFoodCategory()  //categoryID, longTitle, shortTitle
        category.categoryID = categoryID
        category.longTitle = longTitle
        category.shortTitle = shortTitle
        category.save(object : SaveListener<String>() {
            override fun done(objectId: String?, e: BmobException?) {
                if (e == null) {
                    //message.text = "添加数据成功，返回objectId为：" + objectId
                } else {
                    //message.text = "创建数据失败：" + e.message
                }
            }
        })
    }

}// Required empty public constructor
