package com.example.plantchecker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.Result
import com.example.domain.model.Plant
import com.example.domain.usecase.plant.GetFavoritePlantsUseCase
import com.example.domain.usecase.plant.GetPlantsUseCase
//import com.example.domain.usecase.plant.WaterPlantUseCase
import com.example.plantchecker.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HomeViewModel(
    private val getPlantsUseCase: GetPlantsUseCase,
    private val getFavoritePlantsUseCase: GetFavoritePlantsUseCase,
    // Добавьте WaterPlantUseCase если он у вас есть
    // private val waterPlantUseCase: WaterPlantUseCase
) : ViewModel() {

    // StateFlow для списка всех растений
    private val _plantsState = MutableStateFlow<UiState<List<Plant>>>(UiState.Loading)
    val plantsState: StateFlow<UiState<List<Plant>>> = _plantsState

    // StateFlow для списка избранных растений
    private val _favoritePlantsState = MutableStateFlow<UiState<List<Plant>>>(UiState.Loading)
    val favoritePlantsState: StateFlow<UiState<List<Plant>>> = _favoritePlantsState

    // StateFlow для растений, которым нужен полив
    private val _plantsNeedingWateringState = MutableStateFlow<UiState<List<Plant>>>(UiState.Loading)
    val plantsNeedingWateringState: StateFlow<UiState<List<Plant>>> = _plantsNeedingWateringState

    // Текущий активный фильтр
    private val _activeFilter = MutableStateFlow<PlantFilter>(PlantFilter.All)
    val activeFilter: StateFlow<PlantFilter> = _activeFilter

    init {
        loadPlants()
        loadFavoritePlants()
    }

    // Загрузка всех растений
    fun loadPlants() {
        viewModelScope.launch {
            // Используем Flow из Use Case
            getPlantsUseCase.getFlow().collectLatest { result ->
                _plantsState.value = when (result) {
                    is Result.Success -> UiState.Success(result.data)
                    is Result.Error -> UiState.Error(result.exception.message ?: "Unknown error")
                    is Result.Loading -> UiState.Loading
                }
            }
        }
    }

    // Загрузка избранных растений
    fun loadFavoritePlants() {
        viewModelScope.launch {
            getFavoritePlantsUseCase.getFlow().collectLatest { result ->
                _favoritePlantsState.value = when (result) {
                    is Result.Success -> UiState.Success(result.data)
                    is Result.Error -> UiState.Error(result.exception.message ?: "Unknown error")
                    is Result.Loading -> UiState.Loading
                }
            }
        }
    }

    // Загрузка растений, требующих полива
    fun loadPlantsNeedingWatering() {
        viewModelScope.launch {
            _plantsNeedingWateringState.value = UiState.Loading

            getPlantsUseCase.getFlow().collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        // Фильтруем растения, которым нужен полив
                        val needsWatering = result.data.filter { plant ->
                            val currentTimeMillis = System.currentTimeMillis()
                            val daysSinceLastWatering = TimeUnit.MILLISECONDS.toDays(
                                currentTimeMillis - plant.lastWateredTimestamp
                            )
                            // Если прошло больше дней, чем указано в частоте полива - растение нуждается в поливе
                            daysSinceLastWatering >= plant.wateringFrequencyDays
                        }
                        _plantsNeedingWateringState.value = UiState.Success(needsWatering)
                    }
                    is Result.Error -> {
                        _plantsNeedingWateringState.value = UiState.Error(
                            result.exception.message ?: "Failed to load plants needing watering"
                        )
                    }
                    is Result.Loading -> {
                        _plantsNeedingWateringState.value = UiState.Loading
                    }
                }
            }
        }
    }

    // Метод для смены фильтра
    fun setFilter(filter: PlantFilter) {
        if (_activeFilter.value != filter) {
            _activeFilter.value = filter

            // При смене фильтра обновляем соответствующие данные
            when (filter) {
                PlantFilter.All -> { /* Данные загружаются в init */ }
                PlantFilter.Favorites -> { /* Данные загружаются в init */ }
                PlantFilter.NeedsWatering -> loadPlantsNeedingWatering()
            }
        }
    }

    // Полив растения
    fun waterPlant(plantId: String) {
        viewModelScope.launch {
            try {
                // Если у вас есть WaterPlantUseCase, раскомментируйте эту строку
                // val result = waterPlantUseCase(plantId)

                // Обновляем данные после полива
                loadPlants()
                loadFavoritePlants()
                if (_activeFilter.value == PlantFilter.NeedsWatering) {
                    loadPlantsNeedingWatering()
                }
            } catch (e: Exception) {
                // Обработка ошибок полива
            }
        }
    }

    // Обновить данные
    fun refresh() {
        loadPlants()
        loadFavoritePlants()
        if (_activeFilter.value == PlantFilter.NeedsWatering) {
            loadPlantsNeedingWatering()
        }
    }
}

// Enum для фильтрации растений
enum class PlantFilter {
    All,
    Favorites,
    NeedsWatering
}