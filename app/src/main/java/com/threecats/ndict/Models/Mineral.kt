package com.threecats.ndict.Models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index

/**
 * 由 zhang 于 2018/2/2 创建
 */

@Entity
data class Mineral(

        @Id var id: Long = 0,
        @Index var foodID: Long = 0,

        var K:  Float = 0f,
        var N:  Float = 0f,
        var Ca: Float = 0f,
        var Mg: Float = 0f,
        var Fe: Float = 0f,
        var Mn: Float = 0f,
        var Zn: Float = 0f,
        var Cu: Float = 0f,
        var Se: Float = 0f,
        var P:  Float = 0f,
        var I:  Float = 0f,
        var Mo: Float = 0f,
        var Cr: Float = 0f,
        var Ce: Float = 0f,
        var Co: Float = 0f,
        var Sn: Float = 0f,
        var Ni: Float = 0f,
        var V:  Float = 0f,
        var Si: Float = 0f,
        var Ci: Float = 0f,
        var S:  Float = 0f
) {

}