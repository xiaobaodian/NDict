package com.threecats.ndict.Models

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.threecats.ndict.App
import com.threecats.ndict.OriginalData.InitPerson
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Bmob.BFoodCategory
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import io.objectbox.query.Query

/**
 * 由 zhang 于 2018/1/6 创建
 */

object DataSet {

    lateinit var app: App

    lateinit var categories: MutableList<Category>
    lateinit var foods: MutableList<Food>
    lateinit var traceElements: MutableList<TraceElement>

    lateinit var personBox: Box<Person>
    lateinit var personQuery: Query<Person>

    lateinit var categoryBox: Box<Category>
    lateinit var categoryQuery: Query<Category>

    lateinit var foodBox: Box<Food>
    lateinit var foodQuery: Query<Food>

    lateinit var mineralBox : Box<Mineral>
    lateinit var mineralQuery: Query<Mineral>

    lateinit var vitaminBox : Box<Vitamin>
    lateinit var vitaminQuery: Query<Vitamin>

    lateinit var currentPerson: Person

    fun init(app: App){
        this.app = app

        this.personBox = this.app.boxStore.boxFor<Person>()
        this.personQuery = personBox.query().build()

        this.categoryBox = this.app.boxStore.boxFor<Category>()
        this.categoryQuery = categoryBox.query().build()

        this.foodBox = this.app.boxStore.boxFor<Food>()
        this.foodQuery = foodBox.query().build()
    }

    fun initPerson(): Boolean {
        if (personBox.count().toInt() == 0) {
            val persons = InitPerson.createPerson()
            personBox.put(persons)
        }
        val persons = personQuery.find()
        currentPerson = persons[0]
        return true
    }

    val persons: MutableList<Person>?
        get() = personQuery.find()

    private fun stepCategory(){
        if (categoryBox.count() == 0L) {
            val query = BmobQuery<BFoodCategory>()
            query.findObjects(object : FindListener<BFoodCategory>() {
                override fun done(categorys: MutableList<BFoodCategory>?, e: BmobException?) {
                    if (e == null) {
                        categorys?.let {
                            stepFood(categorys)
                        }
                    } else {
                        //ErrorMessage(context!!, e)
                    }
                }
            })
        }
    }

    private fun stepFood(categories: MutableList<BFoodCategory>){
        categories.forEach {
            val category = Category(it.categoryID, it.longTitle, it.shortTitle, it.objectId, it.updatedAt)
            val foods: MutableList<Food> = ArrayList()
            val query: BmobQuery<BFood> = BmobQuery()
            query.addWhereEqualTo("category", BmobPointer(it))
            query.setLimit(300)
            query.findObjects(object: FindListener<BFood>(){
                override fun done(foods: MutableList<BFood>?, e: BmobException?) {
                    if (e == null) {
                        foods?.let {
                            it.forEach {
                                val food = Food(
                                        it.name,
                                        it.alias,
                                        it.foodBased,
                                        it.protein,
                                        it.foodFiber,
                                        it.fat,
                                        it.carbohydrate,
                                        it.calories,
                                        it.water,
                                        it.cholesterol,
                                        it.objectId,
                                        it.updatedAt
                                 )
                                food.category.target = category
                                val foodID: Long = foodBox.put(food)
                                val mineral = Mineral(
                                        foodID,
                                        food.minerals[0].content,
                                        food.minerals[1].content,
                                        food.minerals[2].content,
                                        food.minerals[3].content,
                                        food.minerals[4].content,
                                        food.minerals[5].content,
                                        food.minerals[6].content,
                                        food.minerals[7].content,
                                        food.minerals[8].content,
                                        food.minerals[9].content,
                                        food.minerals[10].content,
                                        food.minerals[11].content,
                                        food.minerals[12].content,
                                        food.minerals[13].content,
                                        food.minerals[14].content,
                                        food.minerals[15].content,
                                        food.minerals[16].content,
                                        food.minerals[17].content,
                                        food.minerals[18].content,
                                        food.minerals[19].content,
                                        food.minerals[20].content
                                )
                                mineralBox.put(mineral)
                                val vitamin = Vitamin(
                                        foodID,
                                        food.vitamins[0].content,
                                        food.vitamins[1].content,
                                        food.vitamins[2].content,
                                        food.vitamins[3].content,
                                        food.vitamins[4].content,
                                        food.vitamins[5].content,
                                        food.vitamins[6].content,
                                        food.vitamins[7].content,
                                        food.vitamins[8].content,
                                        food.vitamins[9].content,
                                        food.vitamins[10].content,
                                        food.vitamins[11].content,
                                        food.vitamins[12].content,
                                        food.vitamins[13].content,
                                        food.vitamins[14].content,
                                        food.vitamins[15].content,
                                        food.vitamins[16].content
                                )
                                vitaminBox.put(vitamin)
                            }
                        }
                    } else {
                        //toast("${e.message}")
                        //ErrorMessage(this@FoodListActivity, e)
                    }
                }
            })
        }

    }

}