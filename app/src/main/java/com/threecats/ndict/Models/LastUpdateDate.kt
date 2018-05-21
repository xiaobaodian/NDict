package com.threecats.ndict.Models

import cn.bmob.v3.datatype.BmobDate
import com.threecats.ndict.Enum.ERecordType
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.*

/**
 * 由 zhang 于 2018/5/21 创建
 */

@Entity
data class LastUpdateDate(
        var recordType: ERecordType = ERecordType.Category,
        var lastDate: BmobDate = BmobDate(Date())
) {
    @Id var ID: Long = 0
}