package com.threecats.ndict.Models

import cn.bmob.v3.datatype.BmobDate
import com.threecats.ndict.Enum.EMeasure
import com.threecats.ndict.Enum.ERecordType
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import java.util.*

/**
 * 由 zhang 于 2018/5/21 创建
 */

@Entity
data class LastUpdateDate(
        @Convert(converter = RecordTypeConverter::class, dbType = Int::class)
        var recordType: ERecordType = ERecordType.Category,
        var lastDate: Date = Date()
) {
    @Id var id: Long = 0

    class RecordTypeConverter : PropertyConverter<ERecordType, Int> {
        override fun convertToEntityProperty(databaseValue: Int): ERecordType {
            return enumValues<ERecordType>()[databaseValue]
        }
        override fun convertToDatabaseValue(entityProperty: ERecordType): Int {
            return entityProperty.ordinal
        }
    }

}