package com.example.restaurantteamsrf.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.restaurantteamsrf.R
import com.example.restaurantteamsrf.calendar.ui.fragments.Example1Fragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController



class SeleccionaDisponibilidadCalendario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecciona_disponibilidad_calendario)

        // Recibe los datos del Intent
        val idMember = intent.getStringExtra("ID_MEMBER")
        val idTeam = intent.getStringExtra("ID_TEAM")

        if (!idTeam.isNullOrEmpty()) {
            val bundle = Bundle()
            bundle.putString("ID_MEMBER", idMember)
            bundle.putString("ID_TEAM", idTeam)

            val example1Fragment = Example1Fragment()
            example1Fragment.arguments = bundle

            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, example1Fragment)
            fragmentTransaction.commit()

        } else {
            Log.e("YourActivity", "ID_TEAM is null or empty")
            finish()

            // Aquí puedes manejar el caso en el que idTeam sea nulo o vacío, si es necesario.
        }
    }
}