package com.threecats.ndictdataset.Bmob

import cn.bmob.v3.BmobObject
import com.threecats.ndictdataset.Enum.EMeasure
import com.threecats.ndictdataset.Models.ProposedDosage

/**
 * 由 zhang 于 2018/4/21 创建
 */

data class BTraceElement(
        var nutrientID: Int = 0,
        var name: String = "",
        var alias: String = "",
        var symbol: String = "",
        var measure: EMeasure = EMeasure.Mg,
        var proposedDosages: MutableList<ProposedDosage> = ArrayList(),
        var content: _Article? = null
): BmobObject()

//class BTraceElement: BmobObject() {
//    var nutrientID: Int = 0
//    var name: String = ""
//    var alias: String = ""
//    var symbol: String = ""
//    var measure: EMeasure = EMeasure.Mg
//    var content: _Article? = null
//    var proposedDosages: MutableList<ProposedDosage> = ArrayList()
//}