package com.threecats.ndict.OriginalData

import com.threecats.ndict.Models.Food
import com.threecats.ndict.Models.FoodCategory

/**
 * 由 zhang 于 2018/1/31 创建
 */
object InitFoods {

    fun createFoods(foodCategorys: List<FoodCategory>){

        //谷类及其制品
        var category: FoodCategory? = foodCategorys.find { it.categoryID == 1 }
        category?.foods?.add(Food("稻米"))
        category?.foods?.add(Food("粳米"))
        category?.foods?.add(Food("籼米"))
        category?.foods?.add(Food("小麦"))
        category?.foods?.add(Food("糯米"))
        category?.foods?.add(Food("黑米"))
        category?.foods?.add(Food("米饭（蒸）"))
        category?.foods?.add(Food("小麦粉（糖白粉）"))
        category?.foods?.add(Food("小麦粉（标准粉）"))
        category?.foods?.add(Food("面条（切面）"))
        category?.foods?.add(Food("面条（挂面）"))
        category?.foods?.add(Food("馒头（富强粉）"))
        category?.foods?.add(Food("馒头（标准粉）"))
        category?.foods?.add(Food("油饼"))
        category?.foods?.add(Food("油条"))
        category?.foods?.add(Food("荞麦"))
        category?.foods?.add(Food("荞麦面"))
        category?.foods?.add(Food("小米"))
        category?.foods?.add(Food("玉米（鲜）"))
        category?.foods?.add(Food("玉米（白、鲜）"))
        category?.foods?.add(Food("玉米（黄、干）"))
        category?.foods?.add(Food("玉米面（黄）"))
        category?.foods?.add(Food("燕麦"))
        category?.foods?.add(Food("薏米"))

        //干豆类及其制品
        category = foodCategorys.find { it.categoryID == 2 }
        category?.foods?.add(Food("黄豆"))
        category?.foods?.add(Food("绿豆"))

        //鲜豆类及其制品
        category = foodCategorys.find { it.categoryID == 3 }
        category?.foods?.add(Food("鲜豆类及其制品"))
        category?.foods?.add(Food("鲜豆类及其制品"))

        //根茎类
        category = foodCategorys.find { it.categoryID == 4 }
        category?.foods?.add(Food("根茎类"))
        category?.foods?.add(Food("根茎类"))

        //蔬菜类
        category = foodCategorys.find { it.categoryID == 5 }
        category?.foods?.add(Food("蔬菜类"))
        category?.foods?.add(Food("蔬菜类"))

        //瓜茄类
        category = foodCategorys.find { it.categoryID == 6 }
        category?.foods?.add(Food("瓜茄类"))
        category?.foods?.add(Food("瓜茄类"))

        //水果类
        category = foodCategorys.find { it.categoryID == 7 }
        category?.foods?.add(Food("水果类"))
        category?.foods?.add(Food("水果类"))

        //坚果类
        category = foodCategorys.find { it.categoryID == 8 }
        category?.foods?.add(Food("坚果类"))
        category?.foods?.add(Food("坚果类"))

        //畜牧产品
        category = foodCategorys.find { it.categoryID == 9 }
        category?.foods?.add(Food("畜牧产品"))
        category?.foods?.add(Food("畜牧产品"))

        //禽蛋类
        category = foodCategorys.find { it.categoryID == 10 }
        category?.foods?.add(Food("禽蛋类"))
        category?.foods?.add(Food("禽蛋类"))

        //乳类及其制品
        category = foodCategorys.find { it.categoryID == 11 }
        category?.foods?.add(Food("乳类及其制品"))
        category?.foods?.add(Food("乳类及其制品"))

        //水产及其制品
        category = foodCategorys.find { it.categoryID == 12 }
        category?.foods?.add(Food("水产及其制品"))
        category?.foods?.add(Food("水产及其制品"))

        //菌类及其制品
        category = foodCategorys.find { it.categoryID == 13 }
        category?.foods?.add(Food("菌类及其制品"))
        category?.foods?.add(Food("菌类及其制品"))

        //调味制品
        category = foodCategorys.find { it.categoryID == 14 }
        category?.foods?.add(Food("调味制品"))
        category?.foods?.add(Food("调味制品"))

        //酿制产品
        category = foodCategorys.find { it.categoryID == 15 }
        category?.foods?.add(Food("酿制产品"))
        category?.foods?.add(Food("酿制产品"))

        //糖果辅食
        category = foodCategorys.find { it.categoryID == 16 }
        category?.foods?.add(Food("糖果辅食"))
        category?.foods?.add(Food("糖果辅食"))
    }
}