package com.example.restaurantteamsrf.converters

import androidx.room.TypeConverter
import com.example.restaurantteamsrf.data.db.model.UserEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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

}