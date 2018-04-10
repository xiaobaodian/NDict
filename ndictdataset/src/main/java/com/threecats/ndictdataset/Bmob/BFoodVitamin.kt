package com.threecats.ndictdataset.Bmob

import cn.bmob.v3.BmobObject

/**
 * 由 zhang 于 2018/2/27 创建
 */
class BFoodVitamin: BmobObject()  {

    var foodID: String? = null          // 食材，对应关系
    var RE: Float = 0f                  // 视黄醇当量
    var vitaminB1: Float = 0f           // 维生素B1
    var vitaminB2: Float = 0f           // 维生素B2
    var niacin: Float = 0f              // 烟酸
    var vitaminB6: Float = 0f           // 维生素B6
    var pantothenicAcid: Float = 0f     // 泛酸
    var vitaminH: Float = 0f            // 生物素(维生素H)
    var folicAcid: Float = 0f           // 叶酸
    var vitaminB12: Float = 0f          // 维生素B12
    var choline: Float = 0f             // 胆碱
    var vitaminC: Float = 0f            // 维生素C
    var vitaminD: Float = 0f            // 维生素D
    var vitaminE: Float = 0f            // 维生素E
    var vitaminK: Float = 0f            // 维生素K
    var vitaminP: Float = 0f            // 维生素P（生物类黄酮）
    var inositol: Float = 0f            // 肌醇
    var PABA: Float = 0f                // 对氨基苯甲酸

}