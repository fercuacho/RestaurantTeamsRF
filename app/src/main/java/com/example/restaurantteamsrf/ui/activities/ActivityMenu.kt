package com.example.restaurantteamsrf.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.restaurantteamsrf.R
import com.example.restaurantteamsrf.classes.Employee
import com.example.restaurantteamsrf.classes.Manager
import com.example.restaurantteamsrf.classes.Person
import com.example.restaurantteamsrf.classes.Team
import com.example.restaurantteamsrf.databinding.ActivityMenuBinding
import com.example.restaurantteamsrf.ui.fragments.ProfileFragment
import com.example.restaurantteamsrf.ui.fragments.ScheduleFragment
import com.example.restaurantteamsrf.ui.fragments.StatisticsFragment
import com.example.restaurantteamsrf.ui.fragments.TeamsFragment

class ActivityMenu : AppCompatActivity() {

    private lateinit var binding:ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.teams -> {
                    var team = intent.getSerializableExtra("team") as? Team

                    if (team != null) {
                        Toast.makeText(this, "entro al if, team no es nulo", Toast.LENGTH_SHORT).show()

                        // Pasa la información del equipo al fragmento correspondiente
                        val teamsFragment = TeamsFragment()
                        val bundle = Bundle()
                        bundle.putSerializable("team", team)
                        teamsFragment.arguments = bundle
                        teamsFragment

                    } else {

                        Toast.makeText(this, "entro al else, team es nulo", Toast.LENGTH_SHORT).show()

                        // Si no hay información del equipo, devuelve un fragmento predeterminado o lanza un error
                        val teamsFragment = TeamsFragment()
                        val bundle = Bundle()
                        team = Team("error team")
                        bundle.putSerializable("team", team)
                        teamsFragment.arguments = bundle
                        teamsFragment
                    }
                }
                R.id.schedule -> ScheduleFragment()
                R.id.stat -> StatisticsFragment()
                R.id.profile -> ProfileFragment()
                else -> TeamsFragment()
            }

            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, selectedFragment).commit()
            true
        }
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, TeamsFragment()).commit()
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            //.addToBackStack("RegistroFragment")
            .replace(R.id.frame_layout,fragment)
            .commit()
    }
}