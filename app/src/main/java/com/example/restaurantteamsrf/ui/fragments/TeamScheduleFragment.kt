package com.example.restaurantteamsrf.ui.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
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
import com.example.ticker.core.ui.Ticker
import com.kizitonwose.calendar.core.daysOfWeek
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class TeamScheduleFragment : Fragment() {

    private var _binding: FragmentTeamScheduleBinding? = null
    private val binding get() = _binding!!

    private var selectedTime: String? = null

    private var teamId: String? = null


    private lateinit var repository: TeamRepository

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

        OcultarTickers()

        binding.apply {
            setupDaySwitchListeners(
                switchMonday,
                tpOpenMonday,
                tpCloseMonday,
                IvLine1,
                TvMembersMonday,
                SpMembersMonday
            )

            setupDaySwitchListeners(
                switchTuesday,
                tpOpenTuesday,
                tpCloseTuesday,
                IvLine2,
                TvMembersTuesday,
                SpMembersTuesday
            )

            setupDaySwitchListeners(
                switchWednesday,
                tpOpenWednesday,
                tpCloseWednesday,
                IvLine3,
                TvMembersWednesday,
                SpMembersWednesday
            )

            setupDaySwitchListeners(
                switchThursday,
                tpOpenThursday,
                tpCloseThursday,
                IvLine4,
                TvMembersThursday,
                SpMembersThursday
            )

            setupDaySwitchListeners(
                switchFriday,
                tpOpenFriday,
                tpCloseFriday,
                IvLine5,
                TvMembersFriday,
                SpMembersFriday
            )

            setupDaySwitchListeners(
                switchSaturday,
                tpOpenSaturday,
                tpCloseSaturday,
                IvLine6,
                TvMembersSaturday,
                SpMembersSaturday
            )

            setupDaySwitchListeners(
                switchSunday,
                tpOpenSunday,
                tpCloseSunday,
                IvLine7,
                TvMembersSunday,
                SpMembersSunday
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


            binding.apply {
                actualizarHorasYMiembros(
                    availabilityList,
                    DayOfWeek.MONDAY,
                    tpOpenMonday.getCurrentlySelectedTime(),
                    tpCloseMonday.getCurrentlySelectedTime(),
                    SpMembersMonday,
                    switchMonday
                )

                actualizarHorasYMiembros(
                    availabilityList,
                    DayOfWeek.TUESDAY,
                    tpOpenTuesday.getCurrentlySelectedTime(),
                    tpCloseTuesday.getCurrentlySelectedTime(),
                    SpMembersTuesday,
                    switchTuesday
                )

                actualizarHorasYMiembros(
                    availabilityList,
                    DayOfWeek.WEDNESDAY,
                    tpOpenWednesday.getCurrentlySelectedTime(),
                    tpCloseWednesday.getCurrentlySelectedTime(),
                    SpMembersWednesday,
                    switchWednesday
                )

                actualizarHorasYMiembros(
                    availabilityList,
                    DayOfWeek.THURSDAY,
                    tpOpenThursday.getCurrentlySelectedTime(),
                    tpCloseThursday.getCurrentlySelectedTime(),
                    SpMembersThursday,
                    switchThursday
                )

                actualizarHorasYMiembros(
                    availabilityList,
                    DayOfWeek.FRIDAY,
                    tpOpenFriday.getCurrentlySelectedTime(),
                    tpCloseFriday.getCurrentlySelectedTime(),
                    SpMembersFriday,
                    switchFriday
                )

                actualizarHorasYMiembros(
                    availabilityList,
                    DayOfWeek.SATURDAY,
                    tpOpenSaturday.getCurrentlySelectedTime(),
                    tpCloseSaturday.getCurrentlySelectedTime(),
                    SpMembersSaturday,
                    switchSaturday
                )

                actualizarHorasYMiembros(
                    availabilityList,
                    DayOfWeek.SUNDAY,
                    tpOpenSunday.getCurrentlySelectedTime(),
                    tpCloseSunday.getCurrentlySelectedTime(),
                    SpMembersSunday,
                    switchSunday
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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getSchedule(switch: CompoundButton,
                            PickerOpen: Ticker,
                            PickerClose: Ticker
    ): String {
        var horarioDia = ""
        return if (switch.isChecked) {
            val horaEntrada = PickerOpen.getCurrentlySelectedTime().trim()
            val horaSalida = PickerClose.getCurrentlySelectedTime().trim()
            horarioDia = "$horaEntrada-$horaSalida"
            horarioDia
        } else {
            "Cerrado"
        }
    }

    private fun setupDaySwitchListeners(
        switch: CompoundButton,
        timePickerOpen: Ticker,
        timePickerClose: Ticker,
        linea: ImageView,
        textView: TextView,
        spinner: Spinner
    ) {
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switch.text = "Abierto"
                MuestraPicker(timePickerOpen)
                MuestraPicker(timePickerClose)
                MuestraLinea(linea)
                MuestraTextMinMembers(textView)
                MuestraSpinner(spinner)
                IniciaSpinner(spinner)

            } else {
                OcultaPicker(timePickerOpen)
                OcultaPicker(timePickerClose)
                OcultaLinea(linea)
                OcultaTextMinMembers(textView)
                OcultaSpinner(spinner)
                switch.text = "Cerrado"
            }
        }
    }

    private fun MuestraPicker(ticker: Ticker){
        ticker.alpha = 0f
        ticker.visibility = View.VISIBLE
        ticker.animate().alpha(1f).setDuration(500).start()
    }

    private fun OcultaPicker(ticker: Ticker){
        ticker.animate().alpha(0f).setDuration(500).withEndAction { ticker.visibility = View.GONE }.start()
    }

    private fun MuestraLinea(linea: ImageView){
        linea.visibility = View.VISIBLE
    }

    private fun OcultaLinea(linea: ImageView){
        linea.visibility = View.GONE
    }

    private fun MuestraTextMinMembers(textView: TextView){
        textView.visibility = View.VISIBLE
    }

    private fun OcultaTextMinMembers(textView: TextView){
        textView.visibility = View.GONE
    }

    private fun MuestraSpinner(spinner: Spinner){
        spinner.visibility = View.VISIBLE
    }

    private fun OcultaSpinner(spinner: Spinner){
        spinner.visibility = View.GONE
    }


    private fun OcultarTickers(){

        binding.apply {
            OcultaPicker(tpOpenMonday)
            OcultaPicker(tpCloseMonday)
            OcultaPicker(tpOpenTuesday)
            OcultaPicker(tpCloseTuesday)
            OcultaPicker(tpOpenWednesday)
            OcultaPicker(tpCloseWednesday)
            OcultaPicker(tpOpenThursday)
            OcultaPicker(tpCloseThursday)
            OcultaPicker(tpOpenFriday)
            OcultaPicker(tpCloseFriday)
            OcultaPicker(tpOpenSaturday)
            OcultaPicker(tpCloseSaturday)
            OcultaPicker(tpOpenSunday)
            OcultaPicker(tpCloseSunday)

            OcultaLinea(IvLine1)
            OcultaLinea(IvLine2)
            OcultaLinea(IvLine3)
            OcultaLinea(IvLine4)
            OcultaLinea(IvLine5)
            OcultaLinea(IvLine6)
            OcultaLinea(IvLine7)

            OcultaTextMinMembers(TvMembersMonday)
            OcultaTextMinMembers(TvMembersTuesday)
            OcultaTextMinMembers(TvMembersWednesday)
            OcultaTextMinMembers(TvMembersThursday)
            OcultaTextMinMembers(TvMembersFriday)
            OcultaTextMinMembers(TvMembersSaturday)
            OcultaTextMinMembers(TvMembersSunday)

        }
    }

    private fun IniciaSpinner(spinner: Spinner){

        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.number_members,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner.adapter = adapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calcularHorasEntreTiempos(tiempoInicio: String, tiempoFin: String): Double {
        // Definir el formato de hora (hh:mm)
        val formatoHora = DateTimeFormatter.ofPattern("HH:mm")

        // Eliminar espacios adicionales antes de analizar
        val horaInicio = LocalTime.parse(tiempoInicio.trim(), formatoHora)
        val horaFin = LocalTime.parse(tiempoFin.trim(), formatoHora)

        // Calcular la diferencia en minutos como un valor decimal
        val diferenciaEnMinutos = Duration.between(horaInicio, horaFin).toMinutes()

        // Convertir minutos a horas con decimales
        val diferenciaEnHoras = diferenciaEnMinutos.toDouble() / 60.0

        Log.d("DEBUG","diferencia entre $horaInicio y $horaFin es: $diferenciaEnHoras")

        return diferenciaEnHoras
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun actualizarHorasYMiembros(
        availabilityList: List<AvailabilityEntity>,
        dayOfWeekToFilter: DayOfWeek,
        tiempoInicio: String,
        tiempoFin: String,
        spinner: Spinner,
        switch: CompoundButton
    ) {
        if (switch.isChecked) {
        val filteredList = availabilityList.filter { it.dayOfWeek == dayOfWeekToFilter }

        if (filteredList.isNotEmpty()) {
            val availabilityEntity = filteredList.first() // Tomamos la primera entidad de la lista filtrada

            // Calculamos la diferencia de horas utilizando la función calcularHorasEntreTiempos
            val diferenciaEnHoras = calcularHorasEntreTiempos(tiempoInicio, tiempoFin)

            // Asignamos el resultado a la propiedad shiftLength
            availabilityEntity.shiftLength = diferenciaEnHoras
            val num = spinner.selectedItem?.toString() ?: "1"
            availabilityEntity.minMembers = num.toInt()

        }

        }
    }
}
