package com.threecats.ndictdataset.Helper

import android.widget.EditText

/**
 * 由 zhang 创建于 2018/2/20.
 */

class CheckTextHelper {
    var textBoxs: MutableList<CheckItem> = ArrayList<CheckItem>()

    fun addEditBox(editBox: EditText){
        textBoxs.add(CheckItem(0, editBox))
    }

    fun initHash(){
        textBoxs.forEach { it.hash = it.editBox.text.toString().hashCode() }
    }

    fun hasChanged(): Int {
        var sum: Int = 0
        textBoxs.forEach { if (it.hash != it.editBox.text.toString().hashCode()) sum++ }
        return sum
    }

    inner class CheckItem(var hash: Int, var editBox: EditText)
}