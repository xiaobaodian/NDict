package com.threecats.ndict.ViewModels

import com.threecats.ndict.ViewModels.PersonPlus

/**
 * 由 zhang 于 2018/1/10 创建
 */
class PersonDailyDemand(val person: PersonPlus) {

    val protein: Int
        get() = (person.BMR.auto * 0.12).toInt()
    val fat: Int
        get() = (person.BMR.auto * 0.25).toInt()
    val carbohydrate: Int
        get() = (person.BMR.auto * 0.63).toInt()

}