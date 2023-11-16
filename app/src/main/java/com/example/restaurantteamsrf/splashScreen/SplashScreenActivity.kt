package com.example.restaurantteamsrf.splashScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent

import com.example.restaurantteamsrf.ui.activities.Login

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, Login::class.java))
    }
}
