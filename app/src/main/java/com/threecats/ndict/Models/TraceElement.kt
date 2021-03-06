package com.threecats.ndict.Models

import com.threecats.ndict.Enum.EMeasure
import com.threecats.ndictdataset.Bmob.Nutrient
import com.threecats.ndictdataset.Bmob._Article
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import io.objectbox.relation.ToOne
import java.util.*

/**
 * 由 zhang 于 2018/1/8 创建
 */

@Entity
data class TraceElement(

        var name: String = "TraceElement",
        var alias: String = "",
        var Symbol: String = "",

        @Convert(converter = MeasureConverter::class, dbType = Int::class)
        var measure: EMeasure = EMeasure.Gram

) {
    @Id var id: Long = 0
    var content: String = ""

    var bmobId: String = ""
    var bmobUpdateAt: String = ""

    lateinit var nutrient: ToOne<Nutrient>

    @Backlink
    lateinit var proposedDosages: MutableList<ProposedDosage>
    //var proposedDosages: MutableList<ProposedDosage> = ArrayList()

    class MeasureConverter : PropertyConverter<EMeasure, Int> {
        override fun convertToEntityProperty(databaseValue: Int): EMeasure {
            return enumValues<EMeasure>()[databaseValue]
        }
        override fun convertToDatabaseValue(entityProperty: EMeasure): Int {
            return entityProperty.code
        }
    }
}