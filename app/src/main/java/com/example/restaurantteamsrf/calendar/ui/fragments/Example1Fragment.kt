package com.example.restaurantteamsrf.calendar.ui.fragments

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.children
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import com.example.restaurantteamsrf.R
import com.example.restaurantteamsrf.application.TeamsDBApp
import com.example.restaurantteamsrf.calendar.addStatusBarColorUpdate
import com.example.restaurantteamsrf.calendar.setTextColorRes
import com.example.restaurantteamsrf.converters.util.displayText
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.data.db.model.AvailabilityEntity
import com.example.restaurantteamsrf.data.db.model.UserEntity
import com.example.restaurantteamsrf.data.db.model.utils.modifyUserProperties
import com.example.restaurantteamsrf.databinding.Example1CalendarDayBinding
import com.example.restaurantteamsrf.databinding.Example1FragmentBinding
import com.example.restaurantteamsrf.ui.fragments.TeamElementDetailFragment
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.WeekDayPosition
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekCalendarView
import com.kizitonwose.calendar.view.WeekDayBinder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class Example1Fragment : BaseFragment(R.layout.example_1_fragment), HasToolbar {

    override val toolbar: Toolbar?
        get() = null

    override val titleRes: Int = R.string.example_1_title

    private lateinit var binding: Example1FragmentBinding
    private val monthCalendarView: CalendarView get() = binding.exOneCalendar
    private val weekCalendarView: WeekCalendarView get() = binding.exOneWeekCalendar

    private lateinit var repository: TeamRepository
    private var user = UserEntity()

    private var disponibilidad = ""

    private var idMember = ""
    private var idTeam = ""


    private var availabilityMap : Map<DayOfWeek?, String> = emptyMap()


    private val selectedDates = mutableSetOf<LocalDate>()
    @RequiresApi(Build.VERSION_CODES.O)
    private val today = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addStatusBarColorUpdate(R.color.example_1_bg_light)
        binding = Example1FragmentBinding.bind(view)

        repository = (requireContext().applicationContext as TeamsDBApp). repository

        idMember = arguments?.getString("ID_MEMBER").toString()
        idTeam = arguments?.getString("ID_TEAM").toString()


        val daysOfWeek = daysOfWeek()
        binding.legendLayout.root.children
            .map { it as TextView }
            .forEachIndexed { index, textView ->
                textView.text = daysOfWeek[index].displayText()
                textView.setTextColorRes(R.color.example_1_white)
            }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
        setupMonthCalendar(startMonth, endMonth, currentMonth, daysOfWeek)
        setupWeekCalendar(startMonth, endMonth, currentMonth, daysOfWeek)

        monthCalendarView.isInvisible = binding.weekModeCheckBox.isChecked
        weekCalendarView.isInvisible = !binding.weekModeCheckBox.isChecked

        binding.weekModeCheckBox.setOnCheckedChangeListener(weekModeToggled)

        binding.btnSaveDates.setOnClickListener{

            //si lo recibo
            //Toast.makeText(requireContext(), "el identificador es: "+ identificador, Toast.LENGTH_LONG).show()

            if(selectedDates.isNotEmpty()){

                lifecycleScope.launch {
                    val resultado = withContext(Dispatchers.IO) {
                        // Operaciones de E/S en el hilo de fondo
                        user = repository.getCurrentUser(idMember)!!
                        disponibilidad = selectedDates.toString()
                        user.availability = disponibilidad
                        repository.updateUser(user)

                        var team = repository.getTeamById(idTeam.toInt())

                        if (team != null){
                            team.modifyUserProperties(userId = idMember, newAvailability = disponibilidad)
                            repository.updateTeam(team)
                        }
                        // Devolver el valor de disponibilidad
                        disponibilidad
                    }

                    // Cambiar al hilo principal para mostrar el resultado al usuario
                    lifecycleScope.launch(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Disponibilidad guardada correctamente: $resultado", Toast.LENGTH_SHORT).show()
                        // Otra opción: Actualizar un TextView u otro componente de la interfaz de usuario
                        // textViewResultado.text = "Disponibilidad: $resultado"
                    }
                }

                activity?.finish()
            } else {
                Toast.makeText(requireContext(),"Marca al menos una fecha", Toast.LENGTH_LONG).show()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupMonthCalendar(
        startMonth: YearMonth,
        endMonth: YearMonth,
        currentMonth: YearMonth,
        daysOfWeek: List<DayOfWeek>,
    ) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay
            val textView = Example1CalendarDayBinding.bind(view).exOneDayText

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        dateClicked(date = day.date)
                    }
                }
            }
        }
        monthCalendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            @RequiresApi(Build.VERSION_CODES.O)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                bindDate(data.date, container.textView, data.position == DayPosition.MonthDate)
            }
        }
        monthCalendarView.monthScrollListener = { updateTitle() }
        monthCalendarView.setup(startMonth, endMonth, daysOfWeek.first())
        monthCalendarView.scrollToMonth(currentMonth)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupWeekCalendar(
        startMonth: YearMonth,
        endMonth: YearMonth,
        currentMonth: YearMonth,
        daysOfWeek: List<DayOfWeek>,
    ) {
        class WeekDayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: WeekDay
            val textView = Example1CalendarDayBinding.bind(view).exOneDayText

            init {
                view.setOnClickListener {
                    if (day.position == WeekDayPosition.RangeDate) {
                        dateClicked(date = day.date)
                    }
                }
            }
        }
        weekCalendarView.dayBinder = object : WeekDayBinder<WeekDayViewContainer> {
            override fun create(view: View): WeekDayViewContainer = WeekDayViewContainer(view)
            override fun bind(container: WeekDayViewContainer, data: WeekDay) {
                container.day = data
                bindDate(data.date, container.textView, data.position == WeekDayPosition.RangeDate)
            }
        }
        weekCalendarView.weekScrollListener = { updateTitle() }
        weekCalendarView.setup(
            startMonth.atStartOfMonth(),
            endMonth.atEndOfMonth(),
            daysOfWeek.first(),
        )
        weekCalendarView.scrollToWeek(currentMonth.atStartOfMonth())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindDate(date: LocalDate, textView: TextView, isSelectable: Boolean) {
        textView.text = date.dayOfMonth.toString()
        // Verificar si el día está en el mapa de disponibilidad
        //val availability = availabilityMap[date]
        lifecycleScope.launch {

            //val resultado = withContext(Dispatchers.IO)

            val team = repository.getTeamById(idTeam.toInt())

                availabilityMap = convertToAvailabilityMap(team.availability)

            // }
            val availability = availabilityMap[date.dayOfWeek]


            if (availability == "Cerrado") {
                textView.isEnabled = false
                textView.setTextColorRes(R.color.example_1_white_light)
                textView.background = null
            } else {
                textView.isEnabled = true
                if (isSelectable) {
                    when {
                        selectedDates.contains(date) -> {
                            textView.setTextColorRes(R.color.example_1_bg)
                            textView.setBackgroundResource(R.drawable.example_1_selected_bg)
                        }

                        today == date -> {
                            textView.setTextColorRes(R.color.example_1_white)
                            textView.setBackgroundResource(R.drawable.example_1_today_bg)
                        }

                        else -> {
                            textView.setTextColorRes(R.color.example_1_white)
                            textView.background = null
                        }
                    }
                } else {
                    textView.setTextColorRes(R.color.example_1_white_light)
                    textView.background = null
                }
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateClicked(date: LocalDate) {
        // Verificar si el día está en el mapa de disponibilidad
        //val availability = availabilityMap[date]

        lifecycleScope.launch {

            //val resultado = withContext(Dispatchers.IO) {
            val team = repository.getTeamById(idTeam.toInt())

            availabilityMap = convertToAvailabilityMap(team.availability)
            // }
            val availability =
                availabilityMap[date.dayOfWeek] //tal vez esto tenga que estar adentro de lifecycle

            if (availability != "Cerrado") {

                if (selectedDates.contains(date)) {

                    selectedDates.remove(date)

                } else {

                    selectedDates.add(date)

                }
                // Refresh both calendar views..
                monthCalendarView.notifyDateChanged(date)
                weekCalendarView.notifyDateChanged(date)

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun updateTitle() {
        val isMonthMode = !binding.weekModeCheckBox.isChecked
        if (isMonthMode) {
            val month = monthCalendarView.findFirstVisibleMonth()?.yearMonth ?: return
            binding.exOneYearText.text = month.year.toString()
            binding.exOneMonthText.text = month.month.displayText(short = false)
        } else {
            val week = weekCalendarView.findFirstVisibleWeek() ?: return
            // In week mode, we show the header a bit differently because
            // an index can contain dates from different months/years.
            val firstDate = week.days.first().date
            val lastDate = week.days.last().date
            if (firstDate.yearMonth == lastDate.yearMonth) {
                binding.exOneYearText.text = firstDate.year.toString()
                binding.exOneMonthText.text = firstDate.month.displayText(short = false)
            } else {
                binding.exOneMonthText.text =
                    firstDate.month.displayText(short = false) + " - " +
                    lastDate.month.displayText(short = false)
                if (firstDate.year == lastDate.year) {
                    binding.exOneYearText.text = firstDate.year.toString()
                } else {
                    binding.exOneYearText.text = "${firstDate.year} - ${lastDate.year}"
                }
            }
        }
    }

    private val weekModeToggled = object : CompoundButton.OnCheckedChangeListener {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCheckedChanged(buttonView: CompoundButton, monthToWeek: Boolean) {
            // We want the first visible day to remain visible after the
            // change so we scroll to the position on the target calendar.
            if (monthToWeek) {
                val targetDate = monthCalendarView.findFirstVisibleDay()?.date ?: return
                weekCalendarView.scrollToWeek(targetDate)
            } else {
                // It is possible to have two months in the visible week (30 | 31 | 1 | 2 | 3 | 4 | 5)
                // We always choose the second one. Please use what works best for your use case.
                val targetMonth = weekCalendarView.findLastVisibleDay()?.date?.yearMonth ?: return
                monthCalendarView.scrollToMonth(targetMonth)
            }

            val weekHeight = weekCalendarView.height
            // If OutDateStyle is EndOfGrid, you could simply multiply weekHeight by 6.
            val visibleMonthHeight = weekHeight *
                monthCalendarView.findFirstVisibleMonth()?.weekDays.orEmpty().count()

            val oldHeight = if (monthToWeek) visibleMonthHeight else weekHeight
            val newHeight = if (monthToWeek) weekHeight else visibleMonthHeight

            // Animate calendar height changes.
            val animator = ValueAnimator.ofInt(oldHeight, newHeight)
            animator.addUpdateListener { anim ->
                monthCalendarView.updateLayoutParams {
                    height = anim.animatedValue as Int
                }
                // A bug is causing the month calendar to not redraw its children
                // with the updated height during animation, this is a workaround.
                monthCalendarView.children.forEach { child ->
                    child.requestLayout()
                }
            }

            animator.doOnStart {
                if (!monthToWeek) {
                    weekCalendarView.isInvisible = true
                    monthCalendarView.isVisible = true
                }
            }
            animator.doOnEnd {
                if (monthToWeek) {
                    weekCalendarView.isVisible = true
                    monthCalendarView.isInvisible = true
                } else {
                    // Allow the month calendar to be able to expand to 6-week months
                    // in case we animated using the height of a visible 5-week month.
                    // Not needed if OutDateStyle is EndOfGrid.
                    monthCalendarView.updateLayoutParams { height = WRAP_CONTENT }
                }
                updateTitle()
            }
            animator.duration = 250
            animator.start()
        }
    }


    private fun convertToAvailabilityMap(availabilities: List<AvailabilityEntity>): Map<DayOfWeek?, String> {
        return availabilities.associate { it.dayOfWeek to it.horario }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertirStringAMapa(cadenaDisponibilidad: String): Map<DayOfWeek, String> {
        val mapaResultado: MutableMap<DayOfWeek, String> = mutableMapOf()

        // Eliminar los corchetes y dividir la cadena en pares key-value
        val pares = cadenaDisponibilidad
            .replace("{", "")
            .replace("}", "")
            .split(", ")

        // Formato de tiempo para asegurar consistencia
        val formatter = DateTimeFormatter.ofPattern("H:mm")

        // Mapeo de nombres de días a DayOfWeek
        val dayNameToEnum = mapOf(
            "MONDAY" to DayOfWeek.MONDAY,
            "TUESDAY" to DayOfWeek.TUESDAY,
            "WEDNESDAY" to DayOfWeek.WEDNESDAY,
            "THURSDAY" to DayOfWeek.THURSDAY,
            "FRIDAY" to DayOfWeek.FRIDAY,
            "SATURDAY" to DayOfWeek.SATURDAY,
            "SUNDAY" to DayOfWeek.SUNDAY
        )

        // Iterar sobre los pares y agregar al mapa
        for (par in pares) {
            try {
                val (dia, horario) = par.split("=")

                val dayOfWeek = dayNameToEnum[dia]
                    ?: throw IllegalArgumentException("Día no válido: $dia")

                mapaResultado[dayOfWeek] = when {
                    horario == "Cerrado" -> "Cerrado"
                    else -> {
                        val (horaInicio, horaFin) = horario.split("-")
                        "${formatter.format(LocalTime.parse(horaInicio))}-${formatter.format(LocalTime.parse(horaFin))}"
                    }
                }
            } catch (e: Exception) {
                // Manejo de errores: registrar la excepción o mostrar un mensaje, según sea necesario
                Log.e("Error", "Error al procesar la cadena de disponibilidad: $par", e)
            }
        }

        return mapaResultado
    }

//    private fun convertirStringAMapa(cadenaDisponibilidad: String): Map<DayOfWeek, String> {
//        val mapaResultado: MutableMap<DayOfWeek, String> = mutableMapOf()
//
//        // Eliminar los corchetes y dividir la cadena en pares key-value
//        val pares = cadenaDisponibilidad
//            .replace("{", "")
//            .replace("}", "")
//            .split(", ")
//
//        // Formato de tiempo para asegurar consistencia
//        val formatter = DateTimeFormatter.ofPattern("H:mm")
//
//        // Iterar sobre los pares y agregar al mapa
//        for (par in pares) {
//            try {
//                val (dia, horario) = par.split("=")
//
//                val dayOfWeek = when (dia) {
//                    "MONDAY" -> DayOfWeek.MONDAY
//                    "TUESDAY" -> DayOfWeek.TUESDAY
//                    "WEDNESDAY" -> DayOfWeek.WEDNESDAY
//                    "THURSDAY" -> DayOfWeek.THURSDAY
//                    "FRIDAY" -> DayOfWeek.FRIDAY
//                    "SATURDAY" -> DayOfWeek.SATURDAY
//                    "SUNDAY" -> DayOfWeek.SUNDAY
//                    else -> throw IllegalArgumentException("Día no válido: $dia")
//                }
//
//                mapaResultado[dayOfWeek] = when {
//                    horario == "Cerrado" -> "Cerrado"
//                    else -> {
//                        val (horaInicio, horaFin) = horario.split("-")
//                        "${formatter.format(LocalTime.parse(horaInicio))}-${formatter.format(LocalTime.parse(horaFin))}"
//                    }
//                }
//            } catch (e: Exception) {
//                // Manejo de errores: registrar la excepción o mostrar un mensaje, según sea necesario
//                Log.e("Error", "Error al procesar la cadena de disponibilidad: $par", e)
//            }
//        }
//
//        return mapaResultado
//    }


}
