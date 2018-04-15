package com.threecats.ndictdataset.View


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.bmob.v3.BmobBatch
import cn.bmob.v3.BmobObject
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BatchResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListListener
import cn.bmob.v3.listener.UpdateListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BNutrient
import com.threecats.ndictdataset.Helper.ErrorMessage

import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.R.id.nutrientTitle
import com.threecats.ndictdataset.Shells.RecyclerViewShell.*
import kotlinx.android.synthetic.main.fragment_trace_element.*
import org.jetbrains.anko.toast
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.fixedRateTimer


/**
 * A simple [Fragment] subclass.
 */
class TraceElementFragment : Fragment() {

    private val shareSet = BDM.ShareSet!!

    private var nutrientList: MutableList<BNutrient>? = null
    //private var nutrientRView: RecyclerView? = null

    private var teShell: RecyclerViewShell<Any, BNutrient>? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_trace_element, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (teShell == null) {
            teShell = RecyclerViewShell(context)
        }

        teShell?.let {
            it.recyclerView(nutrientRView).progressBar(progressBarNutrient).addViewType("item", ItemType.Item, R.layout.nutrient_recycleritem)
            it.setDisplayItemListener(object : onDisplayItemListener<Any, BNutrient>{
                override fun onDisplayItem(item: RecyclerViewItem<Any, BNutrient>, holder: RecyclerViewAdapter<Any, BNutrient>.ItemViewHolder) {
                    val e = item.getObject() as BNutrient
                    holder.displayText(nutrientTitle, e.name)
                    //nutrientTitle.text = e.name
                }
            })
            it.setOnClickItemListener(object : onClickItemListener<Any, BNutrient>{
                override fun onClickItem(item: RecyclerViewItem<Any, BNutrient>, holder: RecyclerViewAdapter<Any, BNutrient>.ItemViewHolder) {
                    val e = item.getObject()
                    e?.let {
                        //it.add("testArray", "1")
                        //it.add("testArray", "2")
                        it.addAll("testArray", Arrays.asList("1","2"))
                        it.update(object : UpdateListener(){
                            override fun done(p0: BmobException?) {
                                if (p0 == null) {
                                    context.toast("加入数组成功")
                                } else {
                                    context.toast("加入数组没有成功")
                                }
                            }
                        })
                    }
                }
            })
            it.setOnLongClickItemListener(object : onLongClickItemListener<Any, BNutrient>{
                override fun onLongClickItem(item: RecyclerViewItem<Any, BNutrient>, holder: RecyclerViewAdapter<Any, BNutrient>.ItemViewHolder) {
                    val e = item.getObject()
                    e?.let {
                        //context.toast("${it.testArray?.size}")
                        it.remove("testArray")
                        it.update(object : UpdateListener() {
                            override fun done(p0: BmobException?) {
                                if (p0 == null) {
                                    context.toast("删除数组成功")
                                } else {
                                    context.toast("删除数组没有成功")
                                }
                            }
                        })
                    }
                }
            })
            it.setQueryDataListener(object : onQueryDatasListener<Any, BNutrient>{
                override fun onQueryDatas(shell: RecyclerViewShell<Any, BNutrient>) {
                    val query = BmobQuery<BNutrient>()
                    query.findObjects(object : FindListener<BNutrient>() {
                        override fun done(nutrients: MutableList<BNutrient>?, e: BmobException?) {
                            if (e == null) {
                                //progressBarNutrient.visibility = View.GONE
                                nutrients?.let {
                                    shell.addItems(it)
                                    shell.completeQuery()
                                }
                            } else {
                                if (view != null) {
                                    ErrorMessage(context, e)
                                }
                            }
                        }
                    })
                }
            })
            it.setOnNullDataListener((object : onNullDataListener{
                override fun onNullData(isNull: Boolean) {
                    if (isNull) {
                        //context.toast("当前没有数据")
                    } else{
                        //context.toast("已经添加数据")
                    }
                }
            }))
            it.link()
        }
    }
}// Required empty public constructor
