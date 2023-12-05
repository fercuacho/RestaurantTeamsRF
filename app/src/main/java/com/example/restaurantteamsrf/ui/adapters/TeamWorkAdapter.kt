package com.example.restaurantteamsrf.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantteamsrf.classes.Team
import com.example.restaurantteamsrf.data.db.model.TeamEntity
import com.example.restaurantteamsrf.data.remote.model.TeamDto
import com.example.restaurantteamsrf.databinding.TeamElementBinding
import com.example.restaurantteamsrf.databinding.TeamElementLayoutBinding
import com.example.restaurantteamsrf.util.TeamDiffUtil

class TeamWorkAdapter(private var teams:List<TeamEntity>, private val onClickDeleteTeam: (TeamEntity) -> Unit, private val goToDetailTeam: (TeamEntity) -> Unit): RecyclerView.Adapter<TeamWorkAdapter.ViewHolder>(){

    class ViewHolder(private val binding: TeamElementLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        val ivLogoTeam = binding.ivLogoTeam
        val botonEliminar = binding.btnDeleteTeam

        fun bind(team: TeamEntity){
            binding.apply {
                tvTeamName.text = team.name   //poner una condicional por si no existe el nombre todavía
                tvTeamLength.text = team.members.size.toString()
                //tvWorkPlace.text = team.members.toString()
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
        holder.botonEliminar.setOnClickListener {
            onClickDeleteTeam(team)
        }
        holder.itemView.setOnClickListener{
            goToDetailTeam(team)
        }

    }

    fun updateList(newlist: List<TeamEntity>){
        val teamDiff = TeamDiffUtil(teams, newlist)
        val result = DiffUtil.calculateDiff(teamDiff)
        teams = newlist
        result.dispatchUpdatesTo(this)
    }

}
