package com.example.restaurantteamsrf.classes

class Employee(override var name:String,
               override val lastname: String,
               override val email:String? = "",
               override val password:String? = "",
               override val number:String? = "") : Person(name, lastname, email, password, number) {
}