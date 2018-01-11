package com.threecats.ndict.Models

import android.app.Application
import com.threecats.ndict.App
import com.threecats.ndict.Enum.EGender
import com.threecats.ndict.Helper.DateTime
import com.threecats.ndict.OriginalData.InitPerson
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import io.objectbox.query.Query

/**
 * 由 zhang 于 2018/1/6 创建
 */

class DataModel(app: App) {

    val app: App

    lateinit var personBox: Box<Person>
    lateinit var personQuery: Query<Person>

    lateinit var foodCategoryBox: Box<FoodCategory>
    lateinit var foodCategoryQuery: Query<FoodCategory>

    lateinit var foodBox: Box<Food>
    lateinit var foodQuery: Query<Food>

    init{
        this.app = app
        personBox = app.boxStore.boxFor<Person>()
        personQuery = personBox.query().build()
    }

    fun initPerson(){
        if (personBox.count().toInt() == 0) {
            val persons = InitPerson.createPersonData()
            personBox.put(persons)
        }
    }

}