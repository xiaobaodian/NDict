package com.threecats.ndict.Models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import java.util.*

/**
 * 由 zhang 于 2018/1/8 创建
 */

@Entity
class Food() {

    @Id
    var id: Long = 0

    var timestamp: Date = Date()
    var name: String = "food name"
    var alias: String = ""
    var anotherName: String = ""
    var note: String = ""
    var picture: Int = 0

    var calories: Float = 0f
    var water: Float = 0f
    var protein:Float = 0f
    var fat: Float = 0f
    var carbohydrate:Float = 0f
    var foodFiber: Float = 0f

    lateinit var category: ToOne<FoodCategory>

    constructor(name: String): this(){
        this.name = name
    }
}