package com.example.restaurantteamsrf.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.restaurantteamsrf.R
import com.example.restaurantteamsrf.application.RestaurantTeamsApp
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.data.remote.model.TeamDetailDto
import com.example.restaurantteamsrf.databinding.FragmentTeamDetailBinding
import com.example.restaurantteamsrf.model.Video
import com.example.restaurantteamsrf.ui.adapters.VideosAdapter
import com.example.restaurantteamsrf.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TEAM_ID = "team_id"

class TeamDetailFragment : Fragment() {

    private var teamId: String? = null

    private var _binding: FragmentTeamDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: TeamRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videos = ArrayList<Video>()
        arguments?.let {
            teamId = it.getString(TEAM_ID)
            Log.d(Constants.LOGTAG, getString(R.string.id_recibido) + {teamId})

            repository = (requireActivity().application as RestaurantTeamsApp).repository

            lifecycleScope.launch {

                teamId?.let { id ->
                    //val call: Call<TeamDetailDto> = repository.getTeamDetail(id)
                    val call: Call<TeamDetailDto> = repository.getTeamDetailApiary(id)
                    //val call: Call<TeamDetailDto> = repository.getTeamDetailApiary(id)

                    call.enqueue(object: Callback<TeamDetailDto> {
                        override fun onResponse(
                            call: Call<TeamDetailDto>,
                            response: Response<TeamDetailDto>
                        ) {

                            binding.apply {
                                pbLoading.visibility = View.GONE

                                tvTitle.text = response.body()?.name

                                tvManager.text = getString(R.string.manager_del_equipo) + response.body()?.manager
                                tvCountry.text = getString(R.string.pa_s_del_equipo) + response.body()?.contry
                                tvLenght.text = getString(R.string.tama_o_del_equipo) +response.body()?.lenght
                                tvMainDish.text = getString(R.string.platillo_principal) + response.body()?.mainDish

                                val url = response.body()?.video
                                val video = Video(url.toString(), "","")

                                videos.add(video)

                                vpVideos.adapter = VideosAdapter(videos)

                                Glide.with(requireContext())
                                    .load(response.body()?.logo)
                                    .into(ivImage)
                            }

                        }

                        override fun onFailure(call: Call<TeamDetailDto>, t: Throwable) {
                            binding.pbLoading.visibility = View.GONE

                            Toast.makeText(requireActivity(), getString(R.string.no_hay_conexi_n), Toast.LENGTH_SHORT).show()
                        }

                    })
                }

            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTeamDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(teamId: String) =
            TeamDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(TEAM_ID, teamId)
                }
            }
    }
}