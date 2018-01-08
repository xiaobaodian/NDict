package com.threecats.ndict.Models

import com.threecats.ndict.Enum.EMeasure
import com.threecats.ndict.Enum.EPhysique
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import java.util.*

/**
 * 由 zhang 于 2018/1/8 创建
 */

@Entity
class TraceElement {

    @Id
    var id: Long = 0

    var timestamp: Date = Date()
    var name: String = "TraceElement"

    @Convert(converter = MeasureConverter::class, dbType = Int::class)
    var measure: EMeasure = EMeasure.Gram

    var content: String = ""
    var Function: String = ""
    var source: String = ""
    var demand: String = ""
    var notice: String = ""

    class MeasureConverter: PropertyConverter<EMeasure, Int> {
        override fun convertToEntityProperty(databaseValue: Int): EMeasure {
            return enumValues<EMeasure>()[databaseValue]
        }
        override fun convertToDatabaseValue(entityProperty: EMeasure): Int{
            return entityProperty.code
        }
    }
}