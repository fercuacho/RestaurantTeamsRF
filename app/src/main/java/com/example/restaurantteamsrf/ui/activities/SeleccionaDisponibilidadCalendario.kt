package com.example.restaurantteamsrf.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.restaurantteamsrf.R
import com.example.restaurantteamsrf.calendar.ui.fragments.Example1Fragment

class SeleccionaDisponibilidadCalendario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecciona_disponibilidad_calendario)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Recibe los datos del Intent
        val idMember = intent.getStringExtra("ID_MEMBER")
        val idTeam = intent.getStringExtra("ID_TEAM")


        val bundle = Bundle()
        bundle.putString("ID_MEMBER", idMember)  // Agrega los parámetros según tus necesidades
        bundle.putString("ID_TEAM", idTeam)  // Agrega los parámetros según tus necesidades

        val example1Fragment = Example1Fragment()
        example1Fragment.arguments = bundle

        // Reemplaza el contenedor con el ID 'container' con tu fragmento
        fragmentTransaction.replace(R.id.container, example1Fragment)

        // O puedes agregar el fragmento sin reemplazar, dependiendo de tus necesidades
        // fragmentTransaction.add(R.id.container, MiFragmento())

        fragmentTransaction.commit()
    }
}