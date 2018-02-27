package com.threecats.ndictdataset.FoodFragment

import android.support.v4.app.Fragment
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Helper.EditTextHelper

/**
 * 由 zhang 于 2018/2/21 创建
 */
abstract class FoodPropertyFragment : Fragment() {

    val currentFood = BDM.ShareSet!!.CurrentFood!!
    val checkTextHelper = EditTextHelper()

    var TabsTitle: String? = null

    abstract fun getFoodFields(): Int
}