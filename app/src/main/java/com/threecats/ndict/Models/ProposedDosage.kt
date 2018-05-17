package com.threecats.ndict.Models

import com.threecats.ndict.Enum.EMeasure
import com.threecats.ndict.Enum.EGender
import com.threecats.ndict.Enum.EPregnancy
import com.threecats.ndictdataset.Bmob.Nutrient
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import io.objectbox.relation.ToOne

/**
 * 由 zhang 于 2018/4/20 创建
 */

@Entity
data class ProposedDosage(

        var ageRange: String = "",
        var dosisRange: String = "",

        @Convert(converter = MeasureConverter::class, dbType = Int::class)
        var measure: EMeasure = EMeasure.Mg,

        @Convert(converter = GenderConverter::class, dbType = Int::class)
        var gender: EGender = EGender.None,

        @Convert(converter = PregnancyConverter::class, dbType = Int::class)
        var pregnancy: EPregnancy = EPregnancy.None
) {
    @Id var id: Long = 0
    var bmobId: String = ""
    var bmobUpdateAt: String = ""

    lateinit var traceElement: ToOne<TraceElement>
    lateinit var nutrient: ToOne<Nutrient>

    class GenderConverter : PropertyConverter<EGender, Int> {
        override fun convertToEntityProperty(databaseValue: Int): EGender {
            val gender = enumValues<EGender>()
            return gender[databaseValue]
        }

        override fun convertToDatabaseValue(entityProperty: EGender): Int {
            return entityProperty.sex
        }
    }

    class PregnancyConverter : PropertyConverter<EPregnancy, Int> {
        override fun convertToEntityProperty(databaseValue: Int): EPregnancy {
            val pregnancy = enumValues<EPregnancy>()
            return pregnancy[databaseValue]
        }

        override fun convertToDatabaseValue(entityProperty: EPregnancy): Int {
            return entityProperty.stage
        }
    }

    class MeasureConverter : PropertyConverter<EMeasure, Int> {
        override fun convertToEntityProperty(databaseValue: Int): EMeasure {
            val measure = enumValues<EMeasure>()
            return measure[databaseValue]
        }

        override fun convertToDatabaseValue(entityProperty: EMeasure): Int {
            return entityProperty.code
        }
    }
}