package com.threecats.javatest.ndictdataset.Bmob

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener

/**
 * 由 zhang 于 2018/2/10 创建
 */
object BmobSet {
    fun getFoodCategory(objectID: String): ResultObject<FoodCategory> {
        val categoryQuery: BmobQuery<FoodCategory> = BmobQuery<FoodCategory>()
        var resultObject = ResultObject<FoodCategory>()
        categoryQuery.getObject(objectID, object : QueryListener<FoodCategory>() {
            override fun done(category: FoodCategory, e: BmobException?) {
                if (e == null) {
                    resultObject.target = category
                } else {
                    resultObject.message = e.message
                }
            }
        })
        return resultObject
    }
}