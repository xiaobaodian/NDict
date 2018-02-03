package com.threecats.ndict.Models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

/**
 * 由 zhang 于 2018/1/7 创建
 */

@Entity
class FoodCategory() {

    @Id
    var id: Long = 0

    var categoryID: Int = 0
    var longTitle: String  = ""
    var shortTitle: String = ""

    lateinit var foods: ToMany<Food>

    constructor(categoryID: Int, longTitle: String, shortTitle: String): this() {
        this.categoryID = categoryID
        this.longTitle = longTitle
        this.shortTitle = shortTitle
    }

}