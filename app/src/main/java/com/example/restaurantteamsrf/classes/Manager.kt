package com.example.restaurantteamsrf.classes

import android.os.Parcelable
import java.io.Serializable


class Manager(
    var name:String? = "",
    var lastname: String? = "",
    var email:String? = "",
    var password:String? = "",
    var number:String? = "") /*: Person(name, lastname, email, password, number)*/
{

/*
    fun createTeam(teamName: String? = "", member: Person? = null): Team {
        var team = Team()
        if (teamName != ""){
            team = Team(teamName)
        }
        member?.let { team.addMember(it) }
        return team
    }

    fun createTeam( teamName: String, members: ArrayList<Person>): Team{
        val team = Team(teamName)
        members?.let { team.addMembers(it) }
        return team
    }*/




}