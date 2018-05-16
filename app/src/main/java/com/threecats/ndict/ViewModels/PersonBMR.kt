package com.threecats.ndict.ViewModels

import com.threecats.ndict.Enum.EGender
import com.threecats.ndict.Enum.EWorkType

/**
 * 由 zhang 于 2018/1/10 创建
 */
class PersonBMR(val person: PersonPlus) {

    val base: Int
        get() = if (person.gender == EGender.male)
                    (66.473 + 13.751 * person.weight + 5.0033 * person.height - 6.7550 * person.age.year).toInt()
                else
                    (655.0955 + 9.463 * person.weight + 1.8496 * person.height-4.6756 * person.age.year).toInt()
    val live: Int
        get() = (base * 1.25).toInt()
    val normal: Int
        get() = if (person.gender == EGender.male) (base*1.48).toInt() else (base*1.375).toInt()
    val mild: Int
        get() = if (person.gender == EGender.male) (base*1.58).toInt() else (base*1.56).toInt()
    val medium: Int
        get() = if (person.gender == EGender.male) (base*1.79).toInt() else (base*1.64).toInt()
    val sevete: Int
        get() = if (person.gender == EGender.male) (base*2.1).toInt() else (base*1.82).toInt()
    val auto: Int
        get() = when (person.workType){
            EWorkType.Live -> live
            EWorkType.Normal -> normal
            EWorkType.Mild -> mild
            EWorkType.Medium -> medium
            EWorkType.Sevete -> sevete
        }
}