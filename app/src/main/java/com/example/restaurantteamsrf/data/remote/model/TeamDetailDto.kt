package com.example.restaurantteamsrf.data.remote.model

import com.google.gson.annotations.SerializedName

data class TeamDetailDto (
    @SerializedName("mainDish")
    var mainDish: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("logo")
    var logo: String? = null,
    @SerializedName("lenght")
    var lenght: String? = null,
    @SerializedName("contry")
    var contry: String? = null,
    @SerializedName("manager")
    var manager: String? = null,
    @SerializedName("video")
    var video: String? = null

)