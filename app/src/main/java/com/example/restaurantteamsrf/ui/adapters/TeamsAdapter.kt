package com.example.restaurantteamsrf.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restaurantteamsrf.data.remote.model.TeamDto
import com.example.restaurantteamsrf.databinding.TeamElementBinding
import com.squareup.picasso.Picasso

class TeamsAdapter (
    private val teams: List<TeamDto>,
    private val onTeamClicked: (TeamDto) -> Unit
): RecyclerView.Adapter<TeamsAdapter.ViewHolder>(){

    class ViewHolder(private val binding: TeamElementBinding): RecyclerView.ViewHolder(binding.root){

        val ivThumbnail = binding.ivThumbnail

        fun bind(team: TeamDto){
            binding.apply {
                tvTitle.text = team.name
                tvDeveloper.text = team.manager
                tvRating.text = team.id
                tvReleased.text = team.tamano

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = TeamElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = teams.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val team = teams[position]

//        Picasso.get()
//            .load(team.logo)
//            .into(holder.ivThumbnail)

        Glide.with(holder.itemView.context)
            .load(team.logo)
            .into(holder.ivThumbnail)

        holder.bind(team)
        holder.itemView.setOnClickListener{
            onTeamClicked(team)
        }
    }
}
