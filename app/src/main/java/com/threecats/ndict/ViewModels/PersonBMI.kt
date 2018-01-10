package com.threecats.ndict.ViewModels

import com.threecats.ndict.Models.Person

/**
 * 由 zhang 于 2018/1/10 创建
 */
class PersonBMI(val person: Person) {

    enum class EArea {
        China, WHO, Singapore, Japan
    }

    //保留一位小数
    val valus: Float
        get() {
            var bmiF: Float = person.weight / ((person.height / 100) * (person.height / 100))
            var bmiX: Int = (bmiF * 10).toInt()
            return (bmiX / 10).toFloat()
        }

    fun type(area: EArea): String {
        var mType = ""
        val bmiX: Int = (valus * 10).toInt()
        when (area) {
            EArea.China -> {
                when (bmiX) {
                    in 0..184 -> mType = "偏瘦"
                    in 185..239 -> mType = "正常"
                    in 240..279 -> mType = "过重"
                    in 280..1000 -> mType = "肥胖"
                }
            }
            EArea.WHO -> {
                when (bmiX) {
                    in 0..164 -> mType = "极瘦"
                    in 165..184 -> mType = "偏瘦"
                    in 185..249 -> mType = "正常"
                    in 250..299 -> mType = "过重"
                    in 300..349 -> mType = "I类肥胖"
                    in 350..390 -> mType = "II类肥胖"
                    in 400..1000 -> mType = "III类肥胖"
                }
            }
            EArea.Singapore -> {
                when (bmiX) {
                    in 0..149 -> mType = "极瘦"
                    in 150..184 -> mType = "偏瘦"
                    in 185..229 -> mType = "正常"
                    in 230..275 -> mType = "过重"
                    in 276..400 -> mType = "肥胖"
                    in 401..1000 -> mType = "非常肥胖"
                }
            }
            EArea.Japan -> {
                when (bmiX) {
                    in 0..184 -> mType = "偏瘦"
                    in 185..229 -> mType = "正常"
                    in 230..249 -> mType = "过重"
                    in 250..1000 -> mType = "肥胖"
                }
            }
        }
        return mType
    }
}