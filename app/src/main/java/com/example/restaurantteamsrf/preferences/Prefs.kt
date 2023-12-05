package com.example.restaurantteamsrf.preferences

import android.content.Context


class Prefs(val context: Context) {

    val SHARED_NAME = "Mydtb"
    val SHARED_USER_NAME = "username"
    val SHARED_PASS = "vip"

    val SHARED_IDENTIFICADOR_SESION = "identificador_sesion"

    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    fun saveName(name: String) {
        storage.edit().putString(SHARED_USER_NAME, name).apply()
    }

    fun saveVIP(vip:Boolean) {
        storage.edit().putBoolean(SHARED_PASS, vip).apply()
    }

    fun getName():String{
        return storage.getString(SHARED_USER_NAME, "")!!
    }

    fun getVIP(): Boolean {
        return storage.getBoolean(SHARED_PASS,  false)
    }

    fun saveIDSesion(identificadorSesion: String) {
        storage.edit().putString(SHARED_IDENTIFICADOR_SESION, identificadorSesion).apply()
    }

    fun getIdSesion():String {
        return storage.getString(SHARED_IDENTIFICADOR_SESION, "")!!
    }

    fun wipe(){
        storage.edit().clear().apply()
    }

}