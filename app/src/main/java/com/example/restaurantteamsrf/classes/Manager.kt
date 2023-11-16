package com.example.restaurantteamsrf.classes

import java.io.Serializable


class Manager(
    override var name:String,
    override var lastname: String,
    override var email:String? = "",
    override var password:String? = "",
    override var number:String? = "") : Person(name, lastname, email, password, number),
    Serializable {

    constructor() : this("", "", "", "", "")


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
    }




}