package com.threecats.ndict.OriginalData

import com.threecats.ndict.Models.Category

/**
 * 由 zhang 于 2018/1/17 创建
 */
object InitFoodCategory {

    fun createFoodCategory(): List<Category>{
        var categories: List<Category> = listOf(
                Category(1,"谷类及其制品","谷类"),
                Category(2,"干豆类及其制品","干豆"),
                Category(3,"鲜豆类及其制品","鲜豆"),
                Category(4,"根茎类","根茎"),
                Category(5,"蔬菜类","蔬菜"),
                Category(6,"瓜茄类","瓜茄"),
                Category(7,"水果类","水果"),
                Category(8,"坚果类","坚果"),
                Category(9,"畜牧产品","畜牧"),
                Category(10,"禽蛋类","禽蛋"),
                Category(11,"乳类及其制品","乳类"),
                Category(12,"水产及其制品","水产"),
                Category(13,"菌类及其制品","菌类"),
                Category(14,"调味制品","调味"),
                Category(15,"酿制产品","酒类"),
                Category(16,"糖果辅食","辅食")
        )
        return categories
    }

}