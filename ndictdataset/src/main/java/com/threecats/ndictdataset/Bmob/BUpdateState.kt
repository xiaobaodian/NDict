package com.threecats.ndictdataset.Bmob

import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BmobDate
import com.threecats.ndictdataset.Enum.ERecordType
import java.util.*

/**
 * 由 zhang 于 2018/5/12 创建
 */
class BUpdateState(
        var recordType: ERecordType = ERecordType.Category,
        var lastDate: BmobDate = BmobDate(Date())
): BmobObject()