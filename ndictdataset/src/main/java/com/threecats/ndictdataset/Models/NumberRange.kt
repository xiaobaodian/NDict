package com.threecats.ndictdataset.Models

/**
 * 由 zhang 于 2018/5/2 创建
 */
class NumberRange {
    var start: String = ""
    var end: String = ""

    override fun toString(): String{
        return "$start - $end"
    }

    fun put(str: String){
        var strList: List<String> = str.split("-")
        if (strList.isNotEmpty()) start = strList[0].trim()
        if (strList.size > 1) {
            end = strList[1].trim()
            if (end == "") end = "**"
        }

    }
}