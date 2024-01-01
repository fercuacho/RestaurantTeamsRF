package com.example.restaurantteamsrf.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.example.restaurantteamsrf.R
import com.example.restaurantteamsrf.application.TeamsDBApp
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.data.db.model.TeamEntity
import com.example.restaurantteamsrf.data.db.model.UserEntity
import com.example.restaurantteamsrf.data.db.model.utils.modifyUserProperties
import com.example.restaurantteamsrf.databinding.FragmentMemberSettingsBinding
import com.example.restaurantteamsrf.databinding.FragmentTeamElementDetailBinding
import com.example.restaurantteamsrf.ui.activities.SeleccionaDisponibilidadCalendario
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TEAM_ID = "ID_TEAM"
private const val MEMBER_ID = "ID_MEMBER"


class MemberSettingsFragment : Fragment() {

    private lateinit var repository: TeamRepository

    private var _binding: FragmentMemberSettingsBinding? = null
    private val binding get() = _binding!!

    private var teamId = ""
    private var memberId = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMemberSettingsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireContext().applicationContext as TeamsDBApp).repository

            teamId = arguments?.getString(TEAM_ID).toString()
            memberId = arguments?.getString(MEMBER_ID).toString()


            lifecycleScope.launch {
                    // Mueve la obtención del usuario al hilo secundario
                    val user = withContext(Dispatchers.IO) {
                        repository.getCurrentUser(memberId)
                    }

                    val team = withContext(Dispatchers.IO) {
                            repository.getTeamById(teamId.toInt())
                    }

                    if (user != null) {
                        binding.tieRatePerHour.setText(user.ratePerHour.toString())
                        binding.tieWorkHours.setText(user.maxHoursToWork.toString())
                        if(user.color != 0) {
                            binding.colorUser.setBackgroundColor(user.color)
                        }
                        binding.tvNameMember.text = user.name
                        binding.btnGuardarCambios.setOnClickListener {
                            lifecycleScope.launch {
                                // Actualiza el usuario en el hilo secundario
                                val maxHours = binding.tieWorkHours.text.toString().toInt()
                                val ratePerHour = binding.tieRatePerHour.text.toString().toInt()

                                user.maxHoursToWork = maxHours
                                user.ratePerHour = ratePerHour

                                team.modifyUserProperties(
                                    userId = memberId, newMaxHoursToWork = maxHours, newRatePerHour = ratePerHour
                                )
                                repository.updateTeam(team)
                                repository.updateUser(user)
                            }
                        }

                        binding.btnDisponibilidad.setOnClickListener {

                            if (team.availability.isNotEmpty()){
//                            message("el team: ${team.id} y el usuario ${user.identificadorSesion}")
                            goToCalendarAvailability(user.identificadorSesion, team.id.toString())

                            }else{
                                message("Seleciona el horario del equipo primero")
                            }
                        }

                        binding.btnConfigColor.setOnClickListener {
                            ColorPickerDialog
                                .Builder(requireContext())
                                .setTitle("Selecciona un color")
                                .setColorShape(ColorShape.CIRCLE)
                                .setColorListener { color, _ ->
                                    // Almacena el color seleccionado en la variable
                                    val selectedColor = color
                                    lifecycleScope.launch {

                                        user.color = color

                                        team.modifyUserProperties(userId = memberId, newColor = color)

                                        repository.updateTeam(team)
                                        repository.updateUser(user)

                                        if(user.color != 0) {
                                            binding.colorUser.setBackgroundColor(user.color)
                                        }

                                    }

                                }
                                .show()
                        }

                    }
            }
        }




    private fun goToCalendarAvailability(memberId: String, teamId:String){
//        if (team.availability.isNotEmpty()){
//            val idMember = member.identificadorSesion
//            val idTeam = team.id

//            message("el team: $idTeam y el usuario $idMember")

            val intent = Intent(activity, SeleccionaDisponibilidadCalendario::class.java)

            // Puedes pasar datos extras a la actividad si es necesario.
            intent.putExtra("ID_MEMBER", memberId)
            intent.putExtra("ID_TEAM", teamId)

            // Inicias la actividad.
            startActivity(intent)
//        }else{
//            message("Seleciona el horario del equipo primero")
//        }
    }

    private fun message (text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()

        /*Snackbar.make(binding.cl, text, Snackbar.LENGTH_SHORT)
            .setTextColor(Color.parseColor(getString(R.string.color_message)))
            .setBackgroundTint(Color.parseColor(getString(R.string.background_message_color)))
            .show()*/
    }


}