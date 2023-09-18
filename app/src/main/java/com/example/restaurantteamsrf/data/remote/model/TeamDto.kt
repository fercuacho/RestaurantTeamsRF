package com.example.restaurantteamsrf.data.remote.model

import com.google.gson.annotations.SerializedName

data class TeamDto(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("logo")
    var logo: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("manager")
    var manager: String? = null,
    @SerializedName("lenght")
    var tamano: String? = null
)
