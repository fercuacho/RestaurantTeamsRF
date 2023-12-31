package com.example.restaurantteamsrf.data.db.model

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.restaurantteamsrf.util.Constants
import java.util.UUID
import kotlin.random.Random

@Entity(tableName = Constants.DATABASE_USERS_TABLE)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    val id: Long = 0,

    @ColumnInfo(name = "user_name")
    var name: String = "",

    @ColumnInfo(name = "user_lastname")
    var lastname: String = "",

    @ColumnInfo(name = "user_email")
    var email:String = "",

    @ColumnInfo(name = "user_password")
    var password:String = "",

    @ColumnInfo(name = "user_number")
    var number:String = "",

    @ColumnInfo(name = "user_type")
    var tipoUsuario: String = "",

    @ColumnInfo(name = "user_identifier")
    val identificadorSesion: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "user_availability")
    var availability: String = "",

    @ColumnInfo(name = "user_hours_to_work")
    var maxHoursToWork: Int = 0,

    @ColumnInfo(name = "user_rate_per_hour")
    var ratePerHour: Int = 0,

    @ColumnInfo(name = "user_color")
    var color: Int = Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))


    )
