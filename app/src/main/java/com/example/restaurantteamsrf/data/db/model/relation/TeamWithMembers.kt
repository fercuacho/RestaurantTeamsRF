package com.example.restaurantteamsrf.data.db.model.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.restaurantteamsrf.data.db.model.TeamEntity
import com.example.restaurantteamsrf.data.db.model.UserEntity

data class TeamWithMembers(
    @Embedded val team: TeamEntity,
    @Relation(
        parentColumn = "team_id",
        entityColumn = "team_id"
    )
    val members: List<UserEntity>
)