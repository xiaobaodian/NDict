package com.threecats.ndictdataset.Models

import com.threecats.ndictdataset.Enum.EGender
import com.threecats.ndictdataset.Shells.RecyclerViewShell.NodeMembership

/**
 * 由 zhang 于 2018/5/4 创建
 */
class DosisGenderGroup(
        var title: String = "所有人",
        private var genderGroup: EGender = EGender.None
): NodeMembership {

    override fun isMembers(item: Any): Boolean {
        return (item as ProposedDosage).gender == genderGroup
    }
}