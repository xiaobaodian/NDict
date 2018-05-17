package com.threecats.ndict.Models

import com.threecats.ndict.Enum.EGender
import com.threecats.ndict.Enum.EPhysique
import com.threecats.ndict.Enum.EWorkType
import com.threecats.ndict.Helper.DateTime
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import java.util.*

/**
 * 由 zhang 于 2018/1/6 创建
 */

@Entity
class Person(
        var name: String = "",

        @Convert(converter = GenderConverter::class, dbType = Int::class)
        var gender: EGender = EGender.Male,

        @Convert(converter = DateConverter::class, dbType = Date::class)
        var birthday: DateTime = DateTime(),

        var height: Float = 0.0f,
        var weight: Float = 0.0f

) {
    @Id var id: Long = 0

    @Convert(converter = WorkTypeConverter::class, dbType = Int::class)
    var workType: EWorkType = EWorkType.Normal

    @Convert(converter = PhysiqueConverter::class, dbType = Int::class)
    var physique: EPhysique = EPhysique.Normal

    var RHR: Int = 60
    var pregnant: Boolean = false
        set(value) = when (gender) {
            EGender.Female -> field = value
            else -> field = false
        }
        get() = if (gender == EGender.Female) field else false
    var nursing: Boolean = false
        set(value) = when (gender) {
            EGender.Female -> field = value
            else -> field = false
        }
        get() = if (gender == EGender.Female) field else false

    var bmobId: String = ""
    var bmobUpdateAt: String = ""

    class GenderConverter : PropertyConverter<EGender, Int> {
        override fun convertToEntityProperty(databaseValue: Int): EGender {
            val gender = enumValues<EGender>()
            return gender[databaseValue]
        }

        override fun convertToDatabaseValue(entityProperty: EGender): Int {
            return entityProperty.sex
        }
    }

    class WorkTypeConverter : PropertyConverter<EWorkType, Int> {
        override fun convertToEntityProperty(databaseValue: Int): EWorkType {
            val workType = enumValues<EWorkType>()
            return workType[databaseValue]
        }

        override fun convertToDatabaseValue(entityProperty: EWorkType): Int {
            return entityProperty.type
        }
    }

    class DateConverter : PropertyConverter<DateTime, Date> {
        override fun convertToEntityProperty(databaseValue: Date): DateTime {
            return DateTime(databaseValue)
        }

        override fun convertToDatabaseValue(entityProperty: DateTime): Date {
            return entityProperty.time
        }
    }

    class PhysiqueConverter : PropertyConverter<EPhysique, Int> {
        override fun convertToEntityProperty(databaseValue: Int): EPhysique {
            val physique = enumValues<EPhysique>()
            return physique[databaseValue]
        }

        override fun convertToDatabaseValue(entityProperty: EPhysique): Int {
            return entityProperty.type
        }
    }

}