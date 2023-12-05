package com.example.restaurantteamsrf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantteamsrf.data.db.model.TeamEntity
import com.example.restaurantteamsrf.data.db.model.UserEntity
import com.example.restaurantteamsrf.databinding.MemberElementBinding
import com.example.restaurantteamsrf.databinding.TeamElementLayoutBinding
import com.example.restaurantteamsrf.util.MemberDiffUtil
import com.example.restaurantteamsrf.util.TeamDiffUtil

class MembersAdapter(private var members: List<UserEntity>): RecyclerView.Adapter<MembersAdapter.ViewHolder>(){

    class ViewHolder (private val binding: MemberElementBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(member: UserEntity){
            binding.apply {
                tvNameMember.text = member.name
                tvEmail.text = member.email
                tvLastnameMember.text = member.lastname
                tvPhoneNumber.text = member.number
                if (member.tipoUsuario == "manager"){
                    tvIsManager.visibility = View.VISIBLE
                }else{
                    tvIsManager.visibility = View.GONE
                }


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MemberElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = members.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val member = members[position]
        holder.bind(member)
    }

    fun updateList(newlist: List<UserEntity>){
        val userDiff = MemberDiffUtil(members, newlist)
        val result = DiffUtil.calculateDiff(userDiff)
        members = newlist
        result.dispatchUpdatesTo(this)
    }

}