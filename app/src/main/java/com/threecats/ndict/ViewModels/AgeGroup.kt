package com.threecats.ndict.ViewModels

/**
 * 由 zhang 创建于 2018/1/10.
 */
class AgeGroup(val person: PersonPlus) {
    val name: String
    get() {
        var text =""
        when (person.age.year){
            0 -> text = "婴儿"
            in 1..7 -> text = "儿童"
            in 8..14 -> text = "少年"
            in 15..44 -> text = "青年"
            in 45..59 -> text ="中年"
            in 60..74 -> text="初老"
            in 75..89 -> text="老年人"
            in 90..900 -> text="长寿老人"
        }
        return text
    }

}