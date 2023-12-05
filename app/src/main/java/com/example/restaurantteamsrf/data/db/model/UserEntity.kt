package com.example.restaurantteamsrf.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.restaurantteamsrf.util.Constants
import java.util.UUID

@Entity(tableName = Constants.DATABASE_USERS_TABLE /*,
    foreignKeys = [ForeignKey(
    entity = TeamEntity::class,
    parentColumns = ["team_id"],
    childColumns = ["team_id"],
    onDelete = ForeignKey.CASCADE)]*/
)
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
    val tipoUsuario: String = "",

    @ColumnInfo(name = "user_identifier")
    val identificadorSesion: String = UUID.randomUUID().toString() /*,

    @ColumnInfo(name = "team_id")
    var teamId: Long? = null // Esta será la clave foránea que hace referencia al id del equipo*/
)
