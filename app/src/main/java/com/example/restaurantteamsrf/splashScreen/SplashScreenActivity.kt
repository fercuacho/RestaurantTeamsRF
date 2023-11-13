package com.example.restaurantteamsrf.splashScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.restaurantteamsrf.R

import android.content.Intent
import android.media.MediaPlayer

import android.os.Handler
import com.example.restaurantteamsrf.Login

import com.example.restaurantteamsrf.ui.MainActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, Login::class.java))
    }
}
