package com.threecats.ndict.Models

import com.threecats.ndict.Models.Food_.category
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

/**
 * 由 zhang 于 2018/1/7 创建
 */

@Entity
data class Category(
        var categoryID: Int? = 0,
        var longTitle: String?  = "",
        var shortTitle: String? = "",
        var bmobId: String = "",
        var bmobUpdateAt: String = ""
) {
    @Id var id: Long = 0

    @Backlink(to = "category")
    lateinit var foods: ToMany<Food>
}