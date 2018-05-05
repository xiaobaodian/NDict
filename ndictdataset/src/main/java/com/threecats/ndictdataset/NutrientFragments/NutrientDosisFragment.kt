package com.threecats.ndictdataset.NutrientFragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Enum.EGender
import com.threecats.ndictdataset.Models.DosisGenderGroup
import com.threecats.ndictdataset.Models.ProposedDosage

import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.Shells.EditorShell.AppendItemListener
import com.threecats.ndictdataset.Shells.EditorShell.DeleteItemListener
import com.threecats.ndictdataset.Shells.EditorShell.UpdateItemListener
import com.threecats.ndictdataset.Shells.RecyclerViewShell.*
import com.threecats.ndictdataset.View.DosisEditerActivity
import kotlinx.android.synthetic.main.content_recycler_view.*
import org.jetbrains.anko.toast

/**
 * A simple [Fragment] subclass.
 */
class NutrientDosisFragment : Fragment() {

    private val shareSet = BDM.ShareSet!!

    private val humanGroup = DosisGenderGroup("所有人", EGender.None)
    private val femaleGroup = DosisGenderGroup("女性", EGender.Female)
    private val maleGroup = DosisGenderGroup("男性", EGender.Male)

    private var dosisListShell: RecyclerViewShell<DosisGenderGroup, ProposedDosage>? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        shareSet.editorProposedDosage.let {
            it.setOnAppendItemListener(object : AppendItemListener<ProposedDosage> {
                override fun onAppendItem(item: ProposedDosage) {
                    dosisListShell?.addItem(item)
                    shareSet.currentNutrient?.proposedDosages?.add(item)
                }
            })
            it.setOnUpdateItemListener(object : UpdateItemListener<ProposedDosage>{
                override fun onUpdateItem(item: ProposedDosage) {
                    dosisListShell?.updateItem(item)
                }
            })
            it.setOnDeleteItemListener(object : DeleteItemListener<ProposedDosage>{
                override fun onDeleteItem(item: ProposedDosage) {
                    dosisListShell?.removeItem(item)
                    shareSet.currentNutrient?.proposedDosages?.remove(item)
                }
            })
        }
        return inflater.inflate(R.layout.fragment_nutrient_dosis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (dosisListShell == null) {
            dosisListShell = RecyclerViewShell(context!!)
            dosisListShell?.let {
                it.addGroup(humanGroup)
                it.addGroup(femaleGroup)
                it.addGroup(maleGroup)
            }
        }

        dosisListShell?.let {
            it.recyclerView(GRecyclerView).progressBar(GProgressBar)
                    .addViewType("group",ItemType.Group, R.layout.recyclergroup_dosis)
                    .addViewType("item", ItemType.Item, R.layout.recycleritem_dosage)
            it.setDisplayGroupListener(object : DisplayGroupListener<DosisGenderGroup, ProposedDosage>{
                override fun onDisplayGroup(group: DosisGenderGroup, holder: RecyclerViewAdapter<DosisGenderGroup, ProposedDosage>.GroupViewHolder) {
                    holder.displayText(R.id.groupTitle, group.title)
                }
            })
            it.setDisplayItemListener(object : DisplayItemListener<DosisGenderGroup, ProposedDosage> {
                override fun onDisplayItem(item: ProposedDosage, holder: RecyclerViewAdapter<DosisGenderGroup, ProposedDosage>.ItemViewHolder) {
                    holder.displayText(R.id.ageRange, "（${item.gender.chinaName}） ${item.ageRange}岁")
                    holder.displayText(R.id.dosage, "${item.dosisRange}毫克/天")
                }
            })
            it.setOnClickItemListener(object : ClickItemListener<DosisGenderGroup, ProposedDosage> {
                override fun onClickItem(item: ProposedDosage, holder: RecyclerViewAdapter<DosisGenderGroup, ProposedDosage>.ItemViewHolder) {
                    shareSet.editorProposedDosage.edit(item)
                    val intent = Intent(context, DosisEditerActivity::class.java)
                    startActivity(intent)
                }
            })
            it.setOnLongClickItemListener(object : LongClickItemListener<DosisGenderGroup, ProposedDosage>{
                override fun onLongClickItem(item: ProposedDosage, holder: RecyclerViewAdapter<DosisGenderGroup, ProposedDosage>.ItemViewHolder) {
                }
            })

            /*
            当前fragment建立实例的时候，表明是开始显示推荐摄入量的列表的状态。通过判断currentNutrient的nutrientID来
            判断是获取那个级别的ProposedDosage数组：
                nutrientID == 5,6   -> 需要查找currentTraceElement，然后获取ProposedDosage数组
                nutrientID == 其他值 -> 获取currentNutrient的ProposedDosage数组
            */
            it.setQueryDataListener(object : QueryDatasListener<DosisGenderGroup, ProposedDosage>{
                override fun onQueryDatas(shell: RecyclerViewShell<DosisGenderGroup, ProposedDosage>) {
                    when (shareSet.currentNutrient?.nutrientID){
                        in 5..6 -> {
                            shareSet.currentTraceElement?.demand?.forEach { dosisListShell?.addItem(it) }
                        }
                        else -> {
                            shareSet.currentNutrient?.proposedDosages?.forEach { dosisListShell?.addItem(it) }
                        }
                    }
                    shell.completeQuery()
                }
            })
            it.setOnNullDataListener((object : NullDataListener{
                override fun onNullData(isNull: Boolean) {
                    if (isNull) {
                        //context?.toast("当前没有数据")
                    } else{
                        //context?.toast("已经添加数据")
                    }
                }
            }))
            it.link()
        }

    }
}// Required empty public constructor
