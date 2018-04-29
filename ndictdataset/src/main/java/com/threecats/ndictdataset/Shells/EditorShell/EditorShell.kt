package com.threecats.ndictdataset.Shells.EditorShell

/**
 * 由 zhang 于 2018/4/29 创建
 */

enum class EEditState{
    Append, Editor, Cancel
}

class EditorShell<I> {

    var currentItem: I? = null
    var editState: EEditState = EEditState.Append

    fun append(item: I){
        currentItem = item
        editState = EEditState.Append
    }

    fun edit(item: I){
        currentItem = item
        editState = EEditState.Editor
    }

    fun cancel(){
        editState = EEditState.Cancel
    }

    fun commit(){

    }

}