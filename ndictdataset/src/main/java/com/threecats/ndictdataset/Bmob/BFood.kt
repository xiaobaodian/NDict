package com.threecats.ndictdataset.Bmob

import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BmobPointer
import com.threecats.ndictdataset.Enum.EFoodBase

/**
 * 由 zhang 于 2018/2/17 创建
 */
class BFood: BmobObject() {

    var name: String = ""                           // 食材名称
    var alias: String = ""                          // 食材别名
    var foodBase: EFoodBase = EFoodBase.PlantBased  // 食物属性（植物性，动物性）
    var protein: Float = 0f                         // 蛋白质含量
    //var picture: Int = 0                            // 食材的图片
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
    var article: _Article? = null

}