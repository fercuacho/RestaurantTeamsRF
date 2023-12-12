package com.example.restaurantteamsrf.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.restaurantteamsrf.util.Constants
import java.time.DayOfWeek

@Entity(tableName = Constants.DATABASE_AVAILABILITY_TABLE)
data class AvailabilityEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "availability_id")
    val id: Long = 0,

    @ColumnInfo(name = "dayOfWeek")
    val dayOfWeek: DayOfWeek? = null,

    @ColumnInfo(name = "schedule")
    val horario: String = "",

    @ColumnInfo(name = "horario_identifier")
    val horario_identifier: Long = 0
)