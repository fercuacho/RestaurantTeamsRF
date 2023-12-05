package com.example.restaurantteamsrf.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.restaurantteamsrf.R
import com.example.restaurantteamsrf.application.TeamsDBApp
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.data.db.model.TeamEntity
import com.example.restaurantteamsrf.databinding.ActivityMenuBinding
import com.example.restaurantteamsrf.ui.adapters.TeamWorkAdapter
import com.example.restaurantteamsrf.ui.fragments.ProfileFragment
import com.example.restaurantteamsrf.ui.fragments.ScheduleFragment
import com.example.restaurantteamsrf.ui.fragments.StatisticsFragment
import com.example.restaurantteamsrf.ui.fragments.TeamsFragment
import kotlinx.coroutines.launch

class ActivityMenu : AppCompatActivity() {

    private lateinit var binding:ActivityMenuBinding

    private lateinit var repository: TeamRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as TeamsDBApp).repository

        replaceFragment(TeamsFragment())

        val condicion = supportFragmentManager.findFragmentById(R.id.frame_layout)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.teamsFragment -> {
                    if (condicion !is TeamsFragment) {
                        replaceFragment(TeamsFragment())
                    }
                    true
                }

                R.id.scheduleFragment -> {
                    if (condicion !is ScheduleFragment) {
                        replaceFragment(ScheduleFragment())
                    }
                    true
                }
                R.id.statisticsFragment -> {
                    if (condicion !is StatisticsFragment) {
                        replaceFragment(StatisticsFragment())
                    }
                    true
                }
                R.id.profileFragment -> {
                    if (condicion !is ProfileFragment) {
                        replaceFragment(ProfileFragment())
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            //.addToBackStack("RegistroFragment")
            .replace(R.id.frame_layout,fragment)
            .commit()
    }

}