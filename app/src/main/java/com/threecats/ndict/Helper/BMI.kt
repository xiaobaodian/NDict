package com.threecats.ndict.Helper

import com.threecats.ndict.Models.Person

/**
 * 由 zhang 于 2018/1/10 创建
 */
class CBMI(val person: Person) {

    enum class EArea{
        China, WHO, Singapore,Japan
    }

    //保留一位小数
    val valus: Float
        get() {
            var bmiF: Float = person.weight/((person.height/100)*(person.height/100))
            var bmiX: Int = (bmiF * 10).toInt()
            return (bmiX / 10).toFloat()
        }

    fun type(area: EArea): String{
        var t: String = ""
        val bmiX: Int = (valus * 10).toInt()
        when (area) {
            EArea.China -> {
                when (bmiX){
                    in 0..184 -> t="偏瘦"
                    in 185..239 -> t="正常"
                    in 240..279 -> t="过重"
                    in 280..1000 -> t="肥胖"
                }
            }
            EArea.WHO -> {
                when (bmiX){
                    in 0..164 -> t="极瘦"
                    in 165..184 -> t="偏瘦"
                    in 185..249 -> t="正常"
                    in 250..299 -> t="过重"
                    in 300..349 -> t="I类肥胖"
                    in 350..390 -> t="II类肥胖"
                    in 400..1000 -> t="III类肥胖"
                }
            }
            EArea.Singapore -> {
                when (bmiX){
                    in 0..149 -> t="极瘦"
                    in 150..184 -> t="偏瘦"
                    in 185..229 -> t="正常"
                    in 230..275 -> t="过重"
                    in 276..400 -> t="肥胖"
                    in 401..1000 -> t="非常肥胖"
                }
            }
            EArea.Japan -> {
                when (bmiX){
                    in 0..184 -> t="偏瘦"
                    in 185..229 -> t="正常"
                    in 230..249 -> t="过重"
                    in 250..1000 -> t="肥胖"
                }
            }
        }
        return t
    }
}