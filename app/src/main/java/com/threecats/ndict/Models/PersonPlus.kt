package com.threecats.ndict.Models

import com.threecats.ndict.Enum.EGender
import com.threecats.ndict.Enum.EPhysique
import com.threecats.ndict.Enum.EWorkType
import com.threecats.ndict.Helper.DateTime

/**
 * 由 zhang 于 2018/1/8 创建
 */
class PersonPlus(val person: Person) {

    val picture:Int
        get() = person.picture
    val name: String
        get() = person.name
    val birthday: DateTime
        get() = person.birthday
    val gender: EGender
        get() = person.gender
    val height: Float
        get() = person.height
    val weight: Float
        get() = person.weight
    val workType: EWorkType
        get() = person.workType
    val physique: EPhysique
        get() = person.physique
    val RHR: Int
        get() = person.RHR

    val age: Age = Age()
    val BMR: Bmr = Bmr()
    val EHR: HeartRate = HeartRate()
    val dailyDemand: DailyDemand = DailyDemand()

    inner class Age {

        val year: Int
            get() = ((DateTime().months - person.birthday.months) / 12)
        val month: Int
            get() = (DateTime().months - person.birthday.months) - (year * 12)
        val text: String
            get() = if (year <= 0) "${this.month}个月" else "${this.year}岁"

    }

    inner class Bmr {

        val base: Int
            get() = if (person.gender == EGender.male) (66.473 + 13.751 * person.weight + 5.0033 * person.height - 6.7550 * age.year).toInt()
            else (655.0955 + 9.463 * person.weight + 1.8496 * person.height-4.6756 * age.year).toInt()
        val normal: Int
            get() = (base * 1.4).toInt()
        val mild: Int
            get() = if (person.gender == EGender.male) (base*1.58).toInt() else (base*1.56).toInt()
        val medium: Int
            get() = if (person.gender == EGender.male) (base*1.79).toInt() else (base*1.64).toInt()
        val sevete: Int
            get() = if (person.gender == EGender.male) (base*2.1).toInt() else (base*1.82).toInt()
        val auto: Int
            get() = when (person.workType){
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
            return ((220 - age.year - person.RHR) * percentage + person.RHR).toInt()
        }
        fun auto(percentage: Float): Int{
            return when (person.physique){
                EPhysique.poor -> poor(percentage)
                EPhysique.normal -> normal(percentage)
                EPhysique.strong -> strong(percentage)
            }
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