package com.threecats.ndictdataset.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BFoodCategory
import com.threecats.ndictdataset.Enum.EditerState
import com.threecats.ndictdataset.EventClass.DeleteCategoryRecyclerItem
import com.threecats.ndictdataset.EventClass.UpdateCategoryRecyclerItem
import com.threecats.ndictdataset.Helper.EditTextHelper
import com.threecats.ndictdataset.R
import kotlinx.android.synthetic.main.activity_category_editer.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

class CategoryEditerActivity : AppCompatActivity() {

    val currentCategory = BDM.ShareSet!!.CurrentCategory!!
    val checkTextHelper = EditTextHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_editer)
        setSupportActionBar(CategoryEditerToolbar)
        CategoryEditerToolbar.setNavigationOnClickListener { onBackPressed() }

        checkTextHelper.addEditBox(LongTitleIEditText, currentCategory.LongTitle.toString())
        with (LongTitleIEditText) {
            addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun afterTextChanged(s: Editable?) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }

        checkTextHelper.addEditBox(ShortTitleIEditText, currentCategory.ShortTitle.toString())
        with (ShortTitleIEditText) {
            addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun afterTextChanged(s: Editable?) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
        checkTextHelper.initHash()
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
        if (checkTextHelper.ChangeNumber() > 0) updateCategory()
    }

    private fun updateCategory(){
        currentCategory.LongTitle = LongTitleIEditText.text.toString()
        currentCategory.ShortTitle = ShortTitleIEditText.text.toString()

        when (BDM.ShareSet?.ItemEditState){

            EditerState.CategoryAppend -> {
                currentCategory.save(object: SaveListener<String>() {
                    override fun done(objectID: String?, e: BmobException?) {
                        if (e == null) {
                            toast("添加了分类，objectID：$objectID")
                            EventBus.getDefault().post(UpdateCategoryRecyclerItem(currentCategory, EditerState.CategoryAppend))  //Sticky
                        } else {
                            toast("${e.message}")
                        }
                    }
                })
            }
            EditerState.CategoryEdit -> {
                currentCategory.update(object: UpdateListener(){
                    override fun done(e: BmobException?) {
                        if (e == null) {
                            toast("更新了分类数据")
                            EventBus.getDefault().post(UpdateCategoryRecyclerItem(currentCategory, EditerState.CategoryEdit))  //Sticky
                        } else {
                            toast("${e.message}")
                        }
                    }
                })
            }
        }
    }

    private fun alertDeleteCategory(){

        alert("确实要删除分类 ${currentCategory.LongTitle} 吗？", "删除分类") {
            positiveButton("确定") { deleteCategoryFromBmob(currentCategory) }
            negativeButton("取消") {  }
        }.show()

    }

    private fun deleteCategoryFromBmob(category: BFoodCategory){
        category.delete(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    toast("删除${category.LongTitle}成功")
                    EventBus.getDefault().post(DeleteCategoryRecyclerItem(currentCategory))
                    onBackPressed()
                } else {
                    toast("${e.message}")
                }
            }
        })
    }

}
