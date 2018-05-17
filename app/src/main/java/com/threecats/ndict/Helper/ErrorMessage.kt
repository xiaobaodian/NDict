package com.threecats.ndict.Helper

import android.content.Context
import cn.bmob.v3.exception.BmobException
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

/**
 * 由 zhang 于 2018/3/13 创建
 */
class ErrorMessage(val context: Context, val e: BmobException) {
    fun errorTips(){
        when(e.errorCode){
            9006 -> context.longToast("objectID为空")
            9010 -> context.longToast("网络连接超时")
            9016 -> context.longToast("无网络连接，检查手机设置")
            else -> context.longToast("错误信息：${e.message}，错误代码：${e.errorCode}")
        }
    }
}