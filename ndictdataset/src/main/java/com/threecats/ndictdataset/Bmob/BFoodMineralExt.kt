package com.threecats.ndictdataset.Bmob

import cn.bmob.v3.BmobObject

/**
 * 由 zhang 于 2018/3/1 创建
 */
class BFoodMineralExt: BmobObject() {

    var FoodID: String? = null      // 食材，对应关系
    var mAi: Float = 0f             // 铝含量
    var mCi: Float = 0f             // 氯含量
    var mF: Float = 0f              // 氟含量
    var mPb: Float = 0f             // 铅含量
    var mS: Float = 0f              // 硫含量

}