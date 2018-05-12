package com.threecats.ndictdataset.View

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Enum.EGender
import com.threecats.ndictdataset.Enum.ENutrientType
import com.threecats.ndictdataset.Enum.EPregnancy
import com.threecats.ndictdataset.EventClass.UpdateNutrient
import com.threecats.ndictdataset.Helper.next
import com.threecats.ndictdataset.Helper.previous
import com.threecats.ndictdataset.Models.ProposedDosage
import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.Shells.EditorShell.EEditState

import kotlinx.android.synthetic.main.activity_dosis_editer.*
import kotlinx.android.synthetic.main.content_dosis_editer.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

class DosisEditerActivity : AppCompatActivity() {

    private val shareSet = BDM.ShareSet!!

    private var nutrientType: ENutrientType = ENutrientType.Vitamin
    private val genderTitle: List<String> = listOf("女性", "男性", "不限性别")
    private val pregnancyTitle: List<String> = listOf("正常","孕全期","孕早期","孕中期","孕后期")
    private var genderID: Int = 2
    private var pregnancyStage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dosis_editer)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        if (shareSet.currentTraceElement == null) {
            toolbar.title = shareSet.currentNutrient?.name
        } else {
            toolbar.title = shareSet.currentTraceElement?.name
        }

//        when (shareSet.currentNutrient?.nutrientID){
//            5 -> {
//                nutrientType = ENutrientType.Vitamin
//                toolbar.title = "维生素推荐量"
//            }
//            6 -> {
//                nutrientType = ENutrientType.Mineral
//                toolbar.title = "微量元素推荐量"
//            }
//            else -> {
//                nutrientType = ENutrientType.Nutrient
//                toolbar.title = shareSet.currentNutrient?.name
//            }
//        }

        btnGender.setOnClickListener{
            genderID = genderTitle.previous(genderID)
            btnGenderShowTitle(genderID)
        }

        btnPregnancy.setOnClickListener{
            pregnancyStage = pregnancyTitle.next(pregnancyStage)
            btnPregnancyShowTitle(pregnancyStage)
        }

        if (shareSet.editorProposedDosage.isUpdate) {
            setFields()
        } else {
            btnGenderShowTitle(genderID)
        }


        with (etAgeRange){
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dosisediter_menu, menu!!)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.DeleteDosis -> {
                alert("确实要删除该摄入量条目吗？", "删除条目") {
                    positiveButton("确定") {
                        shareSet.editorProposedDosage.delete()
                        onBackPressed()  //返回动作会引发提交操作
                    }
                    negativeButton("取消") {  }
                }.show()
            }
            R.id.SaveAddDosis -> {
                shareSet.editorProposedDosage.commit()
                shareSet.editorProposedDosage.append(ProposedDosage())
                setFields()
            }
            R.id.CancelDosis -> {
                shareSet.editorProposedDosage.cancel()
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        getFields()
        shareSet.editorProposedDosage.commit()
        shareSet.currentNutrient?.let { EventBus.getDefault().post(UpdateNutrient(it)) }
        super.onDestroy()
    }

    private fun btnGenderShowTitle(index: Int){
        if (index !in 0 until genderTitle.size) { return }
        btnGender.text = genderTitle[index]
        if (index == 0) {
            btnPregnancy.visibility = View.VISIBLE
            btnPregnancyShowTitle(pregnancyStage)
        } else {
            btnPregnancy.visibility = View.GONE
        }
    }

    private fun btnPregnancyShowTitle(index: Int){
        if (index !in 0 until pregnancyTitle.size) { return }
        btnPregnancy.text = pregnancyTitle[index]
        if (index == EPregnancy.None.ordinal) {
            ageRangeLayout.visibility = View.VISIBLE  //ageRangeLayout
        } else {
            ageRangeLayout.visibility = View.GONE
        }
    }

    private fun setFields(){
        val proposedDosage = shareSet.editorProposedDosage.currentItem
        proposedDosage?.let {
            genderID = it.gender.ordinal
            pregnancyStage = it.pregnancy.ordinal
            btnGenderShowTitle(genderID)
            btnPregnancyShowTitle(pregnancyStage)
            etAgeRange.append(it.ageRange)
            etDosisRange.append(it.dosisRange)
        }
    }

    private fun getFields(){
        shareSet.editorProposedDosage.currentItem?.let {
            it.gender = EGender.values()[genderID]
            it.pregnancy = EPregnancy.values()[pregnancyStage]
            it.ageRange = etAgeRange.text.toString()
            it.dosisRange = etDosisRange.text.toString()
        }
    }

}
