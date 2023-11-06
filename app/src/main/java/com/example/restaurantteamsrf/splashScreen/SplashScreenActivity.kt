package com.example.restaurantteamsrf.splashScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.restaurantteamsrf.R

import android.content.Intent
import android.media.MediaPlayer

import android.os.Handler

import com.example.restaurantteamsrf.ui.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mediaPlayer = MediaPlayer.create(this, R.raw.impact) // Reemplaza "nombre_de_tu_archivo_de_audio" con el nombre de tu archivo de audio sin la extensión.

        mediaPlayer?.start()

        val SPLASH_SCREEN_DURATION: Long = 2000 // Duración en milisegundos (por ejemplo, 3 segundos)
        Handler().postDelayed({
            mediaPlayer?.stop()
            mediaPlayer?.release()
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_SCREEN_DURATION)
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
