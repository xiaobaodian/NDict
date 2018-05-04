package com.threecats.ndictdataset.Models

import com.threecats.ndictdataset.Enum.EGender
import com.threecats.ndictdataset.Shells.RecyclerViewShell.GroupMembership

/**
 * 由 zhang 于 2018/5/4 创建
 */
class DosisGenderGroup(
        var title: String = "",
        val genderGroup: EGender = EGender.None
): GroupMembership {

    override fun isMembers(item: Any): Boolean {
        return genderGroup == (item as ProposedDosage).gender
    }
}