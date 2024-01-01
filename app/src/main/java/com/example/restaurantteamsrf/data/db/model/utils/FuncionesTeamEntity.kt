package com.example.restaurantteamsrf.data.db.model.utils

import com.example.restaurantteamsrf.data.db.model.TeamEntity

fun TeamEntity.modifyUserProperties(
    userId: String,
    newName: String? = null,
    newLastname: String? = null,
    newEmail: String? = null,
    newPassword: String? = null,
    newNumber: String? = null,
    newAvailability: String? = null,
    newMaxHoursToWork: Int? = null,
    newRatePerHour: Int? = null,
    newColor: Int? = null
) {
    val user = members.find { it.identificadorSesion == userId }
    user?.apply {
        // Modificar las propiedades del usuario si se encuentra
        newName?.let { name = it }
        newLastname?.let { lastname = it }
        newEmail?.let { email = it }
        newPassword?.let { password = it }
        newNumber?.let { number = it }
        newAvailability?.let { availability = it }
        newMaxHoursToWork?.let { maxHoursToWork = it }
        newRatePerHour?.let { ratePerHour = it }
        newColor?.let { color = it }
    }
}