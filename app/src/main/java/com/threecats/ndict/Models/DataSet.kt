package com.threecats.ndict.Models

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.threecats.ndict.App
import com.threecats.ndict.Helper.ErrorMessage
import com.threecats.ndict.OriginalData.InitPerson
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Bmob.BFoodCategory
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import io.objectbox.query.Query
import org.jetbrains.anko.toast

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

        personBox = app.boxStore.boxFor<Person>()
        personQuery = personBox.query().build()

        categoryBox = app.boxStore.boxFor<Category>()
        categoryQuery = categoryBox.query().build()

        foodBox = app.boxStore.boxFor<Food>()
        foodQuery = foodBox.query().build()

        mineralBox = app.boxStore.boxFor<Mineral>()
        mineralQuery = mineralBox.query().build()

        vitaminBox = app.boxStore.boxFor<Vitamin>()
        vitaminQuery = vitaminBox.query().build()
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

    fun initObjectBox(){
        if (categoryBox.count() == 0L) {
            stepCategory()
        } else {
            val categorys = categoryQuery.find()
        }

    }

    private fun stepCategory(){
        val query = BmobQuery<BFoodCategory>()
        query.findObjects(object : FindListener<BFoodCategory>() {
            override fun done(categorys: MutableList<BFoodCategory>?, e: BmobException?) {
                if (e == null) {
                    categorys?.let {
                        stepFood(categorys,0)
                    }
                } else {
                    ErrorMessage(app.applicationContext, e).errorTips()
                }
            }
        })
    }

    private fun stepFood(categories: MutableList<BFoodCategory>, position: Int){
        if (position >= categories.size) {
            app.applicationContext.toast("分类及食材资料已经导入")
            return
        }
        val currentCategory = categories[position]
        val category = Category(currentCategory.categoryID, currentCategory.longTitle, currentCategory.shortTitle, currentCategory.objectId, currentCategory.updatedAt)
        //val foods: MutableList<Food> = ArrayList()
        val query: BmobQuery<BFood> = BmobQuery()
        query.addWhereEqualTo("category", BmobPointer(currentCategory))
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
                            val foodId: Long = foodBox.put(food)
                            val mineral = Mineral(foodID = foodId)
                            mineralBox.put(mineral)
                            val vitamin = Vitamin(foodID = foodId)
                            vitaminBox.put(vitamin)
                        }
                    }
                    stepFood(categories, position+1)
                } else {
                    ErrorMessage(app.applicationContext, e).errorTips()
                }
            }
        })
    }

//    it.minerals[0].content,
//    it.minerals[1].content,
//    it.minerals[2].content,
//    it.minerals[3].content,
//    it.minerals[4].content,
//    it.minerals[5].content,
//    it.minerals[6].content,
//    it.minerals[7].content,
//    it.minerals[8].content,
//    it.minerals[9].content,
//    it.minerals[10].content,
//    it.minerals[11].content,
//    it.minerals[12].content,
//    it.minerals[13].content,
//    it.minerals[14].content,
//    it.minerals[15].content,
//    it.minerals[16].content,
//    it.minerals[17].content,
//    it.minerals[18].content,
//    it.minerals[19].content,
//    it.minerals[20].content

//    it.vitamins[0].content,
//    it.vitamins[1].content,
//    it.vitamins[2].content,
//    it.vitamins[3].content,
//    it.vitamins[4].content,
//    it.vitamins[5].content,
//    it.vitamins[6].content,
//    it.vitamins[7].content,
//    it.vitamins[8].content,
//    it.vitamins[9].content,
//    it.vitamins[10].content,
//    it.vitamins[11].content,
//    it.vitamins[12].content,
//    it.vitamins[13].content,
//    it.vitamins[14].content,
//    it.vitamins[15].content,
//    it.vitamins[16].content

}