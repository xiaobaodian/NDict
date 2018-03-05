package com.threecats.ndictdataset.FoodFragment

import android.support.v4.app.Fragment
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Bmob.BFoodCategory
import com.threecats.ndictdataset.Enum.ChangeBlock
import com.threecats.ndictdataset.Enum.EditerState
import com.threecats.ndictdataset.Helper.EditTextHelper
import com.threecats.ndictdataset.View.FoodEditerActivity

/**
 * 由 zhang 于 2018/2/21 创建
 */
abstract class FoodPropertyFragment : Fragment() {

    val shareSet = BDM.ShareSet!!
    val foodEditTextHelper = EditTextHelper()
    var initFieldsFlag = true

    abstract fun BlockChangeState(parent: FoodEditerActivity)
    abstract fun ImportFields(food: BFood)
    abstract fun ExportFields(food: BFood)
    abstract fun FirstEditTextFocus()

}