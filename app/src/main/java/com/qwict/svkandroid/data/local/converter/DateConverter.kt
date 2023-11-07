package com.qwict.svkandroid.data.local.converter

import androidx.room.TypeConverter
import java.util.Date

object DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long): Date {
        return Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
}
