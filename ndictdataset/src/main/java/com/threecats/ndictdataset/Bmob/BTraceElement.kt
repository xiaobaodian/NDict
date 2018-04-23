package com.threecats.ndictdataset.Bmob

import cn.bmob.v3.BmobObject
import com.threecats.ndictdataset.Enum.EMeasure

/**
 * 由 zhang 于 2018/4/21 创建
 */
class BTraceElement: BmobObject() {
    var nutrientID: Int = 0
    var name: String = ""
    var alias: String = ""
    var symbol: String = ""
    var measure: EMeasure = EMeasure.Mg
    var content: _Article? = null
    var demand: MutableList<ProposedDosage> = ArrayList()
}