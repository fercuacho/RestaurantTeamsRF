package com.example.restaurantteamsrf.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.restaurantteamsrf.R
import com.example.restaurantteamsrf.application.TeamsDBApp.Companion.prefs
import com.example.restaurantteamsrf.databinding.FragmentProfileBinding
import com.example.restaurantteamsrf.databinding.FragmentTeamsBinding
import com.example.restaurantteamsrf.ui.activities.ActivityMenu
import com.example.restaurantteamsrf.ui.activities.Login


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

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

        binding.tvUsuario.text = prefs.getName()
        binding.tvIdentificador.text = prefs.getIdSesion()

        binding.btnCerrarSesion.setOnClickListener{
            prefs.wipe()
            val intent = Intent(activity, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            activity?.finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}