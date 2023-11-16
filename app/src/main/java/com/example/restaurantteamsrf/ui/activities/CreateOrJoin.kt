package com.example.restaurantteamsrf.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.restaurantteamsrf.R
import com.example.restaurantteamsrf.databinding.ActivityCreateOrJoinBinding

class CreateOrJoin : AppCompatActivity() {

    private lateinit var binding:ActivityCreateOrJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateOrJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCrearTeam.setOnClickListener{
            val intent = Intent(this, CreateManagerAccount::class.java)
            startActivity(intent)
            finish()
        }

    }



}