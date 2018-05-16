package com.threecats.ndict.Models

import com.threecats.ndict.Enum.EMeasure
import com.threecats.ndict.Enum.EGender
import com.threecats.ndict.Enum.EPregnancy

/**
 * 由 zhang 于 2018/4/20 创建
 */
data class ProposedDosage(
        var ageRange: String = "",
        var dosisRange: String = "",
        var measure: EMeasure = EMeasure.Mg,
        var gender: EGender = EGender.None,
        var pregnancy: EPregnancy = EPregnancy.None
) {
    var bmobId: String = ""
    var bmobUpdateAt: String = ""
}