package com.threecats.ndictdataset.Shells.RecyclerViewShell

/**
 * 由 zhang 于 2018/3/29 创建
 */
class RecyclerViewViewType(itemType: ItemType) {
    var title: String = ""
    var itemType: ItemType
    var layoutID: Int? = null

    init {
        this.itemType =itemType
    }

    constructor(title: String, itemType: ItemType, layoutId: Int): this(itemType){
        this.title = title
        this.layoutID = layoutId
    }

    private var site: Int = -1
    fun indexAt(viewTypes: List<RecyclerViewViewType>): Int{
        if (site < 0) {
            site = viewTypes.indexOf(this)
        }
        return site
    }
}