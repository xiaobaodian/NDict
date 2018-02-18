package com.threecats.ndictdataset.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.R
import kotlinx.android.synthetic.main.activity_food_editer.*

class FoodEditerActivity : AppCompatActivity() {

    val currentFood = BDM.ShareSet?.CurrentFood

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_editer)
    }

    override fun onStart() {
        super.onStart()
        //NameIEditText.text = currentFood?.name
        NameIEditText.text.append(currentFood?.name)
        AliasIEditText.text.append(currentFood?.alias)
//        if (currentFood?.category == null) {
//            AliasIEditText.text.append("category is null")
//        } else {
//            var s: String? = currentFood?.category?.LongTitle
//            if (s == null) {
//                AliasIEditText.text.append("hhhhhhh")
//            } else {
//                AliasIEditText.text.append(s)
//            }
//        }

    }

    override fun onDestroy() {
        super.onDestroy()
        var oId = currentFood?.objectId
        Toast.makeText(this@FoodEditerActivity, oId, Toast.LENGTH_SHORT).show()
        var food = BFood()
        food.name = NameIEditText.text.toString()
        food.alias = AliasIEditText.text.toString()
        Toast.makeText(this@FoodEditerActivity, food.name+"/"+food.alias, Toast.LENGTH_SHORT).show()
        food.update(oId, object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    Toast.makeText(this@FoodEditerActivity, "更新了数据", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@FoodEditerActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
