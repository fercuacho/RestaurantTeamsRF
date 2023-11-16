package com.example.restaurantteamsrf.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.restaurantteamsrf.classes.Manager
import com.example.restaurantteamsrf.databinding.ActivityCreateTeamBinding

class CreateTeam : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTeamBinding

    var nameTeam = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnCrearTeam.setOnClickListener{

            nameTeam = binding.etNombreTeam.text.toString()

            val intent = intent
            val manager = intent.getSerializableExtra("manager") as? Manager

            if (manager != null) {
                // Creamos un team
                val team = manager.createTeam(nameTeam,manager)

                val intent2 = Intent(this, ActivityMenu::class.java)
                intent.putExtra("team", team)
                startActivity(intent2)
                finish()

            } else {
                // Maneja el caso en que el objeto Manager no se recupera correctamente
                //No papito
            }
        }
    }
}