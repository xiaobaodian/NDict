package com.threecats.ndictdataset.Helper

import android.widget.EditText

/**
 * 由 zhang 创建于 2018/2/20.
 */

class EditTextHelper {
    var textBoxs: MutableList<EditItem> = ArrayList<EditItem>()

    fun addEditBox(editBox: EditText){
        textBoxs.add(EditItem(0, editBox))
    }

    fun addEditBox(editBox: EditText, text: String){
        textBoxs.add(EditItem(0, editBox))
        editBox.text.append(text)
    }

    fun initHash(){
        textBoxs.forEach { it.hash = it.editBox.text.toString().hashCode() }
    }

    fun ChangeNumber(): Int {
        var sum: Int = 0
        textBoxs.forEach { if (it.hash != it.editBox.text.toString().hashCode()) sum++ }
        return sum
    }

    fun CheckNull(){
        textBoxs.forEach { if (it.editBox.text.isEmpty()) it.editBox.text.append("0.0") }
    }

    inner class EditItem(var hash: Int, var editBox: EditText)
}