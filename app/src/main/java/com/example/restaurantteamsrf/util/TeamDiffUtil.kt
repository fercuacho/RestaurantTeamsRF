package com.example.restaurantteamsrf.util

import androidx.recyclerview.widget.DiffUtil
import com.example.restaurantteamsrf.classes.Team
import com.example.restaurantteamsrf.data.db.model.TeamEntity

class TeamDiffUtil(
    private val oldList: List<TeamEntity>,
    private val newList: List<TeamEntity>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}