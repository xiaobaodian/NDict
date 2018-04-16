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
    var foodBased: Int = 0                          // 食物属性（植物性，动物性）
    var protein: Float = 0f                         // 蛋白质含量
    //var picture: Int = 0                            // 食材的图片
    var foodFiber: Float = 0f                       // 食物纤维含量
    var fat: Float = 0f                             // 脂肪含量
    var carbohydrate: Float = 0f                    // 碳水化合物含量
    var calories: Float = 0f                        // 卡路里含量
    var water: Float = 0f                           // 食材水分
    var cholesterol: Float = 0f                     // 胆固醇含量

    var category: BFoodCategory? = null             // 食材的分类
    var article: _Article? = null

    var vitamins: MutableList<ElementContent> = ArrayList()
    var minerals: MutableList<ElementContent> = ArrayList()

    init {
        vitamins.add(ElementContent("RE"))
        vitamins.add(ElementContent("VitaminB1"))
        vitamins.add(ElementContent("VitaminB2"))
        vitamins.add(ElementContent("Niacin"))
        vitamins.add(ElementContent("VitaminB6"))
        vitamins.add(ElementContent("PantothenicAcid"))
        vitamins.add(ElementContent("VitaminH"))
        vitamins.add(ElementContent("FolicAcid"))
        vitamins.add(ElementContent("VitaminB12"))
        vitamins.add(ElementContent("Choline"))
        vitamins.add(ElementContent("VitaminC"))
        vitamins.add(ElementContent("VitaminD"))
        vitamins.add(ElementContent("VitaminE"))
        vitamins.add(ElementContent("VitaminK"))
        vitamins.add(ElementContent("VitaminP"))
        vitamins.add(ElementContent("Inositol"))
        vitamins.add(ElementContent("PABA"))

        minerals.add(ElementContent("K"))
        minerals.add(ElementContent("N"))
        minerals.add(ElementContent("Ca"))
        minerals.add(ElementContent("Mg"))
        minerals.add(ElementContent("Fe"))
        minerals.add(ElementContent("Mn"))
        minerals.add(ElementContent("Zn"))
        minerals.add(ElementContent("Cu"))
        minerals.add(ElementContent("Se"))
        minerals.add(ElementContent("p"))
        minerals.add(ElementContent("I"))
        minerals.add(ElementContent("Mo"))
        minerals.add(ElementContent("Cr"))
        minerals.add(ElementContent("Ce"))
        minerals.add(ElementContent("Co"))
        minerals.add(ElementContent("Sn"))
        minerals.add(ElementContent("Ni"))
        minerals.add(ElementContent("V"))
        minerals.add(ElementContent("Si"))
        minerals.add(ElementContent("Ci"))
        minerals.add(ElementContent("S"))
    }

}