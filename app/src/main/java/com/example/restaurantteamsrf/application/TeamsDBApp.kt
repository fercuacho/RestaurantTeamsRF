package com.example.restaurantteamsrf.application

import android.app.Application
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.data.db.TeamDatabase
import com.example.restaurantteamsrf.preferences.Prefs

class TeamsDBApp: Application() {

    companion object{
        lateinit var prefs: Prefs
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
    }

    private val database by lazy {
        TeamDatabase.getDatabase(this)
    }

    val repository by lazy {
        TeamRepository(database.teamDao(), database.userDao(), database.availabilityDao())
    }
}