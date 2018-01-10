package com.threecats.ndict.Models

import com.threecats.ndict.Enum.EGender
import com.threecats.ndict.Enum.EPhysique
import com.threecats.ndict.Enum.EWorkType
import com.threecats.ndict.Helper.*

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
    val BMI: PersonBMI = PersonBMI(person)
    val BMR: PersonBMR = PersonBMR(this)
    val EHR: PersonHeartRate = PersonHeartRate(this)
    val dailyDemand: PersonDailyDemand = PersonDailyDemand(this)

    inner class Age {

        val year: Int
            get() = ((DateTime().months - person.birthday.months) / 12)
        val month: Int
            get() = (DateTime().months - person.birthday.months) - (year * 12)
        val text: String
            get() = if (year <= 0) "${this.month}个月" else "${this.year}岁"

    }

}