package com.example.restaurantteamsrf.data.db.model.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.restaurantteamsrf.data.db.model.AvailabilityEntity
import com.example.restaurantteamsrf.data.db.model.TeamEntity

data class TeamWithAvailability(
    @Embedded
    val teamEntity: TeamEntity
//
//    @Relation(parentColumn = "horario_identifier", entityColumn = "horario_identifier")
//    val availabilityList: List<AvailabilityEntity>
)