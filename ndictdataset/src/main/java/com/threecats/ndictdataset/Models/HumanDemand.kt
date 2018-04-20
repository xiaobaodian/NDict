package com.threecats.ndictdataset.Models

import com.threecats.ndictdataset.Enum.EGender
import com.threecats.ndictdataset.Enum.EMeasure

/**
 * 由 zhang 于 2018/3/17 创建
 */
class HumanDemand {
    var gender: EGender = EGender.Male
    var ageBegin: Float = 0f
    var ageEnd: Float = 0f
    var demandBegin: Float = 0f
    var demandEnd: Float = 0f
    var measure: EMeasure = EMeasure.Mg
}