package com.example.restaurantteamsrf.calendar.ui.fragments

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import com.example.restaurantteamsrf.R
import com.example.restaurantteamsrf.application.TeamsDBApp
import com.example.restaurantteamsrf.calendar.addStatusBarColorUpdate
import com.example.restaurantteamsrf.calendar.getColorCompat
import com.example.restaurantteamsrf.calendar.layoutInflater
import com.example.restaurantteamsrf.calendar.setTextColorRes
import com.example.restaurantteamsrf.converters.util.displayText
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.data.db.model.AvailabilityEntity
import com.example.restaurantteamsrf.data.db.model.TeamEntity
import com.example.restaurantteamsrf.data.db.model.UserEntity
import com.example.restaurantteamsrf.databinding.Example5CalendarDayBinding
import com.example.restaurantteamsrf.databinding.Example5CalendarHeaderBinding
import com.example.restaurantteamsrf.databinding.Example5EventItemViewBinding
import com.example.restaurantteamsrf.databinding.Example5FragmentBinding
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import kotlin.math.min
import kotlin.random.Random

class Example5WorkScheduleAdapter(openingHours:List<AvailabilityEntity>) :
    RecyclerView.Adapter<Example5WorkScheduleAdapter.Example5WorkScheduleViewHolder>() {

    val integrantes = mutableListOf<UserEntity>()
    val openingHours = openingHours
    var fecha : LocalDate? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Example5WorkScheduleViewHolder {
        return Example5WorkScheduleViewHolder(
            Example5EventItemViewBinding.inflate(parent.context.layoutInflater, parent, false),
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: Example5WorkScheduleViewHolder, position: Int) {
        viewHolder.bind(integrantes[position])
    }

    override fun getItemCount(): Int = integrantes.size

    inner class Example5WorkScheduleViewHolder(val binding: Example5EventItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(integrante: UserEntity) {
//            binding.itemFlightDateText.setBackgroundColor(itemView.context.getColorCompat(integrante.color))
            binding.itemFlightDateText.setBackgroundColor(integrante.color)

            binding.tvNameMember.text = integrante.name

            val diaDeLaSemana = fecha?.let { obtenerDiaDeLaSemana(it) }

            val horario = diaDeLaSemana?.let { buscarHorarioPorDia(openingHours, it) }

            if (horario.isNullOrEmpty()) {
                binding.tvArea.text = "no entro"
            } else {
                binding.tvArea.text = horario
            }
        }
    }

    fun buscarHorarioPorDia(listaOpeningHours: List<AvailabilityEntity>, dayOfWeekBuscado: DayOfWeek): String? {
        val availabilityEncontrado = listaOpeningHours.find { it.dayOfWeek == dayOfWeekBuscado }
        return availabilityEncontrado?.horario
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerDiaDeLaSemana(fecha: LocalDate): DayOfWeek {
        return fecha.dayOfWeek
    }

}



class Example5Fragment : BaseFragment(R.layout.example_5_fragment), HasToolbar {

    override val toolbar: Toolbar?
        get() = null

    override val titleRes: Int = R.string.example_5_title

    private var selectedDate: LocalDate? = null

    private lateinit var repository: TeamRepository
    private var idMember = ""
    private var teamId: String? = null

    private lateinit var workScheduleAdapter : Example5WorkScheduleAdapter


    private lateinit var binding: Example5FragmentBinding

    @RequiresApi(Build.VERSION_CODES.O)
    var listaOpeningHours: List<AvailabilityEntity> = buildList {
        add(AvailabilityEntity(dayOfWeek = DayOfWeek.MONDAY, horario = "Cerrado"))
        add(AvailabilityEntity(dayOfWeek = DayOfWeek.TUESDAY, horario = "13:30-23:00"))
        add(AvailabilityEntity(dayOfWeek = DayOfWeek.WEDNESDAY, horario = "13:30-23:00"))
        add(AvailabilityEntity(dayOfWeek = DayOfWeek.THURSDAY, horario = "13:30-23:00"))
        add(AvailabilityEntity(dayOfWeek = DayOfWeek.FRIDAY, horario = "13:30-23:00"))
        add(AvailabilityEntity(dayOfWeek = DayOfWeek.SATURDAY, horario = "13:30-23:00"))
        add(AvailabilityEntity(dayOfWeek = DayOfWeek.SUNDAY, horario = "13:30-23:00"))
    }

    val fer = UserEntity(name="fer", availability = "[2023-12-15],[2023-12-12],[2023-12-5],[2023-12-19],[2023-12-26] ")
    val joac = UserEntity(name = "joac", availability = "[2023-12-3],[2023-12-6],[2023-12-20],[2023-12-22],[2023-12-26] ")
    val yeo = UserEntity(name = "yeo", availability = "[2023-12-6],[2023-12-14],[2023-12-23],[2023-12-19],[2023-12-27] ")

    val listUsers :List<UserEntity> = listOf(fer,joac,yeo)

    @RequiresApi(Build.VERSION_CODES.O)
    val team = TeamEntity( availability = listaOpeningHours, name = "hola", manager = fer)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addStatusBarColorUpdate(R.color.example_5_toolbar_color)
        binding = Example5FragmentBinding.bind(view)

        repository = (requireContext().applicationContext as TeamsDBApp).repository

        teamId = arguments?.getString("team_id").toString()


        lifecycleScope.launch {
            teamId?.let { id ->

                val team = repository.getTeamById(id.toInt())

                listaOpeningHours = team.availability

                workScheduleAdapter = Example5WorkScheduleAdapter(listaOpeningHours)


                binding.exFiveRv.apply {
                    layoutManager =
                        LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                    adapter = workScheduleAdapter
                }
                workScheduleAdapter.notifyDataSetChanged()

                val daysOfWeek = daysOfWeek()
                val currentMonth = YearMonth.now()
                val startMonth = currentMonth.minusMonths(200)
                val endMonth = currentMonth.plusMonths(200)
                configureBinders(daysOfWeek)
                binding.exFiveCalendar.setup(startMonth, endMonth, daysOfWeek.first())
                binding.exFiveCalendar.scrollToMonth(currentMonth)

                binding.exFiveCalendar.monthScrollListener = { month ->
                    binding.exFiveMonthYearText.text = month.yearMonth.displayText()

                    selectedDate?.let {
                        // Clear selection if we scroll to a new month.
                        selectedDate = null
                        binding.exFiveCalendar.notifyDateChanged(it)
                        updateAdapterForDate(null)
                    }
                }

                binding.exFiveNextMonthImage.setOnClickListener {
                    binding.exFiveCalendar.findFirstVisibleMonth()?.let {
                        binding.exFiveCalendar.smoothScrollToMonth(it.yearMonth.nextMonth)
                    }
                }

                binding.exFivePreviousMonthImage.setOnClickListener {
                    binding.exFiveCalendar.findFirstVisibleMonth()?.let {
                        binding.exFiveCalendar.smoothScrollToMonth(it.yearMonth.previousMonth)
                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateAdapterForDate(date: LocalDate?) {
        workScheduleAdapter.integrantes.clear()

        lifecycleScope.launch {
            teamId?.let { id ->

                val team = repository.getTeamById(id.toInt())


                date?.let { getUsersByDate(team.members, it) }
                    ?.let { workScheduleAdapter.integrantes.addAll(it) }

                workScheduleAdapter.fecha = date
                Log.d("Example5WorkScheduleAdapter", "date: $date")

                workScheduleAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun configureBinders(daysOfWeek: List<DayOfWeek>) {
        @RequiresApi(Build.VERSION_CODES.O)
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = Example5CalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        if (selectedDate != day.date) {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            val binding = this@Example5Fragment.binding
                            binding.exFiveCalendar.notifyDateChanged(day.date)
                            oldDate?.let { binding.exFiveCalendar.notifyDateChanged(it) }
                            updateAdapterForDate(day.date)
                        }
                    }
                }
            }
        }


        binding.exFiveCalendar.dayBinder = object : MonthDayBinder<DayViewContainer> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun create(view: View) = DayViewContainer(view)
            @RequiresApi(Build.VERSION_CODES.O)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data

                val context = container.binding.root.context
                val textView = container.binding.exFiveDayText
                val layout = container.binding.exFiveDayLayout
                textView.text = data.date.dayOfMonth.toString()


                val rayita1 = container.binding.ex1
                val rayita2 = container.binding.ex2
                val rayita3 = container.binding.ex3
                val rayita4 = container.binding.ex4
                val rayita5 = container.binding.ex5
                val rayita6 = container.binding.ex6


                rayita1.background = null
                rayita2.background = null
                rayita3.background = null
                rayita4.background = null
                rayita5.background = null
                rayita6.background = null

                lifecycleScope.launch {
                    teamId?.let { id ->

                        val team = repository.getTeamById(id.toInt())

                        val availability = getHorarioPorDia(team, data.date.dayOfWeek)

                        if (data.position == DayPosition.MonthDate) {
                            textView.setTextColorRes(R.color.example_5_text_grey)
                            layout.setBackgroundResource(if (selectedDate == data.date) R.drawable.example_5_selected_bg else 0)

                            if (availability == "Cerrado") {
                                textView.isEnabled = false
                                textView.setTextColorRes(R.color.example_1_white_light)
                                textView.background = null
                            } else {
                                textView.isEnabled = true
                                textView.setTextColorRes(R.color.white)

                                val usersWithTargetDate = getUsersByDate(team.members, data.date)

                                if (usersWithTargetDate.isNotEmpty()) {
                                    for (i in 0 until min(usersWithTargetDate.size, 2)) {
                                        val rayita = when (i) {
                                            0 -> rayita1
                                            1 -> rayita2
                                            2 -> rayita3
                                            3 -> rayita4
                                            4 -> rayita5
                                            5 -> rayita6
                                            else -> null
                                        }
//                                rayita?.setBackgroundColor(context.getColorCompat(usersWithTargetDate[i].color))
                                        rayita?.setBackgroundColor(usersWithTargetDate[i].color)

                                    }
                                }
                            }
                        } else {
                            textView.setTextColorRes(R.color.example_5_text_grey_light)
                            layout.background = null
                        }
                    }
                }
            }
        }


        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = Example5CalendarHeaderBinding.bind(view).legendLayout.root
        }

        val typeFace = Typeface.create("sans-serif-light", Typeface.NORMAL)
        binding.exFiveCalendar.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                @RequiresApi(Build.VERSION_CODES.O)
                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    // Setup each header day text if we have not done that already.
                    if (container.legendLayout.tag == null) {
                        container.legendLayout.tag = data.yearMonth
                        container.legendLayout.children.map { it as TextView }
                            .forEachIndexed { index, tv ->
                                tv.text = daysOfWeek[index].displayText(uppercase = true)
                                tv.setTextColorRes(R.color.white)
                                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                                tv.typeface = typeFace
                            }
                    }
                }
            }
    }


    fun getHorarioPorDia(team: TeamEntity, dayOfWeek: DayOfWeek): String? {
        return team.availability
            .filter { it.dayOfWeek == dayOfWeek }
            .map { it.horario }
            .firstOrNull()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUsersByDate(users: List<UserEntity>, targetDate: LocalDate): List<UserEntity> {
        val targetDateString = targetDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        return users.filter { user ->
            user.availability.contains(targetDateString)
        }
    }


}


