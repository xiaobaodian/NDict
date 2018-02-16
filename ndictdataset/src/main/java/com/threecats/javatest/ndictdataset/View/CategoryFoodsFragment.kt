package com.threecats.javatest.ndictdataset.View


import android.os.Bundle
import android.support.v4.app.Fragment
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
import com.threecats.javatest.ndictdataset.Bmob.FoodCategory

import com.threecats.javatest.ndictdataset.R
import kotlinx.android.synthetic.main.fragment_category_foods.*


/**
 * A simple [Fragment] subclass.
 */
class CategoryFoodsFragment : Fragment() {

    private var categoryList: MutableList<FoodCategory>? = null
    private var categoryRView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //Toast.makeText(context,"hdhdhdhhdhd",Toast.LENGTH_LONG).show()
        return inflater!!.inflate(R.layout.fragment_category_foods, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryRView = CategoryRView
        if (categoryList == null) {
            queryAllFoodCategory()
        } else {
            bindCategoryList()
        }
    }

    private fun bindCategoryList(){
        categoryRView?.adapter = CategoryFoodsAdapter(categoryList!!)
    }

    private fun queryOne(objectID: String){
        val categoryQuery: BmobQuery<FoodCategory> = BmobQuery<FoodCategory>()
        categoryQuery.getObject(objectID, object : QueryListener<FoodCategory>() {
            override fun done(category: FoodCategory?, e: BmobException?) {
                if (e == null) {
                    //message.text = category!!.LongTitle
                } else {
                    //message.text = e.message
                }
            }
        })
    }

    private fun queryAllFoodCategory() {
        val query = BmobQuery<FoodCategory>()
        query.findObjects(object : FindListener<FoodCategory>() {
            override fun done(categorys: MutableList<FoodCategory>?, e: BmobException?) {
                if (e == null) {
                    categoryList = categorys
                    if (categoryRView == null) {
                        Toast.makeText(view!!.context,"Recycler is null ",Toast.LENGTH_LONG).show()
                    }
                    categoryRView?.adapter = CategoryFoodsAdapter(categoryList!!)
                    Toast.makeText(view!!.context,"bind Category List / Items : "+categorys!!.size,Toast.LENGTH_LONG).show()
                    //bindCategoryList()
                } else {
                    //message.text = e.message
                }
            }
        })
    }

    private fun saveCategoryOne(categoryID: Int, longTitle: String, shortTitle: String){
        var category = FoodCategory()  //categoryID, longTitle, shortTitle
        category.categoryID = categoryID
        category.LongTitle = longTitle
        category.ShortTitle = shortTitle
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
