package com.threecats.ndictdataset.Shells.EditorShell

/**
 * 由 zhang 于 2018/4/29 创建
 * 用于规范对数据对象编辑的操作流程，传入的类型<I>必须是数据对象（data class），否则无法通过toString()后获
 * 取hashcode()来判断对象属性是否发生改变。对象的toString()不能被重写
 */

enum class EEditState{
    Append, Update, Cancel, Delete
}

class EditorShell<I> {

    var item: I? = null

    val isAppend: Boolean
        get() = editState == EEditState.Append
    val isUpdate: Boolean
        get() = editState == EEditState.Update

    private var editState: EEditState = EEditState.Append
    private var initHashCode: Int = 0

    private var appendItemListener: AppendItemListener<I>? = null
    private var updateItemListener: UpdateItemListener<I>? = null
    private var deleteItemListener: DeleteItemListener<I>? = null
    private var cancelListener: CancelListener<I>? = null

    fun append(item: I){
        this.item = item
        initHashCode = getHashCode()
        editState = EEditState.Append
    }

    fun edit(item: I){
        this.item = item
        initHashCode = getHashCode()
        editState = EEditState.Update
    }

    fun cancel(){
        editState = EEditState.Cancel
    }

    fun delete(){
        editState = EEditState.Delete
    }

    fun commit(): EditorShell<I>{

        val newHashCode = getHashCode()

        item?.let {
            when (editState){
                EEditState.Append -> {
                    if (initHashCode == newHashCode) { return this}
                    appendItemListener?.onAppendItem(it)
                }
                EEditState.Update -> {
                    if (initHashCode == newHashCode) { return this}
                    updateItemListener?.onUpdateItem(it)
                }
                EEditState.Delete -> {deleteItemListener?.onDeleteItem(it)}
                EEditState.Cancel -> {cancelListener?.onCancel(it)}
            }
        }
        initHashCode = newHashCode
        return this
    }

    private fun getHashCode() = item.toString().hashCode()

    fun setOnAppendItemListener(listener: AppendItemListener<I>){
        appendItemListener = listener
    }

    fun setOnUpdateItemListener(listener: UpdateItemListener<I>){
        updateItemListener = listener
    }

    fun setOnDeleteItemListener(listener: DeleteItemListener<I>){
        deleteItemListener = listener
    }

    fun setOnCancelListener(listener: CancelListener<I>){
        cancelListener = listener
    }

}