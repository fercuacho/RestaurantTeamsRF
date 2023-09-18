package com.example.restaurantteamsrf.data.remote

import com.example.restaurantteamsrf.data.remote.model.TeamDetailDto
import com.example.restaurantteamsrf.data.remote.model.TeamDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface TeamsApi {

    @GET
    fun getTeams(
        @Url url:String?
    ): Call<List<TeamDto>>

    @GET("cm/games/game_detail.php")
    //@GET ("teams/team_detail/")
    fun getTeamDetail(
        @Query("id") id: String?
    ):Call<TeamDetailDto>

    //Para Apiary
    @GET("teams/team_list")
    //@GET("games/games_list")
    fun getTeamsApiary(): Call<List<TeamDto>>

    //teams/team_detail/21357
    @GET("teams/team_detail/{id}")
    //@GET("games/game_detail/{id}")
    fun getTeamDetailApiary(
        @Path("id") id: String?/*,
        @Path("name") name: String?*/
    ): Call<TeamDetailDto>

}