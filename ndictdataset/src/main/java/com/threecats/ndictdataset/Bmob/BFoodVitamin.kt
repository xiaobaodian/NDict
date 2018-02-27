package com.threecats.ndictdataset.Bmob

import cn.bmob.v3.BmobObject

/**
 * 由 zhang 于 2018/2/27 创建
 */
class BFoodVitamin: BmobObject()  {

    var Food: BFood? = null             // 食材，对应关系
    var VitaminA: Float = 0f            // 维生素A
    var Carotene: Float = 0f            // 胡萝卜素
    var VitaminB1: Float = 0f           // 维生素B1
    var VitaminB2: Float = 0f           // 维生素B2
    var Niacin: Float = 0f              // 烟酸
    var VitaminB6: Float = 0f           // 维生素B6
    var PantothenicAcid: Float = 0f     // 泛酸
    var VitaminH: Float = 0f            // 生物素(维生素H)
    var FolicAcid: Float = 0f           // 叶酸
    var VitaminB12: Float = 0f          // 维生素B12
    var Choline: Float = 0f             // 胆碱
    var VitaminC: Float = 0f            // 维生素C
    var VitaminD: Float = 0f            // 维生素D
    var VitaminE: Float = 0f            // 维生素E
    var VitaminK: Float = 0f            // 维生素K
    var VitaminP: Float = 0f            // 维生素P（生物类黄酮）
    var Inositol: Float = 0f            // 肌醇
    var PABA: Float = 0f                // 对氨基苯甲酸

}