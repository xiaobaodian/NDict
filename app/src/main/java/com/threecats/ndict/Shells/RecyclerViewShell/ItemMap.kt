package com.threecats.ndict.Shells.RecyclerViewShell

/**
 * 由 zhang 于 2018/5/10 创建
 */
class ItemMap<K, V> {
    private val mapItem: MutableList<Pair<K, V>> = ArrayList()

    private fun find(key: K): Result{
        var position = -1
        var value: V? = null
        for (i in mapItem.indices) {
            val (itKey, itValue) = mapItem[i]
            if (key == itKey) {
                position = i
                value = itValue
                break
            }
        }
        return Result(position, value, position == -1, position != -1)
    }

    fun put(key: K, value: V){
        if (find(key).notFind) {
            mapItem.add(Pair(key, value))
        }
    }

    fun get(key: K): V? {
        return find(key).value
    }

    fun remove(key: K): Boolean{
        val result = find(key)
        if (result.isFind) {
            mapItem.removeAt(result.position)
        }
        return result.isFind
    }

    inner class Result(val position: Int, val value: V?, val notFind: Boolean, val isFind: Boolean)
}