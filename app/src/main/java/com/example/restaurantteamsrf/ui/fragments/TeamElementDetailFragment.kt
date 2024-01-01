package com.example.restaurantteamsrf.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantteamsrf.R
import com.example.restaurantteamsrf.application.TeamsDBApp
import com.example.restaurantteamsrf.application.TeamsDBApp.Companion.prefs
import com.example.restaurantteamsrf.calendar.ui.fragments.Example5Fragment
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.data.db.model.TeamEntity
import com.example.restaurantteamsrf.data.db.model.UserEntity
import com.example.restaurantteamsrf.databinding.FragmentTeamElementDetailBinding
import com.example.restaurantteamsrf.ui.activities.NewMemberDialog
import com.example.restaurantteamsrf.ui.activities.NewTeamDialog
import com.example.restaurantteamsrf.ui.activities.SeleccionaDisponibilidadCalendario
import com.example.restaurantteamsrf.ui.adapters.MembersAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TEAM_ID = "team_id"

class TeamElementDetailFragment : Fragment() {

    private var _binding: FragmentTeamElementDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: TeamRepository

    private var teamId: String? = null

    private lateinit var membersAdapter: MembersAdapter

    private lateinit var  members : List<UserEntity>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTeamElementDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireContext().applicationContext as TeamsDBApp).repository


        arguments?.let { it ->
            teamId = it.getString(TEAM_ID)

            lifecycleScope.launch {
                teamId?.let { id ->


                    val team = repository.getTeamById(id.toInt())

                    //Toast.makeText(requireContext(), "el identificador de ${team.name} es $id", Toast.LENGTH_LONG).show()

                    binding.tvNameTeam.text = team.name

                    members = team.members

                    membersAdapter = MembersAdapter(members){ member ->
                        OnMemberClicked(member.identificadorSesion,id)
                    }

                    binding.rvTeamMembers.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = membersAdapter
                    }

                    binding.btnAddMember.setOnClickListener {
                        val dialog =
                            NewMemberDialog(team = team, updateUI = {
                                updateUI(id)
                            }, message = {
                                message(it)
                            })
                        dialog.show(requireActivity().supportFragmentManager, "dialog")
                    }

                    binding.btnEditOpeningHours.setOnClickListener {

                        val bundle = Bundle()
                        bundle.putString("team_id", id)

                        val nuevoFragmento = TeamScheduleFragment()
                        nuevoFragmento.arguments = bundle

                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.teamsFragmentprueba, nuevoFragmento)
                            .addToBackStack("teamsElementFragment")
                            .commit()

                    }


                    binding.btnCreateSchedule.setOnClickListener {

                        val bundle = Bundle()
                        bundle.putString("team_id", id)

                        if(team.availability.isNotEmpty()){
                            val nuevoFragmento = Example5Fragment()
                            nuevoFragmento.arguments = bundle

                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.teamsFragmentprueba, nuevoFragmento)
                                .addToBackStack("teamsElementFragment")
                                .commit()
                        }else {
                            message("Seleciona el horario del equipo primero")
                        }


                    }

                }
            }
        }


    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(teamId: String) =
            TeamElementDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(TEAM_ID, teamId)
                }
            }
    }

    private fun updateUI(id : String) {

        lifecycleScope.launch {

//            val teamActual = withContext(Dispatchers.IO) {
//                repository.getTeamById(id.toInt())
//            }
//
//            members = teamActual.members


            members = repository.getTeamById(id.toInt()).members
            membersAdapter.updateList(members)
        }
    }

    private fun message (text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()

        /*Snackbar.make(binding.cl, text, Snackbar.LENGTH_SHORT)
            .setTextColor(Color.parseColor(getString(R.string.color_message)))
            .setBackgroundTint(Color.parseColor(getString(R.string.background_message_color)))
            .show()*/
    }

    private fun OnMemberClicked(idMember: String, idTeam: String){

        val bundle = Bundle()
        bundle.putString("ID_TEAM", idTeam)
        bundle.putString("ID_MEMBER", idMember)


        val nuevoFragmento = MemberSettingsFragment()
        nuevoFragmento.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.teamsFragmentprueba, nuevoFragmento)
            .addToBackStack("teamsElementDetailFragment")
            .commit()

    }

}