package com.threecats.ndictdataset.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
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
import com.threecats.ndictdataset.EventClass.DeleteFoodRecyclerItem
import com.threecats.ndictdataset.EventClass.UpdateFoodRecyclerItem
import com.threecats.ndictdataset.Helper.CheckTextHelper
import com.threecats.ndictdataset.R
import kotlinx.android.synthetic.main.activity_food_editer.*
import org.greenrobot.eventbus.EventBus

class FoodEditerActivity : AppCompatActivity() {

    val currentFood = BDM.ShareSet?.CurrentFood
    val checkTextHelp = CheckTextHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_editer)
        setSupportActionBar(FoodEditerToolbar)
        FoodEditerToolbar.setNavigationOnClickListener { onBackPressed() }

        checkTextHelp.addEditBox(NameIEditText)
        with (NameIEditText) {
            text.append(currentFood?.name)
            addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun afterTextChanged(s: Editable?) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }

        checkTextHelp.addEditBox(AliasIEditText)
        with (AliasIEditText) {
            text.append(currentFood?.alias)
            addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun afterTextChanged(s: Editable?) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
        checkTextHelp.initHash()
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
                dialogDeleteFood()
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()

        if (checkTextHelp.hasChanged() > 0) updateItem()
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
                            EventBus.getDefault().post(UpdateFoodRecyclerItem(BDM.ShareSet?.CurrentFood!!))  //Sticky
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
                            EventBus.getDefault().post(UpdateFoodRecyclerItem(BDM.ShareSet?.CurrentFood!!))  //Sticky
                        } else {
                            Toast.makeText(this@FoodEditerActivity, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }

    private fun dialogDeleteFood(){

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("删除食材").setMessage("确实要删除食材 ${currentFood?.name} 吗？ 位置：${BDM.ShareSet?.CurrentFoodPosition}")

        //dialog.setNeutralButton("删除--",{ dialogInterface, i->
        //})

        dialog.setPositiveButton("删除", { dialogInterface, i->
            doDeleteFood(currentFood!!)
        })

        dialog.setNegativeButton("取消", { dialogInterface, i->

        })

        dialog.show()
    }

    private fun doDeleteFood(food: BFood){
        food.delete(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    Toast.makeText(this@FoodEditerActivity, "删除${food.name}成功", Toast.LENGTH_SHORT).show()
                    EventBus.getDefault().post(DeleteFoodRecyclerItem(BDM.ShareSet?.CurrentFood!!))
                    onBackPressed()
                } else {
                    Toast.makeText(this@FoodEditerActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}
