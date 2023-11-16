package com.example.restaurantteamsrf.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantteamsrf.classes.Team
import com.example.restaurantteamsrf.data.remote.model.TeamDto
import com.example.restaurantteamsrf.databinding.TeamElementBinding
import com.example.restaurantteamsrf.databinding.TeamElementLayoutBinding

class TeamWorkAdapter(private val teams: ArrayList<Team>): RecyclerView.Adapter<TeamWorkAdapter.ViewHolder>(){


    class ViewHolder(private val binding: TeamElementLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        val ivLogoTeam = binding.ivLogoTeam

        fun bind(team: Team){
            binding.apply {
                tvTeamName.text = team.nameTeam   //poner una pondicional por si no existe el nombre todavía
                tvTeamLength.text = team.teamSize().toString()
                //tvWorkPlace.text = team.workPlace()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TeamElementLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = teams.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val team = teams[position]
        holder.bind(team)
//        holder.itemView.setOnClickListener{
//            onTeamClicked(team)
//        }

    }

     fun updateData(newTeams: List<Team>) {
        teams.clear()
        teams.addAll(newTeams)
        notifyDataSetChanged()
    }

}
