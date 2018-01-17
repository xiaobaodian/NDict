package com.threecats.ndict.OriginalData

import com.threecats.ndict.Enum.EGender
import com.threecats.ndict.Helper.DateTime
import com.threecats.ndict.Models.Person

/**
 * 由 zhang 于 2018/1/11 创建
 */
object InitPerson {

    fun createPerson(): List<Person>{
        var persons: List<Person> = listOf(
                Person("老爷", EGender.male, DateTime(1969,3,12),164f,70f),
                Person("跟班", EGender.female, DateTime(1973,9,20),164f,65f),
                Person("小宝点", EGender.male, DateTime(2015,6,1),87f,15.5f)
        )
        return persons
    }
}