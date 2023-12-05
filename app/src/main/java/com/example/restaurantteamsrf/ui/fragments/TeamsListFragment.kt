package com.example.restaurantteamsrf.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantteamsrf.R
import com.example.restaurantteamsrf.application.RestaurantTeamsApp
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.data.remote.model.TeamDto
import com.example.restaurantteamsrf.databinding.FragmentTeamsListBinding
import com.example.restaurantteamsrf.ui.adapters.TeamsAdapter
import com.example.restaurantteamsrf.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TeamsListFragment : Fragment() {

    private var _binding: FragmentTeamsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: TeamRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTeamsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //repository = (requireActivity().application as RestaurantTeamsApp).repository

        /*lifecycleScope.launch {
            //val call: Call<List<TeamDto>> = repository.getTeams("cm/games/games_list.php")
            val call: Call<List<TeamDto>> = repository.getTeamsApiary()

            call.enqueue(object: Callback<List<TeamDto>> {
                override fun onResponse(
                    call: Call<List<TeamDto>>,
                    response: Response<List<TeamDto>>
                ) {

                    binding.pbLoading.visibility = View.GONE

                    Log.d(Constants.LOGTAG, getString(R.string.respuesta_del_servidor) + {response.body()})

                    response.body()?.let{ teams ->
                        binding.rvGames.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = TeamsAdapter(teams){ team ->
                                team.id?.let { id ->
                                    //Aquí va el código para la operación para ver los detalles
                                     requireActivity().supportFragmentManager.beginTransaction()
                                        .replace(R.id.fragment_container, TeamDetailFragment.newInstance(id))
                                        .addToBackStack(null)
                                        .commit()
                                }
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<List<TeamDto>>, t: Throwable) {
                    Log.d(Constants.LOGTAG, getString(R.string.error) + {t.message})

                    Toast.makeText(requireActivity(), getString(R.string.no_hay_conexi_n), Toast.LENGTH_SHORT).show()

                    binding.pbLoading.visibility = View.GONE

                }

            })
        }*/

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}