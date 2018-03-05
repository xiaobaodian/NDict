package com.threecats.ndictdataset.View


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BFoodCategory
import com.threecats.ndictdataset.Enum.EditerState
import com.threecats.ndictdataset.EventClass.UpdateCategoryRecyclerItem

import com.threecats.ndictdataset.R
import kotlinx.android.synthetic.main.fragment_category_foods.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * A simple [Fragment] subclass.
 */
class CategoryFoodsFragment : Fragment() {

    private var categoryList: MutableList<BFoodCategory>? = null
    private var categoryRView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        EventBus.getDefault().register(this)
        return inflater!!.inflate(R.layout.fragment_category_foods, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryRView = CategoryRView
        categoryRView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        if (categoryList == null) {
            queryAllFoodCategory()
        } else {
            bindCategoryList()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun bindCategoryList(){
        categoryRView?.adapter = CategoryFoodsAdapter(categoryList!!, context)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doUpdateCategoryRecyclerItem(updateItem: UpdateCategoryRecyclerItem){
        val position = categoryList?.indexOf(updateItem.Category)
        when (updateItem.State){
            EditerState.CategoryEdit -> CategoryRView?.adapter?.notifyItemChanged(position!!)
            EditerState.CategoryAppend -> {
                categoryList?.add(BDM.ShareSet?.CurrentCategory!!)
                val categorySize = categoryList?.size!!
                CategoryRView?.adapter?.notifyItemInserted(categorySize)
            }
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

    private fun queryAllFoodCategory() {
        val query = BmobQuery<BFoodCategory>()
        query.findObjects(object : FindListener<BFoodCategory>() {
            override fun done(categorys: MutableList<BFoodCategory>?, e: BmobException?) {
                if (e == null) {
                    categoryList = categorys
                    if (categoryRView == null) {
                        Toast.makeText(view!!.context,"Recycler is null ",Toast.LENGTH_LONG).show()
                    }
                    if (categoryList != null) {
                        categoryRView?.adapter = CategoryFoodsAdapter(categoryList!!, context)
                    }
                    //bindCategoryList()
                } else {
                    //message.text = e.message
                    if (view != null) {
                        Toast.makeText(view!!.context,e.message,Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun saveCategoryOne(categoryID: Int, longTitle: String, shortTitle: String){
        var category = BFoodCategory()  //categoryID, longTitle, shortTitle
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
