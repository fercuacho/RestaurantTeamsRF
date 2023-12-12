package com.example.restaurantteamsrf.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.restaurantteamsrf.application.TeamsDBApp.Companion.prefs
import com.example.restaurantteamsrf.util.Constants


@Entity(tableName = Constants.DATABASE_TEAM_TABLE)
data class TeamEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "team_id")
    val id: Long = 0,

    @ColumnInfo(name = "team_name")
    var name: String,

    @ColumnInfo(name = "team_manager")
    var manager: UserEntity,

    @ColumnInfo(name = "team_members")
    var members: List<UserEntity> = listOf(manager),

    @ColumnInfo(name = "num_members")
    var num_members: String = members.size.toString(),

    @ColumnInfo(name = "team_identifier")
    val identificador: String = prefs.getIdSesion(),

    @ColumnInfo(name = "team_availability")
    var availability: List<AvailabilityEntity> = emptyList()

)
