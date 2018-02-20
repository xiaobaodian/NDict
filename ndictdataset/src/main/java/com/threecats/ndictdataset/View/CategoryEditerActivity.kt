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
import com.threecats.ndictdataset.Bmob.BFoodCategory
import com.threecats.ndictdataset.Enum.EditerState
import com.threecats.ndictdataset.EventClass.DeleteCategoryRecyclerItem
import com.threecats.ndictdataset.EventClass.DeleteFoodRecyclerItem
import com.threecats.ndictdataset.EventClass.UpdateCategoryRecyclerItem
import com.threecats.ndictdataset.EventClass.UpdateFoodRecyclerItem
import com.threecats.ndictdataset.R
import kotlinx.android.synthetic.main.activity_category_editer.*
import kotlinx.android.synthetic.main.activity_food_editer.*
import org.greenrobot.eventbus.EventBus

class CategoryEditerActivity : AppCompatActivity() {

    val currentCategory = BDM.ShareSet?.CurrentCategory
    var editTag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_editer)
        setSupportActionBar(CategoryEditerToolbar)
        CategoryEditerToolbar.setNavigationOnClickListener { onBackPressed() }

        with (LongTitleIEditText) {
            text.append(currentCategory?.LongTitle)
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

        with (ShortTitleIEditText) {
            text.append(currentCategory?.ShortTitle)
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

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_category_editer, menu!!)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.DeleteFood -> {
                //dialogDeleteFood()
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()

        if (editTag) updateItem()
    }

    private fun updateItem(){
        currentCategory?.LongTitle = LongTitleIEditText.text.toString()
        currentCategory?.ShortTitle = ShortTitleIEditText.text.toString()

        when (BDM.ShareSet?.ItemEditState){

            EditerState.CategoryAppend -> {
                currentCategory?.save(object: SaveListener<String>() {
                    override fun done(objectID: String?, e: BmobException?) {
                        if (e == null) {
                            Toast.makeText(this@CategoryEditerActivity, "添加了数据：$objectID", Toast.LENGTH_SHORT).show()
                            EventBus.getDefault().post(UpdateFoodRecyclerItem(BDM.ShareSet?.CurrentFood!!))  //Sticky
                        } else {
                            Toast.makeText(this@CategoryEditerActivity, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
            EditerState.CategoryEdit -> {
                currentCategory?.update(object: UpdateListener(){
                    override fun done(e: BmobException?) {
                        if (e == null) {
                            Toast.makeText(this@CategoryEditerActivity, "更新了分类数据", Toast.LENGTH_SHORT).show()
                            EventBus.getDefault().post(UpdateCategoryRecyclerItem(BDM.ShareSet?.CurrentCategory!!))  //Sticky
                        } else {
                            Toast.makeText(this@CategoryEditerActivity, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }

    private fun dialogDeleteCategory(){

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("删除分类").setMessage("确实要删除分类 ${currentCategory?.LongTitle} 吗？ ")

        //dialog.setNeutralButton("删除--",{ dialogInterface, i->
        //})

        dialog.setPositiveButton("删除", { dialogInterface, i->
            doDeleteCategory(currentCategory!!)
        })

        dialog.setNegativeButton("取消", { dialogInterface, i->

        })

        dialog.show()
    }

    private fun doDeleteCategory(category: BFoodCategory){
        category.delete(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    Toast.makeText(this@CategoryEditerActivity, "删除${category.LongTitle}成功", Toast.LENGTH_SHORT).show()
                    EventBus.getDefault().post(DeleteCategoryRecyclerItem(BDM.ShareSet?.CurrentCategory!!))
                    onBackPressed()
                } else {
                    Toast.makeText(this@CategoryEditerActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}
