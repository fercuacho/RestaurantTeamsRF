package com.example.restaurantteamsrf.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.restaurantteamsrf.R
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.data.remote.RetrofitHelper
import com.example.restaurantteamsrf.data.remote.model.TeamDto
import com.example.restaurantteamsrf.databinding.ActivityMainBinding
import com.example.restaurantteamsrf.ui.fragments.TeamsListFragment
import com.example.restaurantteamsrf.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    /*
    private lateinit var repository: TeamRepository
    private lateinit var retrofit: Retrofit*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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