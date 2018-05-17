package com.threecats.ndict.Models

import com.threecats.ndictdataset.Bmob.BFoodCategory
import com.threecats.ndictdataset.Bmob._Article
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import java.util.*

/**
 * 由 zhang 于 2018/1/8 创建
 */

@Entity
data class Food(
        @Id var id: Long = 0,
        var name: String = "",                           // 食材名称
        var alias: String = "",                          // 食材别名
        var foodBased: Int = 0,                          // 食物属性（植物性，动物性）
        var protein: Float = 0f,                         // 蛋白质含量
        var foodFiber: Float = 0f,                       // 食物纤维含量
        var fat: Float = 0f,                             // 脂肪含量
        var carbohydrate: Float = 0f,                    // 碳水化合物含量
        var calories: Float = 0f,                        // 卡路里含量
        var water: Float = 0f,                           // 食材水分
        var cholesterol: Float = 0f                      // 胆固醇含量

) {
    lateinit var category: ToOne<Category>

    @Transient
    var vitamins: MutableList<ElementContent> = ArrayList()

    @Transient
    var minerals: MutableList<ElementContent> = ArrayList()

    @Transient
    var article: _Article? = null

    var bmobId: String = ""
    var bmobUpdateAt: String = ""
}