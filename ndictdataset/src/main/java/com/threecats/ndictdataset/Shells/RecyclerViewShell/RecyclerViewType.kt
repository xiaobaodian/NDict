package com.threecats.ndictdataset.Shells.RecyclerViewShell

/**
 * 由 zhang 于 2018/3/29 创建
 */
class RecyclerViewType(itemType: ItemType) {
    var ID: String = ""
    var itemType: ItemType
    var layoutID: Int? = null

    init {
        this.itemType =itemType
    }

    constructor(title: String, itemType: ItemType, layoutId: Int): this(itemType){
        this.ID = title
        this.layoutID = layoutId
    }

    fun title(title: String): RecyclerViewType{
        this.ID = title
        return this
    }

    fun layout(layout: Int): RecyclerViewType{
        this.layoutID = layout
        return this
    }

    private var site: Int = -1
    fun indexAt(viewTypes: List<RecyclerViewType>): Int{
        if (site < 0) {
            site = viewTypes.indexOf(this)
        }
        return site
    }
}