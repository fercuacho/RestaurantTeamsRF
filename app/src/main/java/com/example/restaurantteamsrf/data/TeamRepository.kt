package com.example.restaurantteamsrf.data

import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.restaurantteamsrf.data.db.TeamDao
import com.example.restaurantteamsrf.data.db.UserDao
import com.example.restaurantteamsrf.data.db.model.TeamEntity
import com.example.restaurantteamsrf.data.db.model.UserEntity
import com.example.restaurantteamsrf.data.remote.TeamsApi
import com.example.restaurantteamsrf.data.remote.model.TeamDetailDto
import com.example.restaurantteamsrf.data.remote.model.TeamDto
import retrofit2.Call
import retrofit2.Retrofit

class TeamRepository (/*private val retrofit: Retrofit,*/ private val teamDao: TeamDao, private val userDao: UserDao) {
    /*
    private val teamsApi: TeamsApi = retrofit.create(TeamsApi::class.java)

    fun getTeams(url: String): Call<List<TeamDto>> =
        teamsApi.getTeams(url)

    fun getTeamDetail(id: String?): Call<TeamDetailDto> =
        teamsApi.getTeamDetail(id)

    fun getTeamsApiary(): Call<List<TeamDto>> =
        teamsApi.getTeamsApiary()

    fun getTeamDetailApiary(id: String?): Call<TeamDetailDto> =
        teamsApi.getTeamDetailApiary(id)*/


    suspend fun insertTeam(team: TeamEntity){
        teamDao.insertTeam(team)
    }

    suspend fun getAllTeams(): List<TeamEntity> = teamDao.getAllTeams()

    suspend fun getTeamsForUser(idUser: String): List<TeamEntity> = teamDao.getTeamsForUser(idUser)

    suspend fun getTeamById(id: Int): TeamEntity = teamDao.getTeamById(id)


    suspend fun updateTeam(team: TeamEntity){
        teamDao.updateTeam(team)
    }

    suspend fun deleteTeam(team: TeamEntity){
        teamDao.deleteTeam(team)
    }

    ///////////////////////////////////Para User Entity////////////////////////////////////////////
    suspend fun insertUser(user: UserEntity){
        userDao.insertUser(user)
    }

    suspend fun updateUser(user: UserEntity){
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: UserEntity){
        userDao.deleteUser(user)
    }

    suspend fun getCurrentUser(email: String, pass: String): UserEntity? = userDao.getCurrentUser(email, pass)

    suspend fun getCurrentUser(identificador: String): UserEntity? = userDao.getCurrentUser(identificador)


}