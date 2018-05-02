package com.threecats.ndictdataset.Models

import cn.bmob.v3.BmobBatch
import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BatchResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListListener
import cn.bmob.v3.listener.UpdateListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.*
import com.threecats.ndictdataset.Enum.EEditerState
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
                    //EventBus.getDefault().post(UpdateCategoryRecyclerItem(category, EEditerState.CategoryEdit))
                } else {
                    context.toast("${e.message}")
                }
            }
        })
    }

    fun updateNutrient(nutrient: BNutrient){
        nutrient.update()
    }

}