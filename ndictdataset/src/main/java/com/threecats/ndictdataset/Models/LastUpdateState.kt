package com.threecats.ndictdataset.Models

import android.content.Context
import cn.bmob.v3.BmobBatch
import cn.bmob.v3.BmobObject
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BatchResult
import cn.bmob.v3.datatype.BmobDate
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListListener
import com.threecats.ndictdataset.Bmob.BUpdateState
import com.threecats.ndictdataset.Enum.ERecordType
import com.threecats.ndictdataset.Helper.ErrorMessage
import org.jetbrains.anko.toast
import java.util.*

/**
 * 由 zhang 于 2018/5/12 创建
 */
class LastUpdateState(val context: Context) {
    private var states: MutableList<BUpdateState> = ArrayList()
    private val checkHash = arrayOfNulls<Int>(4)


    init{
        val query: BmobQuery<BUpdateState> = BmobQuery()
        //query.addWhereEqualTo("category", BmobPointer(currentCategory))
        //query.include("vitamin,mineral,mineralExt,article")
        //query.setLimit(300)
        query.findObjects(object: FindListener<BUpdateState>(){
            override fun done(state: MutableList<BUpdateState>?, e: BmobException?) {
                if (e == null) {
                    state?.let { states = it }
                    check()
                    checkHash[0] = states[0].toString().hashCode()
                    checkHash[1] = states[1].toString().hashCode()
                    checkHash[2] = states[2].toString().hashCode()
                    checkHash[3] = states[3].toString().hashCode()
                } else {
                    ErrorMessage(context, e).errorTips()
                }
            }
        })
    }

    private fun check(){
        if (states.isEmpty()) {
            states.add(BUpdateState(ERecordType.Category))
            states.add(BUpdateState(ERecordType.Food))
            states.add(BUpdateState(ERecordType.Nutrient))
            states.add(BUpdateState(ERecordType.TraceElement))

            val batchStates: MutableList<BmobObject> = ArrayList()
            states.forEach { batchStates.add(it) }
            BmobBatch().insertBatch(batchStates).doBatch(object: QueryListListener<BatchResult>(){
                override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                    if (e == null) {
                        context.toast("初始化了最后更新时间列表")
                    } else {
                        ErrorMessage(context, e)
                    }
                }
            })
        }
    }

    private fun find(type: ERecordType): BUpdateState? {
        var result: BUpdateState? = null
        states.forEach { if (it.recordType == type) result = it}
        return result
    }

    fun changeDate(type: ERecordType){
        val state = find(type)
        state?.lastDate = BmobDate(Date())
    }

    fun commit(){
        val batchStates: MutableList<BmobObject> = ArrayList()
        for(i in states.indices){
            if (states[i].toString().hashCode() != checkHash[i]) batchStates.add(states[i])
        }
        if (batchStates.isEmpty()) return
        BmobBatch().updateBatch(batchStates).doBatch(object: QueryListListener<BatchResult>(){
            override fun done(results: MutableList<BatchResult>?, e: BmobException?) {
                if (e == null) {
                    //context.toast("更新了数据变更时间列表")
                } else {
                    ErrorMessage(context, e).errorTips()
                }
            }
        })
    }
}