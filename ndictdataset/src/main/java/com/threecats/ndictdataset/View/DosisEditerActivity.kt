package com.threecats.ndictdataset.View

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.View
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Enum.EGender
import com.threecats.ndictdataset.Enum.ENutrientType
import com.threecats.ndictdataset.R

import kotlinx.android.synthetic.main.activity_dosis_editer.*
import kotlinx.android.synthetic.main.content_dosis_editer.*
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

        when (shareSet.currentNutrient?.nutrientID){
            5 -> {
                nutrientType = ENutrientType.Vitamin
                toolbar.title = ""
            }
            6 -> {
                nutrientType = ENutrientType.Mineral
                toolbar.title = ""
            }
            else -> {
                nutrientType = ENutrientType.Nutrient
                toolbar.title = shareSet.currentNutrient?.name
            }
        }
        btnGenderShowTitle(genderID)
        btnGender.setOnClickListener{
            if (--genderID < 0) {
                genderID = 2
            }
            btnGenderShowTitle(genderID)
        }

        btnPregnancy.setOnClickListener{
            isPregnancy = !isPregnancy
            btnPregnancyShowTitle()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.foodlist_menu, menu!!)
        return super.onCreateOptionsMenu(menu)
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

}
