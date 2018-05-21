package com.threecats.ndict.Models

import android.annotation.SuppressLint
import android.content.Context
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.threecats.ndict.Enum.ERecordType
import com.threecats.ndict.Helper.DateTime
import com.threecats.ndict.Helper.ErrorMessage
import com.threecats.ndictdataset.Bmob.BUpdateState
import java.text.SimpleDateFormat
import java.util.*

/**
 * 由 zhang 于 2018/5/21 创建
 */
class UpdateDatas(val context: Context) {
    private var updateStates: MutableList<BUpdateState> = ArrayList()
    private var lastUpdateStates: MutableList<UpdateDate> = ArrayList()

    init {
        val query: BmobQuery<BUpdateState> = BmobQuery()
        query.findObjects(object: FindListener<BUpdateState>(){
            @SuppressLint("SimpleDateFormat")
            override fun done(state: MutableList<BUpdateState>?, e: BmobException?) {
                if (e == null) {
                    val dateMark = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    state?.forEach {
                        val lastDate = Calendar.getInstance()
                        lastDate.time = dateMark.parse(it.lastDate.date)
                        lastUpdateStates.add(UpdateDate(recordType = it.recordType, bmobLastDate = lastDate))
                    }
                } else {
                    ErrorMessage(context, e).errorTips()
                }
            }
        })
    }

    data class UpdateDate(
            val recordType: ERecordType = ERecordType.Category,
            var bmobLastDate: Calendar = Calendar.getInstance(),
            var localLastDate: Calendar = Calendar.getInstance()
    )
}