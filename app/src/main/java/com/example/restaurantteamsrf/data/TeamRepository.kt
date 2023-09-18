package com.example.restaurantteamsrf.data

import com.example.restaurantteamsrf.data.remote.TeamsApi
import com.example.restaurantteamsrf.data.remote.model.TeamDetailDto
import com.example.restaurantteamsrf.data.remote.model.TeamDto
import retrofit2.Call
import retrofit2.Retrofit

class TeamRepository (private val retrofit: Retrofit) {

    private val teamsApi: TeamsApi = retrofit.create(TeamsApi::class.java)

    fun getTeams(url: String): Call<List<TeamDto>> =
        teamsApi.getTeams(url)

    fun getTeamDetail(id: String?): Call<TeamDetailDto> =
        teamsApi.getTeamDetail(id)

    fun getTeamsApiary(): Call<List<TeamDto>> =
        teamsApi.getTeamsApiary()

    fun getTeamDetailApiary(id: String?): Call<TeamDetailDto> =
        teamsApi.getTeamDetailApiary(id)
}