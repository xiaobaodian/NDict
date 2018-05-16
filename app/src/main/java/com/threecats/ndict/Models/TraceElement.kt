package com.threecats.ndict.Models

import com.threecats.ndict.Enum.EMeasure
import com.threecats.ndictdataset.Bmob._Article
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import java.util.*

/**
 * 由 zhang 于 2018/1/8 创建
 */

@Entity
data class TraceElement(
        @Id var id: Long = 0,

        var elementID: Long = 0,

        var name: String = "TraceElement",
        var alias: String = "",
        var Symbol: String = "",

        @Convert(converter = MeasureConverter::class, dbType = Int::class)
        var measure: EMeasure = EMeasure.Gram,

        var demand: String = "",

        var content: String = "",

        var nutrientID: Int = 0,

        var proposedDosages: MutableList<ProposedDosage> = ArrayList()

) {

    var bmobId: String = ""
    var bmobUpdateAt: String = ""

    class MeasureConverter : PropertyConverter<EMeasure, Int> {
        override fun convertToEntityProperty(databaseValue: Int): EMeasure {
            return enumValues<EMeasure>()[databaseValue]
        }

        override fun convertToDatabaseValue(entityProperty: EMeasure): Int {
            return entityProperty.code
        }
    }
}