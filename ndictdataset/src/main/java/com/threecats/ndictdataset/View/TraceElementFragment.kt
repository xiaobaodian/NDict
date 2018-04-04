package com.threecats.ndictdataset.View


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BNutrient
import com.threecats.ndictdataset.Helper.ErrorMessage

import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.R.id.nutrientTitle
import com.threecats.ndictdataset.Shells.RecyclerViewShell.*
import kotlinx.android.synthetic.main.fragment_trace_element.*
import org.jetbrains.anko.toast


/**
 * A simple [Fragment] subclass.
 */
class TraceElementFragment : Fragment() {

    private val shareSet = BDM.ShareSet!!

    private var nutrientList: MutableList<BNutrient>? = null
    private var nutrientRView: RecyclerView? = null

    private var rvShell: RecyclerViewShell<Any, BNutrient>? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_trace_element, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (rvShell == null) {
            rvShell = RecyclerViewShell(context)
        }

        rvShell?.let {
            it.recyclerView(NutrientRView).progressBar(progressBarNutrient).addViewType("item", ItemType.Item, R.layout.nutrient_recycleritem)
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
                        if (e.name == "水"){
                            //context.toast("可以测试addItem功能了")
                            val n = BNutrient()
                            n.name = "测试项"
                            rvShell!!.addItem(n)
                        }
                    }
                }
            })
            it.setOnLongClickItemListener(object : onLongClickItemListener<Any, BNutrient>{
                override fun onLongClickItem(item: RecyclerViewItem<Any, BNutrient>, holder: RecyclerViewAdapter<Any, BNutrient>.ItemViewHolder) {
                    val e = item.getObject()
                    e?.let {
                        rvShell!!.removeItem(item)
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
                                    //shell.link()
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
                        context.toast("当前没有数据")
                    } else{
                        context.toast("已经添加数据")
                    }
                }
            }))
            it.link()
        }
    }
}// Required empty public constructor
