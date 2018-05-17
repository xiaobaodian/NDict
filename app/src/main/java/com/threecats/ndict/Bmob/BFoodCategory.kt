package com.threecats.ndictdataset.Bmob

import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BmobDate
import com.threecats.ndict.Enum.EFoodBase
import java.util.*

/**
 * 由 zhang 于 2018/2/9 创建
 */

data class BFoodCategory(
        var categoryID: Int? = null,
        var longTitle: String? = null,
        var shortTitle: String? = null
) : BmobObject() {
    var foodTotal: Int = 0
    var foodBased: Int = 0  // 食物属性（植物性，动物性）
}