package com.threecats.ndict.Models

import com.threecats.ndict.App
import com.threecats.ndict.OriginalData.InitFoodCategory
import com.threecats.ndict.OriginalData.InitFoods
import com.threecats.ndict.OriginalData.InitPerson
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import io.objectbox.query.Query

/**
 * 由 zhang 于 2018/1/6 创建
 */

object DataSet {

    var app: App? = null

    lateinit var foodCategorys: List<FoodCategory>
    lateinit var traceElements: List<TraceElement>

    lateinit var personBox: Box<Person>
    lateinit var personQuery: Query<Person>

    lateinit var foodCategoryBox: Box<FoodCategory>
    lateinit var foodCategoryQuery: Query<FoodCategory>

    lateinit var foodBox: Box<Food>
    lateinit var foodQuery: Query<Food>

    lateinit var currentPerson: Person

    fun init(app: App){
        this.app = app

        this.personBox = this.app!!.boxStore.boxFor<Person>()
        this.personQuery = personBox.query().build()

        this.foodCategoryBox = this.app!!.boxStore.boxFor<FoodCategory>()
        this.foodCategoryQuery = foodCategoryBox.query().build()

        this.foodBox = this.app!!.boxStore.boxFor<Food>()
        this.foodQuery = foodBox.query().build()
    }

    fun initPerson(): Boolean {
        if (app == null) {
            return false
        } else {
            if (personBox.count().toInt() == 0) {
                val persons = InitPerson.createPerson()
                personBox.put(persons)
            }
            val persons = personQuery.find()
            currentPerson = persons[0]
            return true
        }
    }

    val persons: List<Person>?
        get() {
            if (app != null) {
                return personQuery.find()
            } else {
                return null
            }
        }

    fun initFoodCategory(){
        if (foodCategoryBox.count() == 0L) {
            val categorys = InitFoodCategory.createFoodCategory()
            foodCategoryBox.put(categorys)
            val c = foodCategoryQuery.find()
            InitFoods.createFoods(c)
        }

    }

}