package com.example.restaurantteamsrf.classes

import java.io.Serializable

open class Person(
    open val name: String,
    open val lastname: String,
    open val email: String? = "",
    open val password: String? = "",
    open val number: String? = ""
): Serializable {

    constructor() : this("", "", "", "", "")

    fun getPersonInfo(): String{
        return "Name: $name, Lastname: $lastname"
    }


}