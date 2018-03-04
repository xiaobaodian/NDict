package com.threecats.ndictdataset.FoodFragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndictdataset.Bmob.BFood

import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.View.FoodEditerActivity
import org.jetbrains.anko.toast


/**
 * A simple [Fragment] subclass.
 */
class FoodPictureFragment : FoodPropertyFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //initShareVar()
        return inflater!!.inflate(R.layout.fragment_food_picture, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (initFieldsFlag) {
            initFieldsFlag = false
            ImportFields(shareSet.CurrentFood!!)
        }
    }

    override fun BlockChangeState(parent: FoodEditerActivity) {
        val changeNumber = foodEditTextHelper.ChangeNumber()
        if (changeNumber > 0) {
            //parent.addChangeBlock(ChangeBlock.Food)
        }
    }

    override fun ImportFields(food: BFood) {
        //foodEditTextHelper.textBoxs.clear()
        //assignFields(food)
    }

    override fun ExportFields(food: BFood) {
        //assemblyFields(food)
    }

}// Required empty public constructor
