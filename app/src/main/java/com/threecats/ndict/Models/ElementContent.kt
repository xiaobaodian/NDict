package com.threecats.ndict.Models

import io.objectbox.annotation.Entity

/**
 * 由 zhang 于 2018/4/16 创建
 */

class ElementContent(symbol: String) {
    var symbol: String = ""
    var content: Float = 0.0f

    init {
        this.symbol = symbol
    }
}