package com.example.restaurantteamsrf.ui.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.example.restaurantteamsrf.R
import com.example.restaurantteamsrf.application.TeamsDBApp
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.data.db.model.AvailabilityEntity
import com.example.restaurantteamsrf.data.db.model.TeamEntity
import com.example.restaurantteamsrf.databinding.FragmentTeamScheduleBinding
import com.kizitonwose.calendar.core.daysOfWeek
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate


class TeamScheduleFragment : Fragment() {

    private var _binding: FragmentTeamScheduleBinding? = null
    private val binding get() = _binding!!

    private var selectedTime: String? = null

    private var teamId: String? = null


    private lateinit var repository: TeamRepository

    @RequiresApi(Build.VERSION_CODES.O)
    val domingo = DayOfWeek.SUNDAY
    @RequiresApi(Build.VERSION_CODES.O)
    val lunes = DayOfWeek.MONDAY

//    val horario = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    val horario: MutableMap<DayOfWeek, String> = mutableMapOf(
        DayOfWeek.MONDAY to "",
        DayOfWeek.TUESDAY to "",
        DayOfWeek.WEDNESDAY to "",
        DayOfWeek.THURSDAY to "",
        DayOfWeek.FRIDAY to "",
        DayOfWeek.SATURDAY to "",
        DayOfWeek.SUNDAY to ""
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTeamScheduleBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireContext().applicationContext as TeamsDBApp).repository

        binding.apply {
            setupDaySwitchListeners(
                switchMonday,
                tpOpenMonday,
                tpCloseMonday
            )

            setupDaySwitchListeners(
                switchTuesday,
                tpOpenTuesday,
                tpCloseTuesday
            )

            setupDaySwitchListeners(
                switchWednesday,
                tpOpenWednesday,
                tpCloseWednesday
            )

            setupDaySwitchListeners(
                switchThursday,
                tpOpenThursday,
                tpCloseThursday
            )

            setupDaySwitchListeners(
                switchFriday,
                tpOpenFriday,
                tpCloseFriday
            )

            setupDaySwitchListeners(
                switchSaturday,
                tpOpenSaturday,
                tpCloseSaturday
            )

            setupDaySwitchListeners(
                switchSunday,
                tpOpenSunday,
                tpCloseSunday
            )
        }

        binding.btnSaveSchedule.setOnClickListener {

            binding.apply {
                horario[DayOfWeek.MONDAY] = getSchedule(switchMonday, tpOpenMonday, tpCloseMonday)
                horario[DayOfWeek.TUESDAY] = getSchedule(switchTuesday, tpOpenTuesday, tpCloseTuesday)
                horario[DayOfWeek.WEDNESDAY] = getSchedule(switchWednesday, tpOpenWednesday, tpCloseWednesday)
                horario[DayOfWeek.THURSDAY] = getSchedule(switchThursday, tpOpenThursday, tpCloseThursday)
                horario[DayOfWeek.FRIDAY] = getSchedule(switchFriday, tpOpenFriday, tpCloseFriday)
                horario[DayOfWeek.SATURDAY] = getSchedule(switchSaturday, tpOpenSaturday, tpCloseSaturday)
                horario[DayOfWeek.SUNDAY] = getSchedule(switchSunday, tpOpenSunday, tpCloseSunday)
            }

            val availabilityList = horario.map { (dayOfWeek, schedule) ->
                AvailabilityEntity(
                    dayOfWeek = dayOfWeek,
                    horario = schedule
                )
            }

            Log.d("DEBUG", "Lista: ${availabilityList.toString()}")

            arguments?.let { it ->
                teamId = it.getString("team_id")
               Toast.makeText(requireContext(), "Mensaje: $teamId", Toast.LENGTH_SHORT).show()

                lifecycleScope.launch(Dispatchers.Main) {
                    teamId?.let { id ->

                        val team = repository.getTeamById(id.toInt())

                        //binding.tvSunday.text = team.name
                        team.availability = availabilityList
                        repository.updateTeam(team)

                    }
                }
            }

                Log.d("DEBUG", "Horario: $horario")

            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }
    }

    private fun setupDaySwitchListeners(
        switch: CompoundButton,
        timePickerOpen: TimePicker,
        timePickerClose: TimePicker
    ) {
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switch.text = "Abierto"
                timePickerOpen.visibility = View.VISIBLE
                timePickerClose.visibility = View.VISIBLE
            } else {
                timePickerOpen.visibility = View.GONE
                timePickerClose.visibility = View.GONE
                switch.text = "Cerrado"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getSchedule(switch: CompoundButton,
                            PickerOpen: TimePicker,
                            PickerClose: TimePicker): String {
        var horarioDia = ""
        return if (switch.isChecked) {
            val horaEntrada = "${PickerOpen.hour}:${PickerOpen.minute}"
            val horaSalida = "${PickerClose.hour}:${PickerClose.minute}"
            horarioDia = "$horaEntrada-$horaSalida"
            horarioDia
        } else {
            "Cerrado"
        }
    }
}
