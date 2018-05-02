package com.threecats.ndictdataset.Bmob

import cn.bmob.v3.BmobObject
import com.threecats.ndictdataset.Models.ProposedDosage

/**
 * 由 zhang 于 2018/3/20 创建
 */

class BNutrient: BmobObject() {
    var nutrientID: Int = 0
    var name = ""
    var context: _Article? = null
    val proposedDosages: MutableList<ProposedDosage> = ArrayList()
}