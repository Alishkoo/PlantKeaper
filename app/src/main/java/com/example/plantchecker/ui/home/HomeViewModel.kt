package com.example.plantchecker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.Result
import com.example.domain.model.Plant
import com.example.domain.usecase.plant.GetFavoritePlantsUseCase
import com.example.domain.usecase.plant.GetPlantsUseCase
import com.example.plantchecker.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getPlantsUseCase: GetPlantsUseCase,
    private val getFavoritePlantsUseCase: GetFavoritePlantsUseCase
) : ViewModel() {

    // StateFlow для списка всех растений
    private val _plantsState = MutableStateFlow<UiState<List<Plant>>>(UiState.Loading)
    val plantsState: StateFlow<UiState<List<Plant>>> = _plantsState

    // StateFlow для списка избранных растений
    private val _favoritePlantsState = MutableStateFlow<UiState<List<Plant>>>(UiState.Loading)
    val favoritePlantsState: StateFlow<UiState<List<Plant>>> = _favoritePlantsState

    // Текущий активный фильтр
    private val _activeFilter = MutableStateFlow<PlantFilter>(PlantFilter.All)
    val activeFilter: StateFlow<PlantFilter> = _activeFilter

    init {
        loadPlants()
        loadFavoritePlants()
    }

    // Загрузка всех растений
    private fun loadPlants() {
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
    private fun loadFavoritePlants() {
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

    // Метод для смены фильтра
    fun setFilter(filter: PlantFilter) {
        _activeFilter.value = filter
    }

    // Обновить данные
    fun refresh() {
        loadPlants()
        loadFavoritePlants()
    }
}

// Enum для фильтрации растений
enum class PlantFilter {
    All,
    Favorites,
    NeedsWatering
}