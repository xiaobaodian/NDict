package com.threecats.ndictdataset.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.BuildConfig
import com.threecats.ndictdataset.Enum.EditerState
import com.threecats.ndictdataset.EventClass.DeleteFoodRecyclerItem
import com.threecats.ndictdataset.EventClass.UpdateFoodRecyclerItem
import com.threecats.ndictdataset.FoodFragment.*
import com.threecats.ndictdataset.R
import kotlinx.android.synthetic.main.activity_food_editer.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class FoodEditerActivity : AppCompatActivity() {

    val currentFood = BDM.ShareSet!!.CurrentFood!!
    val foodPropertyFragments = mutableListOf<FoodPropertyFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_editer)
        setSupportActionBar(FoodEditerToolbar)
        FoodEditerToolbar.setNavigationOnClickListener { onBackPressed() }  //FoodNutrientFragment

        addFragments(FoodNameFragment(),"名称")
        addFragments(FoodNoteFragment(),"描述")
        addFragments(FoodNutrientFragment(),"营养素")
        addFragments(FoodVitaminFragment(),"维生素")
        addFragments(FoodMineralFragment(),"矿物质")
        addFragments(FoodPictureFragment(),"图片")

        FoodEditerViewPage.adapter = FoodEditerGroupAdapter(supportFragmentManager, foodPropertyFragments)
        FoodPropertyTabs.setupWithViewPager(FoodEditerViewPage)
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

    private fun addFragments(fragment: FoodPropertyFragment, name: String){
        val bundle = Bundle()
        bundle.putString("name", name)
        fragment.setArguments(bundle)
        foodPropertyFragments.add(fragment)

        if (BuildConfig.DEBUG) {
            val logshow = AnkoLogger("NDIC")
            logshow.info { "设置Fragment参数传递" }
        }
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
