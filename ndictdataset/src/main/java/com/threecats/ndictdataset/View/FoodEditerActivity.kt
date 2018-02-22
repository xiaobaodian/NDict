package com.threecats.ndictdataset.View

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Enum.EditerState
import com.threecats.ndictdataset.EventClass.DeleteFoodRecyclerItem
import com.threecats.ndictdataset.EventClass.UpdateFoodRecyclerItem
import com.threecats.ndictdataset.FoodFragment.FoodNameFragment
import com.threecats.ndictdataset.FoodFragment.FoodNoteFragment
import com.threecats.ndictdataset.FoodFragment.FoodPropertyFragment
import com.threecats.ndictdataset.Helper.CheckTextHelper
import com.threecats.ndictdataset.R
import kotlinx.android.synthetic.main.activity_food_editer.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

class FoodEditerActivity : AppCompatActivity() {

    val currentFood = BDM.ShareSet!!.CurrentFood!!
    val foodPropertyFragments = mutableListOf<FoodPropertyFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_editer)
        setSupportActionBar(FoodEditerToolbar)
        FoodEditerToolbar.setNavigationOnClickListener { onBackPressed() }
        foodPropertyFragments.add(FoodNameFragment())
        foodPropertyFragments.add(FoodNoteFragment())
        //FoodEditerViewPage.adapter = FoodEditerGroupAdapter(supportFragmentManager, foodPropertyFragments)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.foodediter_menu, menu!!)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.DeleteFood -> {
                alertDeleteFood()
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        //if (checkTextHelper.ChangeNumber() > 0) updateFood()
    }

    private fun updateFood(){
        //currentFood.name = NameIEditText.text.toString()
        //currentFood.alias = AliasIEditText.text.toString()

        when (BDM.ShareSet?.ItemEditState){
            EditerState.FoodAppend -> {
                currentFood.category = BDM.ShareSet?.CurrentCategory
                currentFood.save(object: SaveListener<String>() {
                    override fun done(objectID: String?, e: BmobException?) {
                        if (e == null) {
                            toast("添加了食材，objectID：$objectID")
                            EventBus.getDefault().post(UpdateFoodRecyclerItem(currentFood))  //Sticky
                        } else {
                            toast("${e.message}")
                        }
                    }
                })
            }
            EditerState.FoodEdit -> {
                currentFood.update(object: UpdateListener(){
                    override fun done(e: BmobException?) {
                        if (e == null) {
                            toast("更新了数据")
                            EventBus.getDefault().post(UpdateFoodRecyclerItem(currentFood))  //Sticky
                        } else {
                            toast("${e.message}")
                        }
                    }
                })
            }
        }
    }

    private fun alertDeleteFood(){

        alert("确实要删除食材 ${currentFood.name} 吗？", "删除食材") {
            positiveButton("确定") { deleteFoodFromBmob(currentFood) }
            negativeButton("取消") {  }
        }.show()

    }

    private fun deleteFoodFromBmob(food: BFood){
        food.delete(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    toast("删除${food.name}成功")
                    EventBus.getDefault().post(DeleteFoodRecyclerItem(currentFood))
                    onBackPressed()
                } else {
                    toast("${e.message}")
                }
            }
        })
    }

}
