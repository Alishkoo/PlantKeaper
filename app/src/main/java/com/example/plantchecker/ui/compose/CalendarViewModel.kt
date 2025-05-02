package com.example.plantchecker.ui.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.Result
import com.example.domain.repository.PlantRepository
import com.example.plantchecker.ui.compose.model.WateringEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

class CalendarViewModel(private val plantRepository: PlantRepository) : ViewModel() {


    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth.asStateFlow()

    private val _selectedDate = MutableStateFlow<LocalDate?>(LocalDate.now())
    val selectedDate: StateFlow<LocalDate?> = _selectedDate.asStateFlow()

    private val _wateringEvents = MutableStateFlow<List<WateringEvent>>(emptyList())
    val wateringEvents: StateFlow<List<WateringEvent>> = _wateringEvents.asStateFlow()

    init {
        loadWateringEvents()
    }

    fun previousMonth() {
        _currentMonth.value = _currentMonth.value.minusMonths(1)
        loadWateringEvents()
    }

    fun nextMonth() {
        _currentMonth.value = _currentMonth.value.plusMonths(1)
        loadWateringEvents()
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun setCurrentMonth(yearMonth: YearMonth) {
        _currentMonth.value = yearMonth
        loadWateringEvents()
    }

    private fun loadWateringEvents() {
        viewModelScope.launch {
            val startOfMonth = _currentMonth.value.atDay(1)
            val endOfMonth = _currentMonth.value.atEndOfMonth()

            plantRepository.getPlants().collect { result ->
                if (result is Result.Success) {
                    val plants = result.data
                    val events = mutableListOf<WateringEvent>()

                    for (plant in plants) {
                        val wateringFrequencyDays = plant.wateringFrequencyDays
                        if (wateringFrequencyDays <= 0) continue

                        val lastWatered = java.util.Date(plant.lastWateredTimestamp)
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()

                        var nextWateringDate = lastWatered.plusDays(wateringFrequencyDays.toLong())

                        while (!nextWateringDate.isAfter(endOfMonth)) {
                            if (!nextWateringDate.isBefore(startOfMonth)) {
                                events.add(
                                    WateringEvent(
                                        plantId = plant.id,
                                        plantName = plant.name,
                                        plantSpecies = plant.species,
                                        date = nextWateringDate
                                    )
                                )
                            }
                            nextWateringDate = nextWateringDate.plusDays(wateringFrequencyDays.toLong())
                        }
                    }

                    _wateringEvents.value = events
                }
            }
        }
    }
}