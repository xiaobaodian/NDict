package com.threecats.ndictdataset.Models

import cn.bmob.v3.BmobBatch
import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BatchResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListListener
import cn.bmob.v3.listener.UpdateListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.*
import com.threecats.ndictdataset.Enum.EditerState
import com.threecats.ndictdataset.EventClass.UpdateCategoryRecyclerItem
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

/**
 * 由 zhang 创建于 2018/3/10.
 */
class DataModel {

    val shareSet = BDM.ShareSet!!
    val context = shareSet.AppContext

    fun updateCategoryFoodSize(category: BFoodCategory, size: Int){
        category.foodTotal = size
        category.update(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    EventBus.getDefault().post(UpdateCategoryRecyclerItem(category, EditerState.CategoryEdit))
                } else {
                    context.toast("${e.message}")
                }
            }
        })
    }

    fun checkFoodExtRelevant(foods: MutableList<BFood>){

        val Vitamins: MutableList<BmobObject> = arrayListOf()
        val Minerals: MutableList<BmobObject> = arrayListOf()
        val Mineralexts: MutableList<BmobObject> = arrayListOf()

        foods.forEach {

            val vitamin = it.vitamin ?: BFoodVitamin()
            val mineral = it.mineral ?: BFoodMineral()
            val mineralExt = it.mineralExt ?: BFoodMineralExt()

            if (vitamin.foodID == null) {
                vitamin.foodID = it.objectId
                Vitamins.add(vitamin)
            }
            if (mineral.foodID == null) {
                mineral.foodID = it.objectId
                Minerals.add(mineral)
            }
            if (mineralExt.foodID == null) {
                mineralExt.foodID = it.objectId
                Mineralexts.add(mineralExt)
            }
        }

        if (Vitamins.size > 0) {
            BmobBatch().updateBatch(Vitamins).doBatch(object: QueryListListener<BatchResult>(){
                override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                    if (e == null) {
                        context.toast("补增了${results?.size}个矿物资扩展记录")
                    } else {
                        context.toast("${e.message}")
                    }
                }
            })
        }

        if (Minerals.size > 0) {
            BmobBatch().updateBatch(Minerals).doBatch(object: QueryListListener<BatchResult>(){
                override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                    if (e == null) {
                        context.toast("补增了${results?.size}个矿物质记录")
                    } else {
                        context.toast("${e.message}")
                    }
                }
            })
        }

        if (Mineralexts.size > 0) {
            BmobBatch().updateBatch(Mineralexts).doBatch(object: QueryListListener<BatchResult>(){
                override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                    if (e == null) {
                        context.toast("补增了${results?.size}个矿物资扩展记录")
                    } else {
                        context.toast("${e.message}")
                    }
                }
            })
        }

    }
}