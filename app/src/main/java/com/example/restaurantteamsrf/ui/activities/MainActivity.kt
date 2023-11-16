package com.example.restaurantteamsrf.ui.activities

import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.restaurantteamsrf.R
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.data.remote.RetrofitHelper
import com.example.restaurantteamsrf.data.remote.model.TeamDto
import com.example.restaurantteamsrf.databinding.ActivityMainBinding
import com.example.restaurantteamsrf.ui.fragments.TeamsListFragment
import com.example.restaurantteamsrf.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.security.GeneralSecurityException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /*
    private lateinit var repository: TeamRepository
    private lateinit var retrofit: Retrofit*/

    private lateinit var mp: MediaPlayer

    private lateinit var encryptedSharedPreferences: EncryptedSharedPreferences
    private lateinit var encryptedSharedPrefsEditor: SharedPreferences.Editor
    private var user: FirebaseUser? = null
    private var userId: String? = null
    private var banderaEmailVerificado = true
    private var banderaHuellaActiva = false
    private var psw = ""
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        mp = MediaPlayer.create(this, R.raw.impact)
//        mp.start()
//        installSplashScreen()

        firebaseAuth = FirebaseAuth.getInstance()

        user = firebaseAuth?.currentUser
        userId = user?.uid

        //binding.tvUsuario.text = user?.email

        //Obteniendo el password desde el activity Login
        val bundle: Bundle? = intent.extras
        if(bundle != null) {
            psw = bundle.getString("psw", "")
        }


        try {
            //Creando la llave para encriptar
            val masterKeyAlias = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            encryptedSharedPreferences = EncryptedSharedPreferences
                .create(
                    this,
                    "account",
                    masterKeyAlias,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                ) as EncryptedSharedPreferences
        }catch(e: GeneralSecurityException){
            e.printStackTrace()
            Log.d("LOGS", "Error: ${e.message}")
        }catch (e: IOException){
            e.printStackTrace()
            Log.d("LOGS", "Error: ${e.message}")
        }

        encryptedSharedPrefsEditor = encryptedSharedPreferences.edit()



        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TeamsListFragment())
                .commit()
        }


        /*retrofit = RetrofitHelper().getRetrofit()

        repository = TeamRepository(retrofit)

        lifecycleScope.launch {
            val call: Call<List<TeamDto>> = repository.getTeamsApiary()

            call.enqueue(object: Callback<List<TeamDto>> {
                override fun onResponse(
                    call: Call<List<TeamDto>>,
                    response: Response<List<TeamDto>>
                ) {
                    Log.d(Constants.LOGTAG, "Respuesta del servidor ${response.body()}")
                }

                override fun onFailure(call: Call<List<TeamDto>>, t: Throwable) {
                    //Manejo del error
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })

        }*/


    }
}