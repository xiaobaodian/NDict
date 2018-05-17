package com.threecats.ndict.Bmob

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Bmob.BFoodCategory

/**
 * 由 zhang 于 2018/5/17 创建
 */
class BmobSet {

    fun getCategories(){     //var categories: MutableList<BFoodCategory>
        val query = BmobQuery<BFoodCategory>()
        query.findObjects(object : FindListener<BFoodCategory>() {
            override fun done(categorys: MutableList<BFoodCategory>?, e: BmobException?) {
                if (e == null) {
                    categorys?.let {

                    }
                } else {
                    //ErrorMessage(context!!, e)
                }
            }
        })
    }

    fun getFoods(category: BFoodCategory){
        val query: BmobQuery<BFood> = BmobQuery()
        query.addWhereEqualTo("category", BmobPointer(category))
        //query.include("vitamin,mineral,mineralExt,article")
        query.setLimit(300)
        query.findObjects(object: FindListener<BFood>(){
            override fun done(foods: MutableList<BFood>?, e: BmobException?) {
                if (e == null) {
                    foods?.let {

                    }

                } else {

                }
            }
        })
    }

}