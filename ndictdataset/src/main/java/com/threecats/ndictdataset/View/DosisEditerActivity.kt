package com.threecats.ndictdataset.View

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Enum.EGender
import com.threecats.ndictdataset.Enum.ENutrientType
import com.threecats.ndictdataset.EventClass.UpdateNutrient
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
    private var genderID: Int = 2
    private var isPregnancy = false

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
            if (--genderID < 0) genderID = 2
            btnGenderShowTitle(genderID)
        }

        btnPregnancy.setOnClickListener{
            isPregnancy = !isPregnancy
            shareSet.editorProposedDosage.currentItem?.pregnancy = isPregnancy
            btnPregnancyShowTitle()
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
        super.onDestroy()
        getFields()
        shareSet.editorProposedDosage.commit()
        shareSet.currentNutrient?.let { EventBus.getDefault().post(UpdateNutrient(it)) }
    }

    private fun btnGenderShowTitle(index: Int){
        if (index !in 0..2) { return }
        btnGender.text = genderTitle[index]
        if (index == 0) {
            btnPregnancy.visibility = View.VISIBLE
            btnPregnancyShowTitle()
        } else {
            btnPregnancy.visibility = View.GONE
        }
    }

    private fun btnPregnancyShowTitle(){
        btnPregnancy.text = if (isPregnancy) "孕期" else "正常"
    }

    private fun setFields(){
        val proposedDosage = shareSet.editorProposedDosage.currentItem
        proposedDosage?.let {
            genderID = it.gender.ordinal
            isPregnancy = it.pregnancy
            btnGenderShowTitle(genderID)
            btnPregnancyShowTitle()
            etAgeRange.append(it.ageRange)
            etDosisRange.append(it.dosisRange)
        }
    }

    private fun getFields(){
        shareSet.editorProposedDosage.currentItem?.let {
            it.gender = EGender.values()[genderID]
            it.ageRange = etAgeRange.text.toString()
            it.dosisRange = etDosisRange.text.toString()
        }
    }

}
