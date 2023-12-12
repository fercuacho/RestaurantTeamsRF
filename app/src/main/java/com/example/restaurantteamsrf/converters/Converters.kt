package com.example.restaurantteamsrf.converters

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.example.restaurantteamsrf.data.db.model.AvailabilityEntity
import com.example.restaurantteamsrf.data.db.model.UserEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class Converters {

    @TypeConverter
    fun fromUserEntity(UserEntity: UserEntity): String {
        return Gson().toJson(UserEntity)
    }

    @TypeConverter
    fun toUserEntity(json: String): UserEntity {
        val type = object : TypeToken<UserEntity>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromUserEntityList(UserEntitys: List<UserEntity>): String {
        return Gson().toJson(UserEntitys)
    }

    @TypeConverter
    fun toUserEntityList(json: String): List<UserEntity> {
        val type = object : TypeToken<List<UserEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromString(value: String): List<AvailabilityEntity> {
        val listType = object : TypeToken<List<AvailabilityEntity>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<AvailabilityEntity>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

//    @RequiresApi(Build.VERSION_CODES.O)
//    @TypeConverter
//    fun fromString(value: String?): Set<LocalDate>? {
//        return if (value.isNullOrEmpty()) {
//            emptySet()
//        } else {
//            value.split(",").map { LocalDate.parse(it, formatter) }.toSet()
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    @TypeConverter
//    fun localDateToString(dates: Set<LocalDate>?): String? {
//        return dates?.joinToString(",") { it.format(formatter) }
//    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    @TypeConverter
//    fun fromString(value: String?): Set<LocalDate>? {
//        return if (value.isNullOrBlank()) {
//            emptySet()
//        } else {
//            val dateStrings = value.trim('[', ']').split(", ")
//            dateStrings.mapNotNull { parseIsoDate(it) }.toSet()
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun parseIsoDate(dateStr: String): LocalDate? {
//        return try {
//            LocalDate.parse(dateStr)
//        } catch (e: DateTimeParseException) {
//            // Manejar la excepción según sea necesario, por ejemplo, loggearla
//            null
//        }
//    }
//    @RequiresApi(Build.VERSION_CODES.O)
//    @TypeConverter
//    fun localDateToString(dates: Set<LocalDate>?): String? {
//        return dates?.joinToString(",") { it.format(formatter) }
//    }
}