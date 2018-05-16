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

    lateinit var categories: List<Category>
    lateinit var traceElements: List<TraceElement>

    lateinit var personBox: Box<Person>
    lateinit var personQuery: Query<Person>

    lateinit var categoryBox: Box<Category>
    lateinit var categoryQuery: Query<Category>

    lateinit var foodBox: Box<Food>
    lateinit var foodQuery: Query<Food>

    lateinit var currentPerson: Person

    fun init(app: App){
        this.app = app

        this.personBox = this.app!!.boxStore.boxFor<Person>()
        this.personQuery = personBox.query().build()

        this.categoryBox = this.app!!.boxStore.boxFor<Category>()
        this.categoryQuery = categoryBox.query().build()

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
        if (categoryBox.count() == 0L) {
            val categorys = InitFoodCategory.createFoodCategory()
            categoryBox.put(categorys)
            val c = categoryQuery.find()
            InitFoods.createFoods(c)
            categoryBox.put(c)
        }

    }

}