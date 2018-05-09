package com.threecats.ndictdataset.Models

import com.threecats.ndictdataset.Enum.EGender
import com.threecats.ndictdataset.Enum.EMeasure
import com.threecats.ndictdataset.Enum.EPregnancy

/**
 * 由 zhang 于 2018/4/20 创建
 */
data class ProposedDosage(
        var ageRange: String = "",
        var dosisRange: String = "",
        var measure: EMeasure = EMeasure.Mg,
        var gender: EGender = EGender.None,
        var pregnancy: EPregnancy = EPregnancy.None
)