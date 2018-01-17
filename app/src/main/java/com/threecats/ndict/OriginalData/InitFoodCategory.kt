package com.threecats.ndict.OriginalData

import com.threecats.ndict.Models.FoodCategory

/**
 * 由 zhang 于 2018/1/17 创建
 */
object InitFoodCategory {

    fun createFoodCategory(): List<FoodCategory>{
        var foodCategorys: List<FoodCategory> = listOf(
                FoodCategory(1,"谷类及其制品","谷类"),
                FoodCategory(2,"干豆类及其制品","干豆"),
                FoodCategory(3,"鲜豆类及其制品","鲜豆"),
                FoodCategory(4,"根茎类","根茎"),
                FoodCategory(5,"蔬菜类","蔬菜"),
                FoodCategory(6,"瓜茄类","瓜茄"),
                FoodCategory(7,"水果类","水果"),
                FoodCategory(8,"坚果类","坚果"),
                FoodCategory(9,"畜牧产品","畜牧"),
                FoodCategory(10,"禽蛋类","禽蛋"),
                FoodCategory(11,"乳类及其制品","乳类"),
                FoodCategory(12,"水产及其制品","水产"),
                FoodCategory(13,"菌类及其制品","菌类"),
                FoodCategory(14,"调味制品","调味"),
                FoodCategory(15,"酿制产品","酒类"),
                FoodCategory(16,"糖果辅食","辅食")
        )
        return foodCategorys
    }

}