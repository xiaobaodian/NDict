package com.threecats.ndict.Models

import com.threecats.ndict.Enum.EMeasure
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import java.util.*

/**
 * 由 zhang 于 2018/1/8 创建
 */

@Entity
class TraceElement() {

    @Id
    var id: Long = 0

    var elementID: Long = 0

    var timestamp: Date = Date()
    var name: String = "TraceElement"
    var Symbol: String = ""

    @Convert(converter = MeasureConverter::class, dbType = Int::class)
    var measure: EMeasure = EMeasure.Gram

    var demand: String = ""

    var content: String = ""
    var function: String = ""
    var source: String = ""
    var notice: String = ""

    constructor(
            elementID: Long,
            name: String,
            elementSymbol: String,
            measure: EMeasure,
            demand: String,
            content: String,
            function: String,
            source: String,
            notice: String
    ) : this() {
        this.elementID = elementID
        this.name = name
        this.Symbol = elementSymbol
        this.measure = measure
        this.demand = demand
        this.content = content
        this.function = function
        this.source = source
        this.notice = notice
    }

    class MeasureConverter : PropertyConverter<EMeasure, Int> {
        override fun convertToEntityProperty(databaseValue: Int): EMeasure {
            return enumValues<EMeasure>()[databaseValue]
        }

        override fun convertToDatabaseValue(entityProperty: EMeasure): Int {
            return entityProperty.code
        }
    }
}