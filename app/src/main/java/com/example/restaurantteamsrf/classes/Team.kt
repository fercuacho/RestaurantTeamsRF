package com.example.restaurantteamsrf.classes

import java.io.Serializable


class Team(var nameTeam:String? = "") {
    val members = mutableListOf<Person>()
/*

    fun addMember(member: Person) {
        members.add(member)
    }

    fun addMembers(newMembers: ArrayList<Person>) {
        members.addAll(newMembers)
    }

    fun teamSize() : Int = members.size

    fun getInfo(){
        for (member in members){
            //println("nombre del equipo ${nameTeam}")
            println("Miembro del equipo: ${member.getPersonInfo()}")
        }
    }*/
}
