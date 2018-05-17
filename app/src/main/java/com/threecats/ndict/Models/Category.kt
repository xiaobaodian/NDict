package com.threecats.ndict.Models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

/**
 * 由 zhang 于 2018/1/7 创建
 */

@Entity
data class Category(
        var categoryID: Int = 0,
        var longTitle: String  = "",
        var shortTitle: String = ""
) {
    @Id var id: Long = 0
    lateinit var foods: ToMany<Food>
}