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
    // private val waterPlantUseCase: WaterPlantUseCase
) : ViewModel() {

    private val _plantsState = MutableStateFlow<UiState<List<Plant>>>(UiState.Loading)
    val plantsState: StateFlow<UiState<List<Plant>>> = _plantsState

    private val _favoritePlantsState = MutableStateFlow<UiState<List<Plant>>>(UiState.Loading)
    val favoritePlantsState: StateFlow<UiState<List<Plant>>> = _favoritePlantsState

    private val _plantsNeedingWateringState = MutableStateFlow<UiState<List<Plant>>>(UiState.Loading)
    val plantsNeedingWateringState: StateFlow<UiState<List<Plant>>> = _plantsNeedingWateringState

    private val _activeFilter = MutableStateFlow<PlantFilter>(PlantFilter.All)
    val activeFilter: StateFlow<PlantFilter> = _activeFilter

    init {
        loadPlants()
        loadFavoritePlants()
    }

    fun loadPlants() {
        viewModelScope.launch {
            getPlantsUseCase.getFlow().collectLatest { result ->
                _plantsState.value = when (result) {
                    is Result.Success -> UiState.Success(result.data)
                    is Result.Error -> UiState.Error(result.exception.message ?: "Unknown error")
                    is Result.Loading -> UiState.Loading
                }
            }
        }
    }

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

    fun loadPlantsNeedingWatering() {
        viewModelScope.launch {
            _plantsNeedingWateringState.value = UiState.Loading

            getPlantsUseCase.getFlow().collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        val needsWatering = result.data.filter { plant ->
                            val currentTimeMillis = System.currentTimeMillis()
                            val daysSinceLastWatering = TimeUnit.MILLISECONDS.toDays(
                                currentTimeMillis - plant.lastWateredTimestamp
                            )
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

    fun setFilter(filter: PlantFilter) {
        if (_activeFilter.value != filter) {
            _activeFilter.value = filter

            when (filter) {
                PlantFilter.All -> { /* в init */ }
                PlantFilter.Favorites -> { /* в init */ }
                PlantFilter.NeedsWatering -> loadPlantsNeedingWatering()
            }
        }
    }

    fun waterPlant(plantId: String) {
        viewModelScope.launch {
            try {
                // val result = waterPlantUseCase(plantId)

                loadPlants()
                loadFavoritePlants()
                if (_activeFilter.value == PlantFilter.NeedsWatering) {
                    loadPlantsNeedingWatering()
                }
            } catch (e: Exception) {

            }
        }
    }

    fun refresh() {
        loadPlants()
        loadFavoritePlants()
        if (_activeFilter.value == PlantFilter.NeedsWatering) {
            loadPlantsNeedingWatering()
        }
    }
}

enum class PlantFilter {
    All,
    Favorites,
    NeedsWatering
}