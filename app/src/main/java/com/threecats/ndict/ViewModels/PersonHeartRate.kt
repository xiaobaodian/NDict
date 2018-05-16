package com.threecats.ndict.ViewModels

import com.threecats.ndict.Enum.EPhysique

/**
 * 由 zhang 于 2018/1/10 创建
 */
class PersonHeartRate(val person: PersonPlus) {

    val max: Int
        get() = 220 - person.age.year

    fun poor(percentage: Float): Int{
        return ((200 - person.age.year) * percentage).toInt()
    }
    fun normal(percentage: Float): Int{
        return ((220 - person.age.year) * percentage).toInt()
    }
    fun strong(percentage: Float): Int{
        return ((220 - person.age.year - person.RHR) * percentage + person.RHR).toInt()
    }
    fun auto(percentage: Float): Int{
        return when (person.physique){
            EPhysique.Poor -> poor(percentage)
            EPhysique.Normal -> normal(percentage)
            EPhysique.Strong -> strong(percentage)
        }
    }

}