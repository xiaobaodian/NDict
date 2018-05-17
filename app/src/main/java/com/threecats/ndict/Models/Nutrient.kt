package com.threecats.ndictdataset.Bmob

import cn.bmob.v3.BmobObject
import com.threecats.ndict.Enum.EMeasure
import com.threecats.ndict.Models.ProposedDosage
import com.threecats.ndict.Models.TraceElement
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter

/**
 * 由 zhang 于 2018/3/20 创建
 */

@Entity
data class Nutrient(
        var name: String = "",

        @Convert(converter = MeasureConverter::class, dbType = Int::class)
        var measure: EMeasure = EMeasure.Gram
) {
    @Id var id: Long = 0
    var context: String = ""

    @Backlink
    lateinit var proposedDosages: MutableList<ProposedDosage>

    @Backlink
    lateinit var traceElement: MutableList<TraceElement>

    class MeasureConverter : PropertyConverter<EMeasure, Int> {
        override fun convertToEntityProperty(databaseValue: Int): EMeasure {
            return enumValues<EMeasure>()[databaseValue]
        }
        override fun convertToDatabaseValue(entityProperty: EMeasure): Int {
            return entityProperty.code
        }
    }
}