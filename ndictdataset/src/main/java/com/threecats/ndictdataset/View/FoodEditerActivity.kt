package com.threecats.ndictdataset.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Enum.EditerState
import com.threecats.ndictdataset.EventClass.UpdateFoodRecyclerItem
import com.threecats.ndictdataset.R
import kotlinx.android.synthetic.main.activity_food_editer.*
import org.greenrobot.eventbus.EventBus

class FoodEditerActivity : AppCompatActivity() {

    val currentFood = BDM.ShareSet?.CurrentFood
    var editTag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_editer)
        setSupportActionBar(FoodEditerToolbar)
        FoodEditerToolbar.setNavigationOnClickListener { onBackPressed() }

        with (NameIEditText) {
            text.append(currentFood?.name)
            addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun afterTextChanged(s: Editable?) {
                    editTag = true
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }

        with (AliasIEditText) {
            text.append(currentFood?.alias)
            addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun afterTextChanged(s: Editable?) {
                    editTag = true
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }

    }

    override fun onStart() {
        super.onStart()
        //NameIEditText.text = currentFood?.name
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.foodediter_menu, menu!!)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.DeleteFood -> {

            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()

        if (editTag) updateItem()
    }

    private fun updateItem(){
        currentFood?.name = NameIEditText.text.toString()
        currentFood?.alias = AliasIEditText.text.toString()

        when (BDM.ShareSet?.ItemEditState){
            EditerState.FoodAppend -> {
                currentFood?.category = BDM.ShareSet?.CurrentCategory
                currentFood?.save(object: SaveListener<String>() {
                    override fun done(objectID: String?, e: BmobException?) {
                        if (e == null) {
                            Toast.makeText(this@FoodEditerActivity, "添加了数据：$objectID", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@FoodEditerActivity, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
            EditerState.FoodEdit -> {
                currentFood?.update(object: UpdateListener(){
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
        EventBus.getDefault().post(UpdateFoodRecyclerItem(BDM.ShareSet?.CurrentFoodPosition!!))  //Sticky
    }

    private fun deleteFood(food: BFood){
        food.delete(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    Toast.makeText(this@FoodEditerActivity, "删除${food.name}成功", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@FoodEditerActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
}
