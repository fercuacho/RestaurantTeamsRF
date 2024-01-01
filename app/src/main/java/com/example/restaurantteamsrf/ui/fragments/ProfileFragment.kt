package com.example.restaurantteamsrf.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.restaurantteamsrf.R
import com.example.restaurantteamsrf.application.TeamsDBApp
import com.example.restaurantteamsrf.application.TeamsDBApp.Companion.prefs
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.data.db.model.utils.modifyUserProperties
import com.example.restaurantteamsrf.databinding.FragmentProfileBinding
import com.example.restaurantteamsrf.databinding.FragmentTeamsBinding
import com.example.restaurantteamsrf.ui.activities.ActivityMenu
import com.example.restaurantteamsrf.ui.activities.Login
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: TeamRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireContext().applicationContext as TeamsDBApp).repository

//        binding.tvUsuario.text = prefs.getName()
//        binding.tvIdentificador.text = prefs.getIdSesion()

        val idSesion = prefs.getIdSesion()

        lifecycleScope.launch {
            // Mueve la obtención del usuario al hilo secundario
            val user = withContext(Dispatchers.IO) {
                repository.getCurrentUser(idSesion)
            }

            if (user != null) {
                binding.apply {
                    tieNameUser.setText(user.name)
                    tieApellido.setText(user.lastname)
                    tieCorreo.setText(user.email)
                    tieNumber.setText(user.number)
                }
            }

            binding.btnGuardarConfiguracion.setOnClickListener {
                lifecycleScope.launch {

                    binding.apply {
                        val newName = tieNameUser.text.toString()
                        val newApellido = tieApellido.text.toString()
                        val newEmail = tieCorreo.text.toString()
                        val newNumber = tieNumber.text.toString()


                    if (user != null) {
                        user.name = newName
                        user.lastname = newApellido
                        user.email = newEmail
                        user.number = newNumber
                        repository.updateUser(user)
//                            team.modifyUserProperties(
//                                userId = memberId, newMaxHoursToWork = maxHours, newRatePerHour = ratePerHour
//                            )
                    }
                }

            }

        }



        binding.btnCerrarSesion.setOnClickListener{
            prefs.wipe()
            val intent = Intent(activity, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            activity?.finish()

        }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}