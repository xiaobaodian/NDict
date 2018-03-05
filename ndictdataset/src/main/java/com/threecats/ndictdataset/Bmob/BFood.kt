package com.threecats.ndictdataset.Bmob

import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BmobPointer

/**
 * 由 zhang 于 2018/2/17 创建
 */
class BFood: BmobObject() {

    var name: String = ""                           // 食材名称
    var alias: String = ""                          // 食材别名
    var protein: Float = 0f                         // 蛋白质含量
    //var picture: Int = 0                            // 食材的图片
    var note: String = ""                           // 食材的描述文本
    var foodFiber: Float = 0f                       // 食物纤维含量
    var fat: Float = 0f                             // 脂肪含量
    var carbohydrate: Float = 0f                    // 碳水化合物含量
    var calories: Float = 0f                        // 卡路里含量
    var water: Float = 0f                           // 食材水分
    var cholesterol: Float = 0f                     // 胆固醇含量

    var category: BFoodCategory? = null             // 食材的分类
    var vitamin: BFoodVitamin? = null
    var mineral: BFoodMineral? = null
    var mineralExt: BFoodMineralExt? = null

}