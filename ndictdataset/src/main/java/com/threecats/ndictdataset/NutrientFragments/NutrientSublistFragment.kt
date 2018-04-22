package com.threecats.ndictdataset.NutrientFragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import com.threecats.ndictdataset.Bmob.BTraceElement
import com.threecats.ndictdataset.Enum.EMeasure

import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.Shells.RecyclerViewShell.*
import kotlinx.android.synthetic.main.content_recycler_view.*
import org.jetbrains.anko.toast

/**
 * A simple [Fragment] subclass.
 *
 */

class NutrientSublistFragment : Fragment() {

    private var elementListShell: RecyclerViewShell<Any, BTraceElement>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nutrient_sublist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (elementListShell == null) {
            elementListShell = RecyclerViewShell(context!!)
        }

        elementListShell?.let {
            it.recyclerView(GRecyclerView).progressBar(GProgressBar).addViewType("item", ItemType.Item, R.layout.recycleritem_element)
            it.setDisplayItemListener(object : DisplayItemListener<Any, BTraceElement> {
                override fun onDisplayItem(item: BTraceElement, holder: RecyclerViewAdapter<Any, BTraceElement>.ItemViewHolder) {
                    holder.displayText(R.id.traceElementTitle,"${item.name}（${item.symbol}）")
                }
            })
            it.setOnClickItemListener(object : ClickItemListener<Any, BTraceElement> {
                override fun onClickItem(item: BTraceElement, holder: RecyclerViewAdapter<Any, BTraceElement>.ItemViewHolder) {
                }
            })
            it.setOnLongClickItemListener(object : LongClickItemListener<Any, BTraceElement>{
                override fun onLongClickItem(item: BTraceElement, holder: RecyclerViewAdapter<Any, BTraceElement>.ItemViewHolder) {
                }
            })
            it.setQueryDataListener(object : QueryDatasListener<Any, BTraceElement>{
                override fun onQueryDatas(shell: RecyclerViewShell<Any, BTraceElement>) {
                    val query = BmobQuery<BTraceElement>()
                    query.addWhereEqualTo("nutrientID", 5)
                    query.setLimit(200)
                    query.findObjects(object : FindListener<BTraceElement>(){
                        override fun done(elements: MutableList<BTraceElement>?, e: BmobException?) {
                            if (e == null) {
                                elements?.let{
                                    shell.addItems(it)
                                    shell.completeQuery()
                                }
                            } else {
                                context?.toast("加入元素数据失败")
                            }
                        }
                    })
                }
            })
            it.setOnNullDataListener((object : NullDataListener{
                override fun onNullData(isNull: Boolean) {
                    if (isNull) {
                        context?.toast("当前没有数据")
                    } else{
                        context?.toast("已经添加数据")
                    }
                }
            }))
            it.link()
        }
    }
}
