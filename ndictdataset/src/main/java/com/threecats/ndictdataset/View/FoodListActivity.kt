package com.threecats.ndictdataset.View

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import cn.bmob.v3.BmobBatch
import cn.bmob.v3.BmobObject
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BatchResult
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.*
import com.threecats.ndictdataset.Enum.EEditerState
import com.threecats.ndictdataset.EventClass.*
import com.threecats.ndictdataset.Helper.ErrorMessage
import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.Shells.EditorShell.AppendItemListener
import com.threecats.ndictdataset.Shells.EditorShell.DeleteItemListener
import com.threecats.ndictdataset.Shells.EditorShell.UpdateItemListener
import com.threecats.ndictdataset.Shells.RecyclerViewShell.*

import kotlinx.android.synthetic.main.activity_food_list.*
import kotlinx.android.synthetic.main.content_recycler_view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import java.util.ArrayList

class FoodListActivity : AppCompatActivity() {

    private val shareSet = BDM.ShareSet!!
    private lateinit var currentCategory: BFoodCategory
    private var foodListShell: RecyclerViewShell<Any, BFood>? = null

    init{
        if (shareSet.currentCategory == null) {
            onBackPressed()
        } else {
            currentCategory = shareSet.currentCategory!!
        }
    }

    //private var foodList: MutableList<BFood>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)
        setSupportActionBar(FoodToolbar)

        with (FoodToolbar){
            title = currentCategory.longTitle
            setNavigationOnClickListener { onBackPressed() }
        }

        fab.setOnClickListener {
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show()
            //shareSet.createFood()
            shareSet.editorFood.append(BFood().apply { category = currentCategory })
            shareSet.currentFood = shareSet.editorFood.currentItem      // 考虑去掉
            val intent = Intent(this@FoodListActivity, FoodEditerActivity::class.java)
            startActivity(intent)
        }

        //GRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        if (foodListShell == null) {
            foodListShell = RecyclerViewShell(this)
        }

        foodListShell?.let {
            it.recyclerView(GRecyclerView).progressBar(GProgressBar).addViewType("item", ItemType.Item, R.layout.recycleritem_food)
            it.setDisplayItemListener(object : DisplayItemListener<Any, BFood> {
                override fun onDisplayItem(item: BFood, holder: RecyclerViewAdapter<Any, BFood>.ItemViewHolder) {
                    val e = item
                    val updateTime = if (e.updatedAt == null) "" else e.updatedAt
                    holder.displayText(R.id.ItemName, if (e.alias.isEmpty()) e.name else "${e.name}、${e.alias}")
                    holder.displayText(R.id.ItemAlias, updateTime)
                }
            })
            it.setOnClickItemListener(object : ClickItemListener<Any, BFood> {
                override fun onClickItem(item: BFood, holder: RecyclerViewAdapter<Any, BFood>.ItemViewHolder) {
                    //shareSet.editFood(item)
                    shareSet.editorFood.edit(item)
                    shareSet.currentFood = item
                    val intent = Intent(it.context, FoodEditerActivity::class.java)
                    startActivity(intent)
                }
            })
            it.setOnLongClickItemListener(object : LongClickItemListener<Any, BFood> {
                override fun onLongClickItem(item: BFood, holder: RecyclerViewAdapter<Any, BFood>.ItemViewHolder) {
                    //shareSet.editFood(item)
                    shareSet.editorFood.edit(item)
                    shareSet.currentFood = item
                    val intent = Intent(it.context, FoodEditerActivity::class.java)
                    startActivity(intent)
                }
            })
            it.setQueryDataListener(object : QueryDatasListener<Any, BFood> {
                override fun onQueryDatas(shell: RecyclerViewShell<Any, BFood>) {
                    val query: BmobQuery<BFood> = BmobQuery()
                    query.addWhereEqualTo("category", BmobPointer(currentCategory))
                    //query.include("vitamin,mineral,mineralExt,article")
                    query.setLimit(300)
                    query.findObjects(object: FindListener<BFood>(){
                        override fun done(foods: MutableList<BFood>?, e: BmobException?) {
                            if (e == null) {
                                foods?.let {
                                    shell.addItems(it)
                                    shell.completeQuery()
                                    if (currentCategory.foodTotal != it.size) updateCategoryFoodSize(it.size, true)
//                                    foodList = it
//                                    FoodRView.adapter = FoodListAdapter(it, this@FoodListActivity)
                                }

                            } else {
                                //toast("${e.message}")
                                ErrorMessage(this@FoodListActivity, e)
                            }
                        }
                    })
                }
            })
            it.setOnNullDataListener((object : NullDataListener {
                override fun onNullData(isNull: Boolean) {
                    if (isNull) {
                        //toast("当前没有数据")
                    } else{
                        //toast("已经添加数据")
                    }
                }
            }))
            it.link()
        }

        shareSet.editorFood.let {
            it.setOnAppendItemListener(object: AppendItemListener<BFood> {
                override fun onAppendItem(item: BFood) {
                    if (item.name.isEmpty()) return
                    item.category = shareSet.currentCategory
                    item.save(object: SaveListener<String>() {
                        override fun done(objectID: String?, e: BmobException?) {
                            if (e == null) {
                                foodListShell?.addItem(item)
                                updateCategoryFoodSize(foodListShell?.itemsSize()!!)
                                if (BDM.ShowTips) toast("添加了食材[${item.name}]，objectID：$objectID")
                            } else {
                                longToast("添加食材${item.name}出现错误。回滚数据 !")
                                foodListShell?.removeItem(item)
                                //ErrorMessage(applicationContext, e)
                            }
                        }
                    })
                }
            })
            it.setOnUpdateItemListener(object : UpdateItemListener<BFood>{
                override fun onUpdateItem(item: BFood) {
                    if (item.name.isEmpty()) return
                    item.update(object: UpdateListener(){
                        override fun done(e: BmobException?) {
                            if (e == null) {
                                foodListShell?.updateItem(item)
                                if (BDM.ShowTips) toast("更新了食材数据")
                            } else {
                                longToast("更新食材${item.name}出现错误。数据未保存 !")
                                //ErrorMessage(applicationContext, e)
                            }
                        }
                    })
                }
            })
            it.setOnDeleteItemListener(object : DeleteItemListener<BFood>{
                override fun onDeleteItem(item: BFood) {
                    item.delete(object: UpdateListener(){
                        override fun done(e: BmobException?) {
                            if (e == null) {
                                foodListShell?.removeItem(item)
                                toast("删除食材 ${item.name} 成功")
                            } else {
                                ErrorMessage(applicationContext, e)
                            }
                        }
                    })
                }
            })
        }

        EventBus.getDefault().register(this@FoodListActivity)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.foodlist_menu, menu!!)
        return super.onCreateOptionsMenu(menu)
    }

//    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
//        return super.onPrepareOptionsMenu(menu)
//    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.CheckFoodElement -> {
                toast("检查元素关联")
                //checkFoodRelevant()
            }
            R.id.SaveAddItem -> {

            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this@FoodListActivity)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)  //, sticky = true
    fun doUpdateRecyclerItem(updateItem: UpdateFoodRecyclerItem){
        when (updateItem.state){
            EEditerState.FoodEdit -> {
                foodListShell?.updateItem(updateItem.food)
            }
            EEditerState.FoodAppend -> {
                foodListShell?.addItem(updateItem.food)
                updateCategoryFoodSize(foodListShell?.itemsSize()!!)
            }
            else -> toast("EditState Error !")
        }
        //EventBus.getDefault().removeStickyEvent(updateItem)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)  //, sticky = true
    fun doDeleteFoodRecyclerItem(deleteItem: DeleteFoodRecyclerItem){
        foodListShell?.let {
            it.removeCurrentItem(deleteItem.food)
            updateCategoryFoodSize(it.itemsSize())
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doBatchUpdateFood(updateItems: BatchUpdateFood){
        val batchFoods: MutableList<BmobObject> = ArrayList()
        updateItems.Foods.forEach { batchFoods.add(it) }
        BmobBatch().updateBatch(batchFoods).doBatch(object: QueryListListener<BatchResult>(){
            override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                if (e == null) {
                    toast("更新了 ${results?.size} 个对 ${updateItems.Title} 记录的引用")
                } else {
                    //toast("${e.message}")
                    ErrorMessage(this@FoodListActivity, e)
                }
            }
        })
    }

    // 已建立DataModel代码
    private fun updateCategoryFoodSize(size: Int, showMessage: Boolean = false){
        currentCategory.foodTotal = size
        currentCategory.update(object: UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null) {
                    EventBus.getDefault().post(UpdateCategoryRecyclerItem(currentCategory, EEditerState.CategoryEdit))
                    if (showMessage) {
                        toast("更新 ${currentCategory.longTitle} 类的食材总数：$size ")
                    }
                } else {
                    //toast("${e.message}")
                    ErrorMessage(this@FoodListActivity, e)
                }
            }
        })
    }
}
