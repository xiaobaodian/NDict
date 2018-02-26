package com.threecats.ndictdataset.FoodFragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Helper.CheckTextHelper
import org.jetbrains.anko.toast

/**
 * 由 zhang 于 2018/2/21 创建
 */
abstract class FoodPropertyFragment : Fragment() {

    val currentFood = BDM.ShareSet!!.CurrentFood!!
    val checkTextHelper = CheckTextHelper()

    var TabsTitle: String? = null

    abstract fun getFoodFields(): Int
}