package com.threecats.ndictdataset.Shells.EditorShell

/**
 * 由 zhang 创建于 2018/3/28.
 */
interface AppendItemListener<I>{
    fun onAppendItem(item: I)
}

interface UpdateItemListener<I>{
    fun onUpdateItem(item: I)
}

interface CancelListener<I>{
    fun onCancel(item: I)
}

interface DeleteItemListener<I>{
    fun onDeleteItem(item: I)
}