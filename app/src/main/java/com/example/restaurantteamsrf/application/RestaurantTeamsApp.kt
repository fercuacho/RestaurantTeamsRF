package com.example.restaurantteamsrf.application

import android.app.Application
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.data.remote.RetrofitHelper

class RestaurantTeamsApp: Application() {
    private val retrofit by lazy{
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy{
        TeamRepository(retrofit)
    }
}