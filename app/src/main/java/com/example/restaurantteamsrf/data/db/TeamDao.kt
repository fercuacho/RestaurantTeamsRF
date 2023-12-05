package com.example.restaurantteamsrf.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.restaurantteamsrf.data.db.model.TeamEntity
import com.example.restaurantteamsrf.data.db.model.relation.TeamWithMembers
import com.example.restaurantteamsrf.util.Constants.DATABASE_TEAM_TABLE


@Dao
interface TeamDao {

    //Create

    @Insert
    suspend fun insertTeam(team: TeamEntity)

    @Insert
    suspend fun insertTeams(teams: List<TeamEntity>)

    //Read
    @Query("SELECT * FROM ${DATABASE_TEAM_TABLE}")
    suspend fun getAllTeams(): List<TeamEntity>

    @Query("SELECT * FROM ${DATABASE_TEAM_TABLE} WHERE team_identifier = :identificador ")
    suspend fun getTeamsForUser(identificador: String): List<TeamEntity>

    @Query("SELECT * FROM ${DATABASE_TEAM_TABLE} WHERE team_id = :id ")
    suspend fun getTeamById(id: Int): TeamEntity

    /*@Transaction
    @Query("SELECT * FROM ${DATABASE_TEAM_TABLE} WHERE team_id = :teamId")
    suspend fun getTeamWithMembers(teamId: Long): TeamWithMembers*/

    //Update
    @Update
    suspend fun updateTeam(team: TeamEntity)

    //Delete
    @Delete
    suspend fun deleteTeam(team: TeamEntity)
}