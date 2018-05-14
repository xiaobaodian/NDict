package com.threecats.ndictdataset.FoodFragments

import android.support.v4.app.Fragment
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Helper.EditTextHelper
import com.threecats.ndictdataset.View.FoodEditerActivity

/**
 * 由 zhang 于 2018/2/21 创建
 */

abstract class FoodPropertyFragment : Fragment() {

    val shareSet = BDM.ShareSet!!
    val foodEditTextHelper = EditTextHelper()
    var initFields = true

    abstract fun importFields(food: BFood)
    abstract fun exportFields(food: BFood)
    abstract fun firstEditTextFocus()

}