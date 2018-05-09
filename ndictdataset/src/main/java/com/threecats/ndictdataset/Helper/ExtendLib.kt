package com.threecats.ndictdataset.Helper

/**
 * 由 zhang 于 2018/5/9 创建
 */

fun <T> List<T>.next(index: Int): Int{
    var position = index + 1
    if (position >= this.size) position = 0
    return position
}

fun <T> List<T>.previous(index: Int): Int {
    var position = index - 1
    if (position < 0) position = this.size - 1
    return position
}