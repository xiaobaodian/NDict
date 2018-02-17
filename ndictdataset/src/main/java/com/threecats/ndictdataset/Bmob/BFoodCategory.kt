package com.threecats.ndictdataset.Bmob

import cn.bmob.v3.BmobObject

/**
 * 由 zhang 于 2018/2/9 创建
 */
class BFoodCategory() : BmobObject() {
    var categoryID: Int? = null
    var LongTitle: String? = null
    var ShortTitle: String? = null
    var FoodTotal: Int = 0

    constructor(categoryId: Int, longTitle: String, shortTitle: String): this(){
        categoryID = categoryId
        LongTitle = longTitle
        ShortTitle = shortTitle
    }
}