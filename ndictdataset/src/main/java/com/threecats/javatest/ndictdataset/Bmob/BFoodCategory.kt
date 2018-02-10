package com.threecats.javatest.ndictdataset.Bmob

import cn.bmob.v3.BmobObject

/**
 * 由 zhang 于 2018/2/9 创建
 */
data class FoodCategory(
        var categoryID: Int,
        var LongTitle: String,
        var ShortTitle: String
    ): BmobObject() {
}