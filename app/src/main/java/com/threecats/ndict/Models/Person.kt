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
class Person() {

    @Id
    var id:Long=0

    var picture:Int = 0
    var name: String = ""

    @Convert(converter = DateConverter::class, dbType = Date::class)
    var birthday: DateTime = DateTime()

    @Convert(converter = GenderConverter::class, dbType = Int::class)
    var gender: EGender = EGender.male

    var height: Float = 0.0f
    var weight: Float = 0.0f

    @Convert(converter = WorkTypeConverter::class, dbType = Int::class)
    var workType: EWorkType = EWorkType.normal

    @Convert(converter = PhysiqueConverter::class, dbType = Int::class)
    var physique: EPhysique = EPhysique.normal

    var RHR: Int = 60

    @Transient
    val EHR: HeartRate = HeartRate()
    @Transient
    val age: Age = Age()
    @Transient
    val BMR: Bmr = Bmr()
    @Transient
    val dailyDemand: DailyDemand = DailyDemand()

    constructor(
        name: String,
        gender: EGender,
        birthday: DateTime,
        height: Float,
        weight: Float
    ): this() {
        this.name = name
        this.gender = gender
        this.birthday = birthday
        this.height = height
        this.weight = weight
    }

    class GenderConverter: PropertyConverter<EGender, Int> {
        override fun convertToEntityProperty(databaseValue: Int): EGender {
            val gender = enumValues<EGender>()
            return gender[databaseValue]
        }
        override fun convertToDatabaseValue(entityProperty: EGender): Int{
            return entityProperty.sex
        }
    }

    class WorkTypeConverter: PropertyConverter<EWorkType, Int> {
        override fun convertToEntityProperty(databaseValue: Int): EWorkType {
            val workType = enumValues<EWorkType>()
            return workType[databaseValue]
        }
        override fun convertToDatabaseValue(entityProperty: EWorkType): Int{
            return entityProperty.type
        }
    }

    class DateConverter: PropertyConverter<DateTime, Date> {
        override fun convertToEntityProperty(databaseValue: Date): DateTime {
            return DateTime(databaseValue)
        }
        override fun convertToDatabaseValue(entityProperty:DateTime): Date{
            return entityProperty.time
        }
    }

    class PhysiqueConverter: PropertyConverter<EPhysique, Int> {
        override fun convertToEntityProperty(databaseValue: Int): EPhysique {
            val physique = enumValues<EPhysique>()
            return physique[databaseValue]
        }
        override fun convertToDatabaseValue(entityProperty: EPhysique): Int{
            return entityProperty.type
        }
    }

    inner class Age {
        val year: Int
            get() = ((DateTime().months - birthday.months) / 12)
        val month: Int
            get() = (DateTime().months - birthday.months) - (year * 12)
        val text: String
            get() = if (year <= 0) "${this.month}个月" else "${this.year}岁"
    }

    inner class Bmr {
        val base: Int
            get() = if (gender == EGender.male) (66.473 + 13.751 * weight + 5.0033 * height - 6.7550 * age.year).toInt()
                    else (655.0955 + 9.463 * weight + 1.8496 * height-4.6756 * age.year).toInt()
        val normal: Int
            get() = (base * 1.4).toInt()
        val mild: Int
            get() = if (gender == EGender.male) (base*1.58).toInt() else (base*1.56).toInt()
        val medium: Int
            get() = if (gender == EGender.male) (base*1.79).toInt() else (base*1.64).toInt()
        val sevete: Int
            get() = if (gender == EGender.male) (base*2.1).toInt() else (base*1.82).toInt()
        val auto: Int
            get() = when (workType){
                EWorkType.normal -> normal
                EWorkType.mild -> mild
                EWorkType.medium -> medium
                EWorkType.sevete -> sevete
            }
    }

    inner class HeartRate {
        val max: Int
            get() = 220 - age.year

        fun poor(percentage: Float): Int{
            return ((200 - age.year) * percentage).toInt()
        }

        fun normal(percentage: Float): Int{
            return ((220 - age.year) * percentage).toInt()
        }

        fun strong(percentage: Float): Int{
            return ((220 - age.year - RHR) * percentage + RHR).toInt()
        }
    }

    inner class DailyDemand {
        val protein: Int
            get() = (BMR.auto * 0.12).toInt()
        val fat: Int
            get() = (BMR.auto * 0.25).toInt()
        val carbohydrate: Int
            get() = (BMR.auto * 0.63).toInt()
    }

}