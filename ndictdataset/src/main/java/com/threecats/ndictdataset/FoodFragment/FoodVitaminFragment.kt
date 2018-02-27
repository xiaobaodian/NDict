package com.threecats.ndictdataset.FoodFragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Bmob.BFoodVitamin
import com.threecats.ndictdataset.Enum.EditerState
import com.threecats.ndictdataset.EventClass.UpdateFoodRecyclerItem

import com.threecats.ndictdataset.R
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast


/**
 * A simple [Fragment] subclass.
 */
class FoodVitaminFragment : FoodPropertyFragment() {

    var vitaminItem: BFoodVitamin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (vitaminItem == null) {
            findVitaminItem(currentFood)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_food_vitamin, container, false)
    }

    override fun getFoodFields(): Int {
        val changeNumber = checkTextHelper.ChangeNumber()
        if (changeNumber > 0) {

        }
        return changeNumber
    }

    private fun findVitaminItem(food: BFood){
        var query: BmobQuery<BFoodVitamin> = BmobQuery()
        query.addWhereEqualTo("Food", BmobPointer(food))
        query.setLimit(1)
        query.findObjects(object: FindListener<BFoodVitamin>(){
            override fun done(vitamins: MutableList<BFoodVitamin>?, e: BmobException?) {
                if (e == null) {
                    vitamins?.forEach { vitaminItem = it }
                } else {
                    vitaminItem = BFoodVitamin()
                    vitaminItem!!.Food = food
                }
            }
        })
    }

    private fun saveVitaminItem(food: BFood){
        currentFood.save(object: SaveListener<String>() {
            override fun done(objectID: String?, e: BmobException?) {
                if (e == null) {
                    toast("添加了食材，objectID：$objectID")
                    EventBus.getDefault().post(UpdateFoodRecyclerItem(currentFood, EditerState.FoodAppend))  //Sticky
                } else {
                    toast("${e.message}")
                }
            }
        })
    }

}// Required empty public constructor
