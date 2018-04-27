package com.threecats.ndictdataset.View

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Enum.ENutrientType
import com.threecats.ndictdataset.R

import kotlinx.android.synthetic.main.activity_dosis_editer.*

class DosisEditerActivity : AppCompatActivity() {

    private val shareSet = BDM.ShareSet!!

    private var nutrientType: ENutrientType = ENutrientType.Vitamin

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

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.foodlist_menu, menu!!)
        return super.onCreateOptionsMenu(menu)
    }

}
