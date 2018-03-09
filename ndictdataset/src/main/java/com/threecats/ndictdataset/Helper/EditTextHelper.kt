package com.threecats.ndictdataset.Helper

import android.widget.EditText
import com.threecats.ndictdataset.Bmob.BFood
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

/**
 * 由 zhang 创建于 2018/2/20.
 */

class EditTextHelper {
    var textBoxs: MutableList<EditItem> = ArrayList<EditItem>()

    fun addEditBox(editBox: EditText){
        //var v: KProperty<MutableList<EditItem>> = EditTextHelper::textBoxs

        textBoxs.add(EditItem(0, editBox))
    }

    fun addEditBox(editBox: EditText, text: String){
        if (editBox == null) return
        textBoxs.add(EditItem(0, editBox))
        editBox.text.clear()
        editBox.text.append(text)
        editBox.setSelectAllOnFocus(true)
    }

    fun clear(){
        textBoxs.forEach { it.editBox.text.clear() }
    }

    fun initHash(){
        textBoxs.forEach { it.hash = it.editBox.text.toString().hashCode() }
    }

    fun ChangeNumber(): Int {
        var sum: Int = 0
        textBoxs.forEach { if (it.hash != it.editBox.text.toString().hashCode()) sum++ }
        return sum
    }

    fun CheckNull(initStr: String){
        textBoxs.forEach { if (it.editBox.text.isEmpty()) it.editBox.text.append(initStr) }
    }

    inner class EditItem(var hash: Int, var editBox: EditText)  //, var foodField: KMutableProperty<R>
}