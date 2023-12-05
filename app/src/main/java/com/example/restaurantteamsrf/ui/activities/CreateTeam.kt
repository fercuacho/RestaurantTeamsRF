package com.example.restaurantteamsrf.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.restaurantteamsrf.application.TeamsDBApp
import com.example.restaurantteamsrf.application.TeamsDBApp.Companion.prefs
import com.example.restaurantteamsrf.classes.Manager
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.data.db.model.TeamEntity
import com.example.restaurantteamsrf.data.db.model.UserEntity
import com.example.restaurantteamsrf.databinding.ActivityCreateTeamBinding
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID

class CreateTeam : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTeamBinding

    var nameTeam = ""
    //Para las cajas de texto
    private var email = ""
    private var contrasenia = ""
    private var nombre = ""
    private var apellido = ""
    private var numero = ""

    //Manager Objeto


    private lateinit var repository: TeamRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCrearTeam.setOnClickListener{

            nameTeam = binding.etNombreTeam.text.toString()

            val intent = intent

            nombre = intent.getStringExtra("nombre_manager").toString()
            apellido = intent.getStringExtra("apellido_manager").toString()
            numero = intent.getStringExtra("numero").toString()
            email = intent.getStringExtra("email").toString()
            contrasenia = intent.getStringExtra("contrasenia").toString()

            var manager = UserEntity(name = nombre, lastname = apellido, number = numero, email = email, password = contrasenia, tipoUsuario = "manager")

            prefs.saveName(nombre)
            prefs.saveIDSesion(manager.identificadorSesion)// TAL VEZ TRUENE AQUÍ
            //Es importante guardar en prefs el identificador de sesion antes
            // de crear un team porque la clase team lo utiliza al crear un team

            repository = (this.applicationContext as TeamsDBApp).repository

            // Creamos un team
            var team = TeamEntity(name = nameTeam, manager = manager)

            try {
                lifecycleScope.launch {
                    repository.insertUser(manager)
                    repository.insertTeam(team) //guardamos en room
                }
                /*message(getString(R.string.exito_al_guardar_el_equipo))
                updateUI()*/
            }catch (e: IOException){
                e.printStackTrace()
               //message(getString(R.string.error_al_guardar_el_equipo))
            }

            val intent2 = Intent(this, ActivityMenu::class.java)
            startActivity(intent2)
            finish()

        }
    }

//    fun generarIdentificadorUnico(): String {
//        return UUID.randomUUID().toString()
//    }

}