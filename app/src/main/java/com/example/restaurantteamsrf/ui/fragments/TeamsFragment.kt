package com.example.restaurantteamsrf.ui.fragments

import android.app.Dialog
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
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.data.db.model.TeamEntity
import com.example.restaurantteamsrf.data.db.model.UserEntity
import com.example.restaurantteamsrf.databinding.FragmentTeamsBinding
import com.example.restaurantteamsrf.ui.activities.NewTeamDialog
import com.example.restaurantteamsrf.ui.adapters.TeamWorkAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class TeamsFragment : Fragment() {

    private var _binding: FragmentTeamsBinding? = null
    private val binding get() = _binding!!

    private lateinit var teamAdapter: TeamWorkAdapter

    private lateinit var repository: TeamRepository

    private var teams: List<TeamEntity> = emptyList()
    private var user = UserEntity()

    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTeamsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Se va la progressbar
        //binding.pbLoading.visibility = View.GONE

        teamAdapter = TeamWorkAdapter(teams,
        onClickDeleteTeam = {team ->
            onClickDeleteTeam(team)
        },
            goToDetailTeam = {team ->
                goToTeamDetails(team)
                binding.btnNewTeam.hide()
            }
        )

        binding.rvTeams.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = teamAdapter
        }

        //creo mi instancia de repositorio
        repository = (requireContext().applicationContext as TeamsDBApp). repository

        lifecycleScope.launch {
            //Esta linea devuelve los teams que coinsiden con el id de la sesion
            // actual que se obtiene que se obtienen de sharedprefs
            val currentUser = withContext(Dispatchers.IO) {
                repository.getCurrentUser(prefs.getIdSesion())
            }

            user = currentUser ?: UserEntity()

            binding.btnNewTeam.setOnClickListener {
                val dialog =
                    NewTeamDialog(team = TeamEntity(name = "", manager = user), updateUI = {
                        updateUI()
                    }, message = {
                        message(it)
                    })
                dialog.show(requireActivity().supportFragmentManager, "dialog")
            }
        }

        updateUI()

    }

    private fun onClickDeleteTeam(team: TeamEntity) {

        //Toast.makeText(requireContext(), "estas intentando eliminar el team ${team.name}", Toast.LENGTH_LONG).show()
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Team")
            .setMessage("¿Realmente deseas eliminar el equipo?")
            .setPositiveButton("Delete") { _, _ ->
                try {
                    lifecycleScope.launch {
                        repository.deleteTeam(team)
                        updateUI()
                    }
                    message("Equipo eliminado exitosamente")

                } catch (e: IOException) {
                    e.printStackTrace()
                    message("Error al eliminar el equipo")
                }
            }
            .setNegativeButton("Cancelar"){dialog,_ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun goToTeamDetails(team:TeamEntity){

        team.id?.let { id ->
            //Aquí va el código para la operación para ver los detalles
           //message("el identificador de ${team.name} es ${id}")
            val nuevoFragmento = TeamElementDetailFragment.newInstance(id.toString())

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.teamsFragmentprueba, nuevoFragmento)
                .addToBackStack("teamsFragment")
                .commit()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateUI() {
        lifecycleScope.launch {
            //Esta linea devuelve los teams que coinsiden con el id de la sesion
            // actual que se obtiene que se obtienen de sharedprefs

            val teamsForUser = withContext(Dispatchers.IO) {
                repository.getTeamsForUser(prefs.getIdSesion())
            }

            teams = teamsForUser
            teamAdapter.updateList(teams)
        }
    }


    private fun message (text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()

        /*Snackbar.make(binding.cl, text, Snackbar.LENGTH_SHORT)
            .setTextColor(Color.parseColor(getString(R.string.color_message)))
            .setBackgroundTint(Color.parseColor(getString(R.string.background_message_color)))
            .show()*/
    }

//    fun onClickNewTeam(view: View) {
//        val dialog = NewTeamDialog(team = TeamEntity(name = "", manager = Manager()), updateUI = {
//            updateUI()
//        }, message = {
//            message(it)
//        })
//        dialog.show(requireActivity().supportFragmentManager, "dialog")
//    }




}



/*
        val teams = ArrayList<Team>()

        val manager = Manager("abel", "smith")
        val employee = Employee("Viktor", "Pak")

        val team = manager.createTeam("equipo 1", manager)
        val team2 = manager.createTeam("equipo 2", manager)
        team.addMember(employee)

        teams.add(team)
        teams.add(team2)

        binding.rvTeams.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTeams.adapter = TeamWorkAdapter(teams)
        */

