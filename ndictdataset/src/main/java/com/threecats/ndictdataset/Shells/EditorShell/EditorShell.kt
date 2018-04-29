package com.threecats.ndictdataset.Shells.EditorShell

/**
 * 由 zhang 于 2018/4/29 创建
 */

enum class EEditState{
    Append, Update, Cancel, Delete
}

class EditorShell<I> {

    var currentItem: I? = null
    var editState: EEditState = EEditState.Append

    private var appendItemListener: AppendItemListener<I>? = null
    private var updateItemListener: UpdateItemListener<I>? = null
    private var deleteItemListener: DeleteItemListener<I>? = null
    private var cancelListener: CancelListener<I>? = null

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

    fun append(item: I){
        currentItem = item
        editState = EEditState.Append
    }

    fun edit(item: I){
        currentItem = item
        editState = EEditState.Update
    }

    fun cancel(){
        editState = EEditState.Cancel
    }

    fun delete(){
        editState = EEditState.Delete
    }

    fun commit(){

        currentItem?.let {
            when (editState){
                EEditState.Append -> {appendItemListener?.onAppendItem(it)}
                EEditState.Update -> {updateItemListener?.onUpdateItem(it)}
                EEditState.Delete -> {deleteItemListener?.onDeleteItem(it)}
                EEditState.Cancel -> {cancelListener?.onCancel(it)}
            }
        }

    }

}