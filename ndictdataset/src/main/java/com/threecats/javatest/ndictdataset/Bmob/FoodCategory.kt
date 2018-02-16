package com.threecats.javatest.ndictdataset.Bmob

import android.os.Parcel
import android.os.Parcelable
import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BmobRelation

/**
 * 由 zhang 于 2018/2/9 创建
 */
class FoodCategory() : BmobObject() {
    var categoryID: Int? = null
    var LongTitle: String? = null
    var ShortTitle: String? = null

    constructor(categoryId: Int, longTitle: String, shortTitle: String): this(){
        categoryID = categoryId
        LongTitle = longTitle
        ShortTitle = shortTitle
    }
}