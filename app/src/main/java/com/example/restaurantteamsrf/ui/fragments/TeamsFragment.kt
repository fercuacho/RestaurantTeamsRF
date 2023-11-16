package com.example.restaurantteamsrf.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.restaurantteamsrf.R
import com.example.restaurantteamsrf.classes.Team
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.databinding.FragmentTeamsBinding
import com.example.restaurantteamsrf.databinding.FragmentTeamsListBinding
import com.example.restaurantteamsrf.ui.adapters.TeamWorkAdapter
import com.example.restaurantteamsrf.ui.adapters.TeamsAdapter


class TeamsFragment : Fragment() {

    private var _binding: FragmentTeamsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var teamAdapter: TeamWorkAdapter
    //private lateinit var teams : ArrayList<Team>

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

        binding.pbLoading.visibility = View.GONE

        val teams = ArrayList<Team>()
        val team = arguments?.getSerializable("team") as? Team

        recyclerView = view.findViewById(R.id.rvTeams)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        teamAdapter = TeamWorkAdapter(teams)
        recyclerView.adapter = teamAdapter

        if (team != null) {
            teams.add(team)
            teamAdapter.updateData(teams)
        }else {
            var team = Team("equipo prueba")
            teams.add(team)
            teamAdapter.updateData(teams)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}