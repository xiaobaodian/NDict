package com.threecats.ndict.Models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * 由 zhang 创建于 2018/5/16.
 */

@Entity
data class Vitamin(
        @Id var id: Long = 0,
        var RE:                 Float = 0f,
        var VitaminB1:          Float = 0f,
        var VitaminB2:          Float = 0f,
        var Niacin:             Float = 0f,
        var VitaminB6:          Float = 0f,
        var PantothenicAcid:    Float = 0f,
        var VitaminH:           Float = 0f,
        var FolicAcid:          Float = 0f,
        var VitaminB12:         Float = 0f,
        var Choline:            Float = 0f,
        var VitaminC:           Float = 0f,
        var VitaminD:           Float = 0f,
        var VitaminE:           Float = 0f,
        var VitaminK:           Float = 0f,
        var VitaminP:           Float = 0f,
        var Inositol:           Float = 0f,
        var PABA:               Float = 0f
) {
}