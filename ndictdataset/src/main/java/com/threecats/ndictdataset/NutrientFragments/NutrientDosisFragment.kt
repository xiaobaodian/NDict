package com.threecats.ndictdataset.NutrientFragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BTraceElement
import com.threecats.ndictdataset.Bmob.ProposedDosage

import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.Shells.RecyclerViewShell.*
import kotlinx.android.synthetic.main.content_recycler_view.*


/**
 * A simple [Fragment] subclass.
 */
class NutrientDosisFragment : Fragment() {

    private val shareSet = BDM.ShareSet!!
    private var dosisListShell: RecyclerViewShell<Any, ProposedDosage>? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nutrient_dosis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (dosisListShell == null) {
            dosisListShell = RecyclerViewShell(context!!)
        }

        dosisListShell?.let {
            it.recyclerView(GRecyclerView).progressBar(GProgressBar).addViewType("item", ItemType.Item, R.layout.recycleritem_dosage)
            it.setDisplayItemListener(object : DisplayItemListener<Any, ProposedDosage> {
                override fun onDisplayItem(item:ProposedDosage , holder: RecyclerViewAdapter<Any, ProposedDosage>.ItemViewHolder) {
                }
            })
            it.setOnClickItemListener(object : ClickItemListener<Any, ProposedDosage> {
                override fun onClickItem(item: ProposedDosage, holder: RecyclerViewAdapter<Any, ProposedDosage>.ItemViewHolder) {
                }
            })
            it.setOnLongClickItemListener(object : LongClickItemListener<Any, ProposedDosage>{
                override fun onLongClickItem(item: ProposedDosage, holder: RecyclerViewAdapter<Any, ProposedDosage>.ItemViewHolder) {
                }
            })

            /*
            当前fragment建立实例的时候，表明是开始显示推荐摄入量的列表的状态。通过判断currentNutrient的nutrientID来
            判断是获取那个级别的ProposedDosage数组：
                nutrientID == 5,6   -> 需要查找currentTraceElement，然后获取ProposedDosage数组
                nutrientID == 其他值 -> 获取currentNutrient的ProposedDosage数组
            */
            it.setQueryDataListener(object : QueryDatasListener<Any, ProposedDosage>{
                override fun onQueryDatas(shell: RecyclerViewShell<Any, ProposedDosage>) {
                    when (shareSet.currentNutrient?.nutrientID){
                        in 5..6 -> {
                            shareSet.currentTraceElement?.demand?.forEach { dosisListShell?.addItem(it) }
                        }
                        else -> {
                            shareSet.currentNutrient?.proposedDosages?.forEach { dosisListShell?.addItem(it) }
                        }
                    }
                }
            })
            it.setOnNullDataListener((object : NullDataListener{
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
